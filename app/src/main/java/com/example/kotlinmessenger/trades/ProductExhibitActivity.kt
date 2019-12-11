package com.example.kotlinmessenger.trades

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_product_exhibit.*
import java.util.*

class ProductExhibitActivity : AppCompatActivity() {
    companion object{
        val TAG = "ProductExhibitActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_exhibit)
        val productname = name_textview_product.text.toString()
        val productprice =price_textview_product.text.toString()
        supportActionBar?.title = "出品画面"

        selectphoto_button_product.setOnClickListener{
            //Log.d(TAG,"Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
        register_button_product.setOnClickListener {
            performRegisterProduct()
        }


    }

    var selectedPhotoUri: Uri? = null

    private fun performRegisterProduct(){
        val productname = name_textview_product.text.toString()
        val productprice = price_textview_product.text.toString()

        uploadImageToFirebaseStorage()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //Log.d(TAG,"Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectphoto_imageview_product.setImageBitmap(bitmap)


            selectphoto_button_product.alpha = 0f

            // val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null){
            //Log.d("RegisterActivity","debug")
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/product/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                //Log.d(RegisterActivity.TAG,"Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    //Log.d(RegisterActivity.TAG,"File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/product/$uid/").push()
        val pid = ref.key!!

        val product = Product(
            uid,
            pid,
            name_textview_product.text.toString(),
            price_textview_product.text.toString(),
            profileImageUrl
        )
        ref.setValue(product)
            .addOnSuccessListener {
                Log.d(TAG,"Finally we savaed the user to Firebase Database")

                val intent = Intent(this,
                    ProductListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG,"Failed to set value to database: ${it.message}")
            }
    }
}

