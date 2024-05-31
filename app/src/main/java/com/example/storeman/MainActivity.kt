package com.example.storeman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeman.adapter.ProductAdapter
import com.example.storeman.adapter.PurchasedProductAdapter
import com.example.storeman.data.Product
import com.example.storeman.data.PurchasedProduct
import com.example.storeman.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var rvProducts: RecyclerView
    private val productList = mutableListOf<Product>()
    private var currentBudget = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvProducts = binding.rvRestockItem
        rvProducts.layoutManager = LinearLayoutManager(this)

        showProductList()

        binding.btnGenerate.setOnClickListener {
            binding.rvRestockItem.visibility = View.VISIBLE
            binding.tvTotal.visibility = View.VISIBLE
            binding.tlTitle.visibility = View.VISIBLE
            val budgetText = binding.etInsertBudget.text.toString()
            if (budgetText.isNotEmpty()) {
                val maxBudget = budgetText.toInt()
                val optimizedProducts = optimizeStock(productList, maxBudget)
                showOptimizedProducts(optimizedProducts)
                displayTotalSpent()
            } else {
                Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabAddItem.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        binding.fabListItem.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
            finish()
        }
    }

    private fun showProductList() {
        db.collection("product")
            .get()
            .addOnSuccessListener { querySnapshot ->
                productList.clear()
                for (document in querySnapshot) {
                    val product = document.toObject(Product::class.java)
                    product.updateDensity() // Panggil updateDensity setelah inisialisasi
                    productList.add(product)
                }
                rvProducts.adapter = ProductAdapter(productList) { position ->
                    Toast.makeText(this, "Product clicked at position $position", Toast.LENGTH_SHORT).show()
                }
                // Log produk yang diambil dari Firestore
                for (product in productList) {
                    Log.d("ProductList", "Product: ${product.name}, CurrentStock: ${product.currentStock}, GoalStock: ${product.goalStock}, Density: ${product.density}")
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load products: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun optimizeStock(products: List<Product>, maxBudget: Int): List<PurchasedProduct> {
        val purchasedProducts = mutableListOf<PurchasedProduct>()
        val productPurchaseCount = mutableMapOf<String, Int>()
        currentBudget = 0

        while (currentBudget < maxBudget) {
            val sortedProducts = products.sortedByDescending { it.density }
            Log.d("StockOptimization", "Comparing product densities")
            for (product in sortedProducts) {
                Log.d("StockOptimization", "Product: ${product.name}, Density: ${product.density}")
            }

            val selectedProduct = sortedProducts.firstOrNull() ?: break

            if (currentBudget + selectedProduct.buyPrice > maxBudget || selectedProduct.currentStock >= selectedProduct.goalStock) {
                break
            }

            selectedProduct.addStock(1)
            productPurchaseCount[selectedProduct.name] = productPurchaseCount.getOrDefault(selectedProduct.name, 0) + 1
            currentBudget += selectedProduct.buyPrice

            // Update density of the selected product
            selectedProduct.updateDensity()
        }

        for ((name, quantity) in productPurchaseCount) {
            val product = products.find { it.name == name }!!
            purchasedProducts.add(PurchasedProduct(name, product.buyPrice, product.buyPrice * quantity, quantity))
        }

        Log.d("StockOptimization", "Jumlah produk yang dibeli:")
        for (product in purchasedProducts) {
            Log.d("StockOptimization", "Product: ${product.name}, Quantity: ${product.quantity}")
        }

        // Reset currentStock produk yang dibeli
        for (product in purchasedProducts) {
            val productToUpdate = products.find { it.name == product.name }
            productToUpdate?.let {
                it.currentStock -= product.quantity
                it.updateDensity() // Update density setelah mengurangi currentStock
            }
        }

        return purchasedProducts
    }

    private fun showOptimizedProducts(products: List<PurchasedProduct>) {
        val adapter = PurchasedProductAdapter(products) { position ->
            Toast.makeText(this, "Optimized Product clicked at position $position", Toast.LENGTH_SHORT).show()
        }
        rvProducts.adapter = adapter
    }

    private fun displayTotalSpent() {
        binding.tvTotal.text = "Total Spent: $currentBudget"
    }
}