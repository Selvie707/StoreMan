package com.example.storeman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.storeman.databinding.ActivityAddItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var productName: String
    private var productStock: Int = 0
    private var productGoalStock: Int = 0
    private var productPrice: Int = 0
    private var productCapital: Int = 0
    private var productSold: Int = 0
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val usersCollection = db.collection("product")

        binding.btnSubmit.setOnClickListener {
            productName = binding.etProductName.text.toString()
            productStock = binding.etProductStock.text.toString().toInt()
            productGoalStock = binding.etProductGoal.text.toString().toInt()
            productPrice = binding.etProductPrice.text.toString().toInt()
            productSold = binding.etProductSold.text.toString().toInt()

            val userMap = hashMapOf(
                "name" to productName,
                "currentStock" to productStock,
                "goalStock" to productGoalStock,
                "buyPrice" to productPrice,
                "soldPerMonth" to productSold
            )

            usersCollection.document(productName).set(userMap).addOnSuccessListener {
                Toast.makeText(this, "Successfully Added!!!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                finish()
            }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "There's something wrong", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity", "Error: ${e.message}")
                }
        }
    }
}