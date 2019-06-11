package br.com.programadorthi.anarchtecturetry.blockchain.data.local

import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainCurrentValueEntity
import br.com.programadorthi.anarchtecturetry.feature.blockchain.data.local.BlockchainCurrentValueLocalMapper
import br.com.programadorthi.anarchtecturetry.feature.blockchain.domain.Blockchain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class BlockchainCurrentValueLocalMapperTest {

    @Test
    fun `should mapper a empty blockchain when there is no current blockchain value in the database`() {
        val mapper = BlockchainCurrentValueLocalMapper()
        val result = mapper.apply(emptyList())
        assertThat(result).isEqualTo(Blockchain.EMPTY)
    }

    @Test
    fun `should mapper the current blockchain value when insert or update the current value in the database`() {
        val entity = BlockchainCurrentValueEntity(
            timestamp = Date().time,
            value = BigDecimal.ONE
        )
        val expected = Blockchain(
            date = Date(entity.timestamp),
            value = entity.value
        )
        val database = listOf(entity)
        val mapper = BlockchainCurrentValueLocalMapper()
        val result = mapper.apply(database)
        assertThat(result).isEqualTo(expected)
    }
}