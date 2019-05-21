package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainEntity
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainLocalMapper
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainLocalMapperTest {

    @Test
    fun `should mapper a empty blockchain list when there is no blockchain history in the database`() {
        val mapper = BlockchainLocalMapper()
        val result = mapper.apply(emptyList())
        assertThat(result).isEmpty()
    }

    @Test
    fun `should mapper a blockchain list when insert or update a blockchain history in the database`() {
        val entity = BlockchainEntity(
            timestamp = Date().time,
            value = BigDecimal.ONE
        )
        val expected = Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )
        val database = listOf(entity)
        val mapper = BlockchainLocalMapper()
        val result = mapper.apply(database)
        assertThat(result)
            .isNotEmpty
            .first()
            .isEqualTo(expected)
    }
}