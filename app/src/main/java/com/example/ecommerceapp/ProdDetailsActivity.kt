package com.example.ecommerceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class ProdDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prod_details)

        var prodName = findViewById<TextView>(R.id.prodTitle)
        var prodDesc = findViewById<TextView>(R.id.prodDesc)
        var price = findViewById<TextView>(R.id.price)
        var prodImg = findViewById<ImageView>(R.id.prodImgView)
        var cartBtn = findViewById<Button>(R.id.cartBtn)
        var qty = findViewById<TextView>(R.id.qty)

        var imgPath = intent.getStringExtra("prodImg")
        GlideApp.with(this).load(imgPath).override(1000, 1000).into(prodImg)

        prodName.text = intent.getStringExtra("prodName")
        prodDesc.text = intent.getStringExtra("prodDesc")
        price.text = intent.getStringExtra("price")

        cartBtn.setOnClickListener {
            FirebaseApp.initializeApp(this)
            val db = FirebaseFirestore.getInstance()
            val prodItem = hashMapOf(
                "name" to prodName.text.toString(),
                "price" to price.text.toString(),
                "qty" to qty.text.toString()
            )

            //store products in firebase firestore db
            db.collection("products").document(prodName.text.toString()).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // The product already exists in firestore database
                        Toast.makeText(this, "Product already exists in Firestore", Toast.LENGTH_SHORT).show()
                    } else {
                        // The product does not exist in firestore database
                        db.collection("products").document(prodName.text.toString())
                            .set(prodItem)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Product added to Firestore", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener{
                                Toast.makeText(this, "Error adding product to Firestore", Toast.LENGTH_SHORT).show()
                            }
                    }
                }.addOnFailureListener { exception ->
                    // Handle exceptions that occur while checking if the document exists
                    Toast.makeText(this, "Error checking if product exists in Firestore: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.prod_details_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_cart -> {
                val intent = Intent(this,CartActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}