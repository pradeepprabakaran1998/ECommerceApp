package com.example.ecommerceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import dto.Product

class MainActivity : AppCompatActivity() {
    lateinit var prodDb:DatabaseReference
    lateinit var listView:ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prodDb = FirebaseDatabase.getInstance().getReference("products")
        listView = findViewById(R.id.listView)
        prodDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for(prod in snapshot.children){
                    val product = prod.getValue(Product::class.java)
                    products.add(product!!)
                }

                val adapter = object:ArrayAdapter<Product>(
                    applicationContext,
                    R.layout.product_list_item,
                    products
                ){
                    override fun getView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {                           //Anonymous class
                        val view = convertView?: LayoutInflater.from(parent.context)  //Creating the view object
                            .inflate(R.layout.product_list_item, parent, false)
                        val itemName =
                            view.findViewById<TextView>(R.id.itemName)
                        val itemPrice =
                            view.findViewById<TextView>(R.id.itemPrice)
                        val imgView = view.findViewById<ImageView>(R.id.imgView)

                        val product = products[position]
                        itemName.text = product.name
                        itemPrice.text = product.price.toString()
                        GlideApp.with(applicationContext).load(product.imgPath).override(500, 500).optionalFitCenter().into(imgView)
                        return view
                    }
                }
                listView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
        listView.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val product = (parent?.getItemAtPosition(position) as Product)
                val prodDetailIntent = Intent(this@MainActivity,ProdDetailsActivity::class.java)
                prodDetailIntent.putExtra("prodName",product.name)
                prodDetailIntent.putExtra("prodImg",product.imgPath)
                prodDetailIntent.putExtra("prodDesc",product.description)
                prodDetailIntent.putExtra("price",product.price.toString())
                startActivity(prodDetailIntent)
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}