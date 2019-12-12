package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MypageEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_edit)

        val button=findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val pref= PreferenceManager.getDefaultSharedPreferences(this)
            val editText = findViewById<EditText>(R.id.editText)
            val editText2 = findViewById<EditText>(R.id.editText2)

            val message = editText.run { text.toString() }
            val message2 = editText2.run { text.toString() }

            val intent = Intent(this, MyPageActivity::class.java)

            intent.putExtra("message", message)
            intent.putExtra("message2", message2)


            startActivity(intent)
        }




    }






}
