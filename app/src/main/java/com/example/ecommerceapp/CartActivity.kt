package com.example.ecommerceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dto.Product

class CartActivity : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<Product>
    lateinit var products: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        products = FirebaseFirestore.getInstance().collection("products")
        val paymentBtn = findViewById<Button>(R.id.payBtn)
        adapter = object : ArrayAdapter<Product>(applicationContext, R.layout.cart_view_item) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(parent.context)  //Creating the view object
                    .inflate(R.layout.cart_view_item, parent, false)
                val itemName = view.findViewById<TextView>(R.id.cartItem)
                val product = getItem(position)
                itemName.text = product?.name
                return view
            }
        }
       listView = findViewById(R.id.cartView)
       listView.adapter = adapter

        products.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val priceString = document.getString("price")
                val price = priceString?.toDouble() ?: 0.0
                val product = Product(document.getString("name"), price)
                adapter.add(product)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,"Error Fetching Products", Toast.LENGTH_SHORT).show()
        }
        paymentBtn.setOnClickListener { ele ->
            Toast.makeText(this,"This app is still a work in progress and this feature is not available yet", Toast.LENGTH_SHORT).show()
        }
        }

}