package com.example.kotlinmessenger.trades

//import com.example.kotlinmessenger.messages.UserItem
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.MyPageActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.messages.LatestMessagesActivity
import com.example.kotlinmessenger.messages.NewMessageActivity
import com.example.kotlinmessenger.models.Product
import com.example.kotlinmessenger.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_product_exhibit.view.*
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.product_list_row.view.*

class ProductListActivity : AppCompatActivity() {

    companion object{

        val TAG = "ProductListActivity"
    }
    //val adapter = GroupAdapter<GroupieViewHolder>()

    //var CurrentProduct: Product?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        supportActionBar?.title = "取り引き画面"



        fetchProduct()

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



    private fun fetchProduct(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/product/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach{
                    Log.d("NewProduct",it.toString())
                    val product = it.getValue(Product::class.java)
                    Log.d("NewProductnew","currentProduct: ${product?.profileImageUrl}")

                    if(product != null){
                        adapter.add(ProductItem(product))

                    }
                }

                recyclerview_list.adapter = adapter

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}

class ProductItem(val product: Product): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.productname_textview_list.text = product.productname

        Picasso.get().load(product.profileImageUrl).into(viewHolder.itemView.selectphoto_imageview_product)
    }
    override fun getLayout(): Int {
        return R.layout.product_list_row
    }
}




