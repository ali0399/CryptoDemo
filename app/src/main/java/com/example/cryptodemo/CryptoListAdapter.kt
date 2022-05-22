package com.example.cryptodemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptodemo.databinding.CryptoListItemLayoutBinding
import com.example.cryptodemo.models.CryptoListResponse
import com.example.cryptodemo.models.CryptoListResponseItem

class CryptoListAdapter(val onItemClick:(CryptoListResponseItem)->Unit) :
    ListAdapter<CryptoListResponseItem, CryptoListAdapter.CoinViewHolder>(DiffCallback) {
    class CoinViewHolder(private var binding: CryptoListItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CryptoListResponseItem) {
            binding.apply {
                cryptoIconTV.text = item.symbol[0].toString()
                cryptoNameTV.text = item.baseAsset
                currentPrice.text = item.lastPrice
                openPrice.text = item.openPrice
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        return CoinViewHolder(CryptoListItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClick(getItem(position))
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CryptoListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CryptoListResponseItem,
            newItem: CryptoListResponseItem,
        ): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(
            oldItem: CryptoListResponseItem,
            newItem: CryptoListResponseItem,
        ): Boolean {
            return oldItem.lastPrice == newItem.lastPrice
        }
    }
}