package br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.anarchtecturetry.R
import br.com.programadorthi.anarchtecturetry.feature.blockchain.presentation.BlockchainViewData

class BlockchainAdapter : RecyclerView.Adapter<BlockchainViewHolder>() {

    private var dataSet = emptyList<BlockchainViewData>()

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockchainViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val itemView = inflate.inflate(R.layout.item_market_price, parent, false)
        return BlockchainViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: BlockchainViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    fun changeDataSet(data: List<BlockchainViewData>) {
        dataSet = data
        notifyDataSetChanged()
    }

}