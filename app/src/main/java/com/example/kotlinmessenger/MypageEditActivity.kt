package com.example.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mypage_edit.*
import java.util.*

class MypageEditActivity : AppCompatActivity() {
    companion object{
        var currentUser: User? = null
        val TAG = "LatestMessages"
        var username = ""
        var profileImageUrl = ""
        var whatproduct = ""
        var placename = ""
        var url =""

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mypage_edit)


        //val button=findViewById<Button>(R.id.button)

        performUser()

        mypageedit_imageview.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        button.setOnClickListener{

            compareuserdata()
            //uploadImageToFirebaseStorage()

//            val editText = findViewById<EditText>(R.id.editText)
//            val editText2 = findViewById<EditText>(R.id.editText2)
//
//            val message = editText.run { text.toString() }
//            val message2 = editText2.run { text.toString() }
//
//            val intent = Intent(this,MyPageActivity::class.java)
//
//            intent.putExtra("message",message)
//            intent.putExtra("message2",message2)
//
//            startActivity(intent)

        }



    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //Log.d(TAG,"Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            mypageedit_imageview.setImageBitmap(bitmap)

            mypageedit_imageview.alpha = 0f
            // val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun compareuserdata() {
        val id = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$id")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        if (currentUser?.username != username_edittext_mypageEdit.text.toString() && username_edittext_mypageEdit.text.toString() != ""){
             username = username_edittext_mypageEdit.text.toString()
        }else{
            username = currentUser!!.username
        }
//        if (currentUser?.profileImageUrl != mypageedit_imageview.toString()){
//             profileImageUrl= username_edittext_mypageEdit.toString()
//        }else{
//            profileImageUrl= currentUser!!.profileImageUrl
//        }
        if(currentUser?.placename != placename_edittext_mypageEdit.text.toString() && placename_edittext_mypageEdit.text.toString() != ""){
            placename = placename_edittext_mypageEdit.text.toString()
        }else{
            placename = currentUser!!.placename
        }
        if(currentUser?.whatproduct != whatproduct_edittext_mypageEdit.text.toString() && whatproduct_edittext_mypageEdit.text.toString() != ""){
            whatproduct = whatproduct_edittext_mypageEdit.text.toString()
        }else{
            whatproduct = currentUser!!.whatproduct
        }



        uploadImageToFirebaseStorage(username, placename, whatproduct)
        //Log.d("debugdebug","$placename,$whatproduct")
    }

    private fun uploadImageToFirebaseStorage(username:String,placename:String,whatproduct:String){
        if(selectedPhotoUri == null){
            //Log.d("RegisterActivity","debug")
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG,"Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    url = it.toString()
                    Log.d(TAG,"File Location: $it")


                }
            }
            .addOnFailureListener{

            }
       if (url != currentUser?.profileImageUrl && url != ""){
             profileImageUrl = url
        }else{
            profileImageUrl= currentUser!!.profileImageUrl
        }
       // Log.d("debugdebug2","$placename,$whatproduct")
        edituser(username,placename,whatproduct, profileImageUrl)
    }
    private fun edituser(username:String,placename:String,whatproduct:String,profileImageUrl: String){
        val id = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$id")

        //Log.d("debugdebug3","$placename,$whatproduct")
        val user = User(
            id.toString(),
            username,
            profileImageUrl,
            placename,
            whatproduct

        )


        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this,MyPageActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this,"保存に失敗しました",Toast.LENGTH_SHORT).show()
            }

    }

    private fun performUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                username_edittext_mypageEdit.hint= currentUser?.username
                placename_edittext_mypageEdit.hint = currentUser?.placename
                whatproduct_edittext_mypageEdit.hint = currentUser?.whatproduct
                Picasso.get().load(currentUser?.profileImageUrl).into(mypageedit_imageview)
                Log.d("LatestMessages","Current user ${currentUser?.profileImageUrl}")


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



    }


}
