package com.google.ar.core.examples.java.ml

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    companion object {
        const val RESIS_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val buttonClick = findViewById<Button>(R.id.resis)
        buttonClick.setOnClickListener {
            val intent = Intent(this, Resis::class.java)
            startActivityForResult(intent, RESIS_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESIS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val id = data?.getStringExtra("ID")
                val email = data?.getStringExtra("Email")
                val password = data?.getStringExtra("Password")

                val validId = "test341"
                val validEmail = "test@gmail.com"
                val validPassword = "123456"

                val buttonClick = findViewById<Button>(R.id.login)
                buttonClick.setOnClickListener {
                    if ((id!! == validId || email!! == validEmail) && password!! == validPassword) {
                        val intent = Intent(this, Home::class.java)
                        intent.putExtra("ID", id)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
