package br.com.programadorthi.blockchain.data.remote

import br.com.programadorthi.base.exception.BaseException
import br.com.programadorthi.blockchain.domain.Blockchain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.math.BigDecimal
import java.util.*

class BlockchainCurrentValueRemoteMapperTest {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun `should throw EssentialParamMissingException when all API call response parameters is null`() {
        val raw = BlockchainCurrentValueRaw(
            timestamp = null,
            value = null
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[timestamp, market_price_usd] are missing in received object: $raw")

        val mapper = BlockchainCurrentValueRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should throw EssentialParamMissingException when API call response has timestamp value null only`() {
        val raw = BlockchainCurrentValueRaw(
            timestamp = null,
            value = BigDecimal.ZERO
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[timestamp] are missing in received object: $raw")

        val mapper = BlockchainCurrentValueRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should throw EssentialParamMissingException when API call response has market_price_usd value null only`() {
        val raw = BlockchainCurrentValueRaw(
            timestamp = Date().time,
            value = null
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[market_price_usd] are missing in received object: $raw")

        val mapper = BlockchainCurrentValueRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should mapper API call response successfully when API call response parameters are not null`() {
        val raw = BlockchainCurrentValueRaw(
            timestamp = Date().time,
            value = BigDecimal.ZERO
        )
        val expected = Blockchain(
            date = Date(raw.timestamp!!),
            value = raw.value!!
        )

        val mapper = BlockchainCurrentValueRemoteMapper()
        val blockchain = mapper.apply(raw)

        assertThat(blockchain).isEqualTo(expected)
    }
}