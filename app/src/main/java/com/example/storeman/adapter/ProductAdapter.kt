package com.example.storeman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeman.R
import com.example.storeman.data.Product

class ProductAdapter(
    private val productsList: List<Product>,
    private val onItemClick: (Int) -> Unit

) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvProductStock: TextView = itemView.findViewById(R.id.tvProductStock)
        private val tvGoalStock: TextView = itemView.findViewById(R.id.tvProductGoal)
        private val tvBuyPrice: TextView = itemView.findViewById(R.id.tvProductBuy)
        private val tvSoldPerMonth: TextView = itemView.findViewById(R.id.tvProductSoldPerMonth)

        fun bind(product: Product) {
            tvProductName.text = product.name
            tvProductStock.text = product.currentStock.toString()
            tvGoalStock.text = product.goalStock.toString()
            tvBuyPrice.text = product.buyPrice.toString()
            tvSoldPerMonth.text = product.soldPerMonth.toString()

            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }
}