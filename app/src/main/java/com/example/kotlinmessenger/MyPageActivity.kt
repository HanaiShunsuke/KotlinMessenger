package com.example.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.example.kotlinmessenger.models.User
import com.example.kotlinmessenger.trades.ProductListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_page.*


class MyPageActivity : AppCompatActivity() {
    companion object{
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        supportActionBar?.title = "マイページ画面"
//    //作物を反映
//        val message=intent.getStringExtra("message")
//        val textView =findViewById<TextView>(R.id.produce_mypage_text)
//        textView.text=message
//
//        val data: SharedPreferences =
//            getSharedPreferences(R.id.produce_mypage_text.toString(), Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = data.edit()
//        editor.putInt(R.id.produce_mypage_text.toString(), 1)
//        editor.apply()


//    //地域を反映
//        val message2=intent.getStringExtra("message2")
//        val textView2 =findViewById<TextView>(R.id.place_mypage_text)
//        textView2.text=message2

        move_message_button.setOnClickListener {
            val intent = Intent(this, LatestMessagesActivity::class.java)
            startActivity(intent)
        }
        move_shop_button.setOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }
        edit_mypage_button.setOnClickListener{
            val intent =Intent(this,MypageEditActivity::class.java)
            startActivity(intent)
        }
        fetchCurrentUser()




    }
    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                username_mypage_text.text= currentUser?.username
                place_mypage_text.text = currentUser?.placename
                produce_mypage_text.text = currentUser?.whatproduct
                Picasso.get().load(currentUser?.profileImageUrl).into(mypage_imageview)
                Log.d("LatestMessages","Current user ${currentUser?.profileImageUrl}")

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}



