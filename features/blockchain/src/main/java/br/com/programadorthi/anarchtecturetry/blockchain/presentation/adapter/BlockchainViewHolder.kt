package br.com.programadorthi.anarchtecturetry.blockchain.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.programadorthi.anarchtecturetry.blockchain.presentation.BlockchainViewData
import kotlinx.android.synthetic.main.item_market_price.view.*

class BlockchainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(viewData: BlockchainViewData) {
        itemView.apply {
            itemMarketPriceDate.text = viewData.date
            itemMarketPriceValue.text = viewData.value
        }
    }

}