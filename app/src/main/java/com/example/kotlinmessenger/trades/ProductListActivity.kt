package com.example.kotlinmessenger.trades

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_product_list.*

class ProductListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        product_exhibit_button.setOnClickListener {
            val intent = Intent(this, ProductExhibitActivity::class.java)
            startActivity(intent)
        }
    }
}
