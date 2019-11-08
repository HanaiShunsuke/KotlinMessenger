package com.example.kotlinmessenger.trades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import kotlinx.android.synthetic.main.activity_product_exhibit.*

class ProductExhibitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_exhibit)
        val productname = name_textview_product.text.toString()
        val productprice =price_textview_product.text.toString()


    }
}
