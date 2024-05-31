package com.example.storeman.data

import android.util.Log

data class Product(
    val name: String = "",
    var currentStock: Int = 0,
    val buyPrice: Int = 0,
    val soldPerMonth: Int = 0,
    val goalStock: Int = 0
) {
//    val goalStock: Int
//        get() = soldPerDay * 15 // Lead time = 15 days

    var density: Double = 0.0
        private set

    fun updateDensity() {
        density = if (goalStock > 0) {
            ((goalStock - currentStock).toDouble() / goalStock) * soldPerMonth
        } else {
            0.0
        }
    }

    fun addStock(quantity: Int) {
        currentStock += quantity
        updateDensity()
    }
}