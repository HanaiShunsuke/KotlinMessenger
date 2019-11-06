package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.example.kotlinmessenger.trades.ProductListActivity
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        move_message_button.setOnClickListener {
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
        }
        move_shop_button.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }
    }
}
