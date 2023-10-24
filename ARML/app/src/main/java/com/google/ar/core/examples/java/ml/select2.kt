import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.examples.java.ml.Home
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.ar.core.examples.java.ml.R
import com.google.ar.core.examples.java.request.ServiceBuilder
import com.google.ar.core.examples.java.request.YourResponseModel
import com.google.ar.core.examples.java.request.YourRequestModel

class select2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select2)

        val button1 = findViewById<Button>(R.id.button5)
        val button2 = findViewById<Button>(R.id.button6)
        val button3 = findViewById<Button>(R.id.button7)
        val button4 = findViewById<Button>(R.id.button8)

        data class YourRequestModel(val text: String)

        button1.setOnClickListener {
            val buttonText = button1.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, select3::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val buttonText = button2.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, select3::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val buttonText = button3.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, select3::class.java)
            startActivity(intent)
        }

        button4.setOnClickListener {
            val buttonText = button4.text.toString()
            sendDataToServer(buttonText)
            val intent = Intent(this, select3::class.java)
            startActivity(intent)
        }

    }

    private fun sendDataToServer(text: String) {
        val serviceBuilder = ServiceBuilder.myApi
.
        val requestData = YourRequestModel

        val call = serviceBuilder.postDataToServer(requestData)

        call.enqueue(object : Callback<YourResponseModel> {
            override fun onFailure(call: Call<YourResponseModel>, t: Throwable) {
                Toast.makeText(this@select2, "데이터 전송 중 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
