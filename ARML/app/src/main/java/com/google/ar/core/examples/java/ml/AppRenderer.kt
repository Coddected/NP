/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.examples.java.ml

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.opengl.Matrix
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Anchor
import com.google.ar.core.Camera
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import com.google.ar.core.TrackingState
import com.google.ar.core.examples.java.common.helpers.DisplayRotationHelper
import com.google.ar.core.examples.java.common.samplerender.SampleRender
import com.google.ar.core.examples.java.common.samplerender.Texture
import com.google.ar.core.examples.java.common.samplerender.arcore.BackgroundRenderer
import com.google.ar.core.examples.java.ml.classification.DetectedObjectResult
import com.google.ar.core.examples.java.ml.classification.GoogleCloudVisionDetector
import com.google.ar.core.examples.java.ml.classification.MLKitObjectDetector
import com.google.ar.core.examples.java.ml.classification.ObjectDetector
import com.google.ar.core.examples.java.ml.render.LabelRender
import com.google.ar.core.examples.java.ml.render.PointCloudRender
import com.google.ar.core.examples.java.ml.render.TextTextureCache
import com.google.ar.core.examples.java.request.Result
import com.google.ar.core.examples.java.request.ResultId
import com.google.ar.core.examples.java.request.ServiceBuilder
import com.google.ar.core.examples.java.request.dto.ImageData
import com.google.ar.core.examples.java.request.dto.PlantData
import com.google.ar.core.examples.java.request.dto.PlantModel
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.NotYetAvailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Collections


/**
 * Renders the HelloAR application into using our example Renderer.
 */
class AppRenderer(val activity: MainActivity) : DefaultLifecycleObserver, SampleRender.Renderer, CoroutineScope by MainScope() {
  companion object {
    val TAG = "HelloArRenderer"
  }

  lateinit var view: MainActivityView

  val builder = AlertDialog.Builder(activity)
  val builder2 = AlertDialog.Builder(activity)

  var plant_leaf_state: String? = null
  var frame: Frame? = null

  var simpleThread: Thread? = null
  var thread: Thread? = null

  var id_value: Int? = null

  val displayRotationHelper = DisplayRotationHelper(activity)
  lateinit var backgroundRenderer: BackgroundRenderer
  val pointCloudRender = PointCloudRender()
  val labelRenderer = LabelRender()

  var builderBoolean: Boolean? = null

  // 다이얼로그를 띄워주기

  val viewMatrix = FloatArray(16)
  val projectionMatrix = FloatArray(16)
  val viewProjectionMatrix = FloatArray(16)

  val arLabeledAnchors = Collections.synchronizedList(mutableListOf<ARLabeledAnchor>())
  var scanButtonWasPressed = false

  var textTexture: TextTextureCache? = TextTextureCache()
  var texture: Texture? = null

  val mlKitAnalyzer = MLKitObjectDetector(activity)
  val gcpAnalyzer = GoogleCloudVisionDetector(activity)

  var cameraImage: Image? = null
  var cameraImageBack: Image? = null

  var currentAnalyzer: ObjectDetector = gcpAnalyzer

  var plantData: PlantData? = null

  override fun onResume(owner: LifecycleOwner) {
    displayRotationHelper.onResume()
  }

  override fun onPause(owner: LifecycleOwner) {
    displayRotationHelper.onPause()
  }

  fun bindView(view: MainActivityView) {
    this.view = view

    view.scanButton.setOnClickListener {
      // frame.acquireCameraImage is dependent on an ARCore Frame, which is only available in onDrawFrame.
      // Use a boolean and check its state in onDrawFrame to interact with the camera image.
      scanButtonWasPressed = true
      view.setScanningActive(true)
      hideSnackbar()
    }

    view.useCloudMlSwitch.setOnCheckedChangeListener { _, isChecked ->
      currentAnalyzer = if (isChecked) gcpAnalyzer else mlKitAnalyzer
    }

    val gcpConfigured = gcpAnalyzer.credentials != null
    view.useCloudMlSwitch.isChecked = gcpConfigured
    view.useCloudMlSwitch.isEnabled = gcpConfigured
    currentAnalyzer = if (gcpConfigured) gcpAnalyzer else mlKitAnalyzer

//    if (!gcpConfigured) {
//      showSnackbar("Google Cloud Vision isn't configured (see README). The Cloud ML switch will be disabled.")
//    }

    view.resetButton.setOnClickListener {
      arLabeledAnchors.clear()
      view.resetButton.isEnabled = false
      hideSnackbar()
    }
  }

  override fun onSurfaceCreated(render: SampleRender) {
    backgroundRenderer = BackgroundRenderer(render).apply {
      setUseDepthVisualization(render, false)
    }
    pointCloudRender.onSurfaceCreated(render)
    labelRenderer.onSurfaceCreated(render)
  }

  override fun onSurfaceChanged(render: SampleRender?, width: Int, height: Int) {
    displayRotationHelper.onSurfaceChanged(width, height)
  }

  var objectResults: List<DetectedObjectResult>? = null

  override fun onDrawFrame(render: SampleRender) {
    val session = activity.arCoreSessionHelper.sessionCache ?: return


    session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))

    // Notify ARCore session that the view size changed so that the perspective matrix and
    // the video background can be properly adjusted.
    displayRotationHelper.updateSessionIfNeeded(session)

    val nameEdit = EditText(activity)

    builder2.setTitle("식물의 이름을 입력해주세요.")
    builder2.setView(nameEdit)
    builder2.setPositiveButton("확인",
      DialogInterface.OnClickListener { dialog, id ->
        val plant_name = nameEdit.text.toString()
        val serviceBuilder = ServiceBuilder.myApi
        val context: Context = activity
        val plantModel: PlantModel = PlantModel(plant_name, plant_leaf_state)



        serviceBuilder.savePlantInfo(plantModel).enqueue(object : retrofit2.Callback<ResultId> {
          override fun onResponse(call: Call<ResultId>, response: Response<ResultId>) {
            val responseCode = response.code()
            if (responseCode == 200) {
              Toast.makeText(activity, "Save Completed !", Toast.LENGTH_SHORT).show()

              id_value = response.body()!!.data!!

              val intent = Intent(activity.applicationContext, Plant_info::class.java)
              intent.putExtra("id", id_value)
              context.startActivity(intent)

              builder2.setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->

                })
            }
          }

          override fun onFailure(call: Call<ResultId>, t: Throwable) {
            Toast.makeText(activity, "Failed due ${t.message}", Toast.LENGTH_SHORT)
              .show()
          }
        })
        builder2.setNegativeButton("취소",
          DialogInterface.OnClickListener { dialog, id ->

          })
      })

    builder.setTitle("식물을 화분에 전송하시겠습니까?")
      .setMessage("서버로 이미지가 전송됩니다.")
      .setPositiveButton("확인",
        DialogInterface.OnClickListener { dialog, id ->

          if (cameraImageBack != null) {


            val buffer: ByteBuffer = cameraImageBack!!.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)


            val bmp = Bitmap.createBitmap(
              cameraImageBack!!.width,
              cameraImageBack!!.height,
              Bitmap.Config.ARGB_8888
            )
            val yuvToRgbConverter = YuvToRgbConverter(activity)

            yuvToRgbConverter.yuvToRgb(cameraImageBack!!, bmp)

            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val requestBody: RequestBody = RequestBody
              .create(MediaType.parse("application/octet-stream"), byteArray)

            val body = MultipartBody.Part.createFormData("image", "Test", requestBody)
            val serviceBuilder = ServiceBuilder.myApi

            serviceBuilder.uploadImage(body).enqueue(object : retrofit2.Callback<Result> {
              override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val responseCode = response.code()
                if (responseCode == 200) {
                  Toast.makeText(activity, response.body()!!.data, Toast.LENGTH_SHORT).show()
                  val imageData = ImageData(response.body()!!.data)


                  serviceBuilder.classifyImage(imageData)
                    .enqueue(object : retrofit2.Callback<Result> {
                      override fun onResponse(call: Call<Result>, response: Response<Result>) {
                        Toast.makeText(activity, response.body()!!.data, Toast.LENGTH_LONG).show()
                        plant_leaf_state = response.body()!!.data
                        builder2.show()
                      }

                      override fun onFailure(call: Call<Result>, t: Throwable) {
                      }
                    })
                  cameraImage!!.close()
                }
              }


              override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(activity, "Failed due ${t.message}", Toast.LENGTH_SHORT)
                  .show()
                cameraImage!!.close()
              }
            })
          }


        })
      .setNegativeButton("취소",
        DialogInterface.OnClickListener { dialog, id ->


        })

    frame = try {
      session.update()
    } catch (e: CameraNotAvailableException) {
      Log.e(TAG, "Camera not available during onDrawFrame", e)
      showSnackbar("Camera not available. Try restarting the app.")
      return
    }

    backgroundRenderer.updateDisplayGeometry(frame!!)
    backgroundRenderer.drawBackground(render)

    // Get camera and projection matrices.
    val camera = frame!!.camera
    camera.getViewMatrix(viewMatrix, 0)
    camera.getProjectionMatrix(projectionMatrix, 0, 0.01f, 100.0f)
    Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

    // Handle tracking failures.
    if (camera.trackingState != TrackingState.TRACKING) {
      return
    }

    // Draw point cloud.
    frame!!.acquirePointCloud().use { pointCloud ->
      pointCloudRender.drawPointCloud(render, pointCloud, viewProjectionMatrix)
    }

    // Frame.acquireCameraImage must be used on the GL thread.
    // Check if the button was pressed last frame to start processing the camera image.
    if (scanButtonWasPressed) {
      scanButtonWasPressed = false
      cameraImage = frame!!.tryAcquireCameraImage()
      cameraImageBack = cameraImage
      if (cameraImage != null) {
        // Call our ML model on an IO thread.
        launch(Dispatchers.IO) {
          val cameraId = session.cameraConfig.cameraId
          val imageRotation = displayRotationHelper.getCameraSensorToDisplayRotation(cameraId)
          objectResults = currentAnalyzer.analyze(cameraImage!!, imageRotation)


        }
      }
    }

    /** If results were completed this frame, create [Anchor]s from model results. */
    val objects = objectResults
    if (objects != null) {
      objectResults = null

      Log.i(TAG, "$currentAnalyzer got objects: $objects")
      val anchors = objects.mapNotNull { obj ->
        val (atX, atY) = obj.centerCoordinate
        val anchor = createAnchor(atX.toFloat(), atY.toFloat(), frame!!) ?: return@mapNotNull null
        Log.i(TAG, "Created anchor ${anchor.pose} from hit test")
        ARLabeledAnchor(anchor, obj.label)
      }
      arLabeledAnchors.addAll(anchors)

      view.post {
        view.resetButton.isEnabled = arLabeledAnchors.isNotEmpty()
        view.setScanningActive(false)

        if (objects.isNotEmpty() && anchors.size == objects.size) {
          cameraImageBack = cameraImage

          builder.show()
        }

        when {
          objects.isEmpty() && currentAnalyzer == mlKitAnalyzer && !mlKitAnalyzer.hasCustomModel() ->
            showSnackbar(
              "Default ML Kit classification model returned no results. " +
                      "For better classification performance, see the README to configure a custom model."
            )

          objects.isEmpty() ->
            showSnackbar("Classification model returned no results.")

          anchors.size != objects.size ->
            showSnackbar(
              "Objects were classified, but could not be attached to an anchor. " +
                      "Try moving your device around to obtain a better understanding of the environment."
            )
        }


      }
    }


    // Draw labels at their anchor position.
    for (arDetectedObject in arLabeledAnchors) {
      val anchor = arDetectedObject.anchor
      if (anchor.trackingState != TrackingState.TRACKING) continue
      labelRenderer.draw(
        render,
        viewProjectionMatrix,
        anchor.pose,
        camera.pose,
        arDetectedObject.label,
      )
    }



//    builder.show()


  }

  fun addAR(render: SampleRender, camera: Camera): Unit = runBlocking {
  GlobalScope.launch {
    launch(Dispatchers.Default) {
      texture = textTexture!!.get(render, plantData!!.plantTemp!!).also {
        Thread.sleep(3_000)
      }.also{
        if (builderBoolean != null) {
          for (arDetectedObject in arLabeledAnchors) {
            val anchor = arDetectedObject.anchor
            if (anchor.trackingState != TrackingState.TRACKING) continue
            labelRenderer.draw(
              render,
              viewProjectionMatrix,
              anchor.pose,
              camera.pose,
              label = plantData!!.plantTemp!!,
              texture = texture,
              textureExist = false,
            )
          }

          builderBoolean = null
        }
      }
    }

  }
}

  /**
   * Utility method for [Frame.acquireCameraImage] that maps [NotYetAvailableException] to `null`.
   */
  fun Frame.tryAcquireCameraImage() = try {
    acquireCameraImage()
  } catch (e: NotYetAvailableException) {
    null
  } catch (e: Throwable) {
    throw e
  }

  private fun showSnackbar(message: String): Unit =
    activity.view.snackbarHelper.showError(activity, message)

  private fun hideSnackbar() = activity.view.snackbarHelper.hide(activity)

  /**
   * Temporary arrays to prevent allocations in [createAnchor].
   */
  private val convertFloats = FloatArray(4)
  private val convertFloatsOut = FloatArray(4)

  /** Create an anchor using (x, y) coordinates in the [Coordinates2d.IMAGE_PIXELS] coordinate space. */
  fun createAnchor(xImage: Float, yImage: Float, frame: Frame): Anchor? {
    // IMAGE_PIXELS -> VIEW
    convertFloats[0] = xImage
    convertFloats[1] = yImage
    frame.transformCoordinates2d(
      Coordinates2d.IMAGE_PIXELS,
      convertFloats,
      Coordinates2d.VIEW,
      convertFloatsOut
    )

    // Conduct a hit test using the VIEW coordinates
    val hits = frame.hitTest(convertFloatsOut[0], convertFloatsOut[1])
    val result = hits.getOrNull(0) ?: return null
    return result.trackable.createAnchor(result.hitPose)
  }
}

data class ARLabeledAnchor(val anchor: Anchor, val label: String)