package com.google.ar.core.examples.java.ml

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Resis : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resis)

        val buttonResist = findViewById<Button>(R.id.resist)
        val editTextId = findViewById<EditText>(R.id.editTextId)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPassword2 = findViewById<EditText>(R.id.editTextPassword2)

        buttonResist.setOnClickListener {
            val id = editTextId.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val password2 = editTextPassword2.text.toString()

            if (password == password2) {
                val intent = Intent()
                intent.putExtra("ID", id)
                intent.putExtra("Email", email)
                intent.putExtra("Password", password)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
