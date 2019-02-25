package br.com.programadorthi.blockchain.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.blockchain.R
import kotlinx.android.synthetic.main.item_market_price.view.*

class BlockchainAdapter : RecyclerView.Adapter<BlockchainAdapter.BlockchainViewHolder>() {

    private var collection = emptyList<BlockchainViewData>()

    fun changeDataSet(data: List<BlockchainViewData>) {
        collection = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockchainViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val itemView = inflate.inflate(R.layout.item_market_price, parent, false)
        return BlockchainViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: BlockchainViewHolder, position: Int) {
        holder.bind(collection[position])
    }

    override fun getItemCount(): Int = collection.size

    class BlockchainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(viewData: BlockchainViewData) {
            itemView.apply {
                itemMarketPriceDate.text = viewData.date
                itemMarketPriceValue.text = viewData.value
            }
        }

    }
}