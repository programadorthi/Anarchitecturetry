package br.com.programadorthi.blockchain.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.blockchain.R
import br.com.programadorthi.blockchain.domain.Blockchain

class BlockchainAdapter : RecyclerView.Adapter<BlockchainAdapter.BlockchainViewHolder>() {

    private var collection: List<Blockchain> = emptyList()

    fun changeDataSet(data: List<Blockchain>) {
        collection = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockchainViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val itemView = inflate.inflate(R.layout.item_market_price, parent, false)
        return BlockchainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BlockchainViewHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount(): Int = collection.size

    class BlockchainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(blockchain: Blockchain) {
            //itemView.itemMarketPriceDate.text = DATE_FORMAT.format(blockchain.date)
            //itemView.itemMarketPriceValue.text = NUMBER_FORMAT.format(blockchain.value)
        }
    }
}