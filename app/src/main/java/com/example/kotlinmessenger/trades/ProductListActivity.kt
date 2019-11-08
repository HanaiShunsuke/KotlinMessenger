package com.example.kotlinmessenger.trades

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.MyPageActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_product_list.*

class ProductListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        product_exhibit_button.setOnClickListener {
            val intent = Intent(this, ProductExhibitActivity::class.java)
            startActivity(intent)
        }
        move_from_product_to_mypage_button.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
        move_from_product_to_chat_button.setOnClickListener {
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
        }
    }
}
