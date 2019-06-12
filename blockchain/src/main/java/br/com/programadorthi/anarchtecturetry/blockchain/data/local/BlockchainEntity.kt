package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "tb_blockchain")
data class BlockchainEntity(

    @PrimaryKey
    val timestamp: Long,

    val value: BigDecimal

)