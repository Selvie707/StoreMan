package com.example.storeman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeman.adapter.ProductAdapter
import com.example.storeman.data.Product
import com.example.storeman.databinding.ActivityAddItemBinding
import com.example.storeman.databinding.ActivityProductListBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var rvProducts: RecyclerView
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvProducts = binding.rvItem

        rvProducts.layoutManager = LinearLayoutManager(this)

        binding.fabAddItem.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        binding.fabItemRecommendation.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        ShowProductList()
    }

    private fun ShowProductList() {
        val db = FirebaseFirestore.getInstance()
        db.collection("product")
            .get()
            .addOnSuccessListener { querySnapshot ->
                productList.clear()
                for (document in querySnapshot) {
                    val material = document.toObject(Product::class.java)
                    productList.add(material)
                }
                rvProducts.adapter = ProductAdapter(productList) { position ->
                    Toast.makeText(this, "Product clicked at position $position", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load materials: $exception", Toast.LENGTH_SHORT).show()
            }
    }
}