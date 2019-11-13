package com.example.kotlinmessenger.trades

//import com.example.kotlinmessenger.messages.UserItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.MyPageActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.ChatLogActivity
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.example.kotlinmessenger.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.product_list_row.view.*

class ProductListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)



        fetchUsers()

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

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers(){
        val uid = FirebaseAuth.getInstance().uid

       // val product = intent.getParcelableExtra<Product>(USER_KEY)

        val ref = FirebaseDatabase.getInstance().getReference("/users/product/$uid")

        //Log.d("ProductListActivity","debug")
        //Log.d("ProductListActivity","ref")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{
                   // Log.d("ProductList",it.toString())
                    val product = it.getValue(Product::class.java)
                    Log.d("ProductListActivity","")
                    if(product != null){
                        adapter.add(ProductItem(product))
                    }
                }




                adapter.setOnItemClickListener { item, view ->

                    val productItem = item as ProductItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    // intent.putExtra(USER_KEY,userItem.user.username)
                    intent.putExtra(USER_KEY,productItem.product)
                    startActivity(intent)

                    finish()
                }

                recyclerview_list.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError){

            }
        })
    }
}



class ProductItem(val product: Product): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.productname_textview_list.text = product.productname
        viewHolder.itemView.productprice_textview_list.text = product.productprice

        Picasso.get().load(product.profileImageUrl).into(viewHolder.itemView.productphoto_imageview_list)
    }
    override fun getLayout(): Int {
        return R.layout.product_list_row
    }
}
