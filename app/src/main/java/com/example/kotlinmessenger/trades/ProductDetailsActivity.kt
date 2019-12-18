package com.example.kotlinmessenger.trades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : AppCompatActivity() {
    var toProduct:Product ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        toProduct = intent.getParcelableExtra(ProductListActivity.USER_KEY)

        productprice_details_textview.text=toProduct?.productprice.toString()
        productname_details_textview.text = toProduct?.productname.toString()
        Picasso.get().load(toProduct?.profileImageUrl).into(productphoto_details_imageview)
        productcomment_details_textview.text = toProduct?.productcomment.toString()

        //listenForProduct()


    }

//    private fun listenForProduct(){
//        val pid = toProduct?.pid
//        val uid = toProduct?.uid
//        val ref = FirebaseDatabase.getInstance().getReference("product/$uid/$pid")
//
//
//    }


}


