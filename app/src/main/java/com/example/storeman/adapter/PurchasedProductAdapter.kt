package com.example.storeman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeman.R
import com.example.storeman.data.PurchasedProduct

class PurchasedProductAdapter(
    private val productsList: List<PurchasedProduct>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PurchasedProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvProductBuyPrice)
        private val tvTotalCost: TextView = itemView.findViewById(R.id.tvProductTotalCost)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)

        fun bind(product: PurchasedProduct) {
            tvProductName.text = product.name
            tvBuyPrice.text = product.buyPrice.toString()
            tvTotalCost.text = product.totalCost.toString()
            tvQuantity.text = product.quantity.toString()

            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_purchased, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }
}