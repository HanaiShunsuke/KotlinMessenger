package com.example.kotlinmessenger.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

     //6:10
    companion object{
         val TAG = "RegisterActivity"
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "登録画面"
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
           performRegister()

        }

        already_have_account_textView.setOnClickListener {
            Log.d(TAG,"Try to show login activity" )

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener{
            Log.d(TAG,"Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d(TAG,"Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha = 0f
           // val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){
        val email = email_edittext_register.text.toString()

        val password = password_edittext_register.text.toString()
        val regex_email = Regex(email)
        val regex_password = Regex(password)

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG,"Email is:" + email)
        Log.d(TAG,"Password: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d(TAG,"Successfully created user with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d(TAG,"Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImageToFirebaseStorage(){
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
                    it.toString()
                    Log.d(TAG,"File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_register.text.toString(),
            profileImageUrl
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG,"Finally we savaed the user to Firebase Database")

                val intent = Intent(this,
                    LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG,"Failed to set value to database: ${it.message}")
            }
    }
}

class User(val uid:String,val username: String,val profileImageUrl: String){
    constructor() : this("","","")
}


//10/11 02終了
//次は3から　https://www.youtube.com/watch?v=86dkYGeXMaU&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&index=3


//10/13 03終了
//次は4から　https://www.youtube.com/watch?v=SuRiwVF5bzs&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&index=4


//10/14 04終了
//次は5から https://www.youtube.com/watch?v=-HB__yZqha0&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&index=5

//10/15 05終了
//次は6

//10/16 06終了
//次は7

//10/17 07終了
//次は8

//10/28 08終了
//次は9