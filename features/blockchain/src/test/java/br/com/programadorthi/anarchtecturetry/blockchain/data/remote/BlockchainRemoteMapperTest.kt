package br.com.programadorthi.anarchtecturetry.blockchain.data.remote

import br.com.programadorthi.anarchtecturetry.blockchain.domain.Blockchain
import br.com.programadorthi.base.exception.BaseException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.math.BigDecimal
import java.util.*

class BlockchainRemoteMapperTest {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Test
    fun `should throw EssentialParamMissingException when all API call response parameters is null`() {
        val raw = BlockchainResponseRaw(
            status = null,
            values = null
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[status, values] are missing in received object: $raw")

        val mapper = BlockchainRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should throw EssentialParamMissingException when API call response has status value null only`() {
        val raw = BlockchainResponseRaw(
            status = null,
            values = listOf()
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[status] are missing in received object: $raw")

        val mapper = BlockchainRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should throw EssentialParamMissingException when API call response has values value null only`() {
        val raw = BlockchainResponseRaw(
            status = "",
            values = null
        )

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[values] are missing in received object: $raw")

        val mapper = BlockchainRemoteMapper()
        mapper.apply(raw)
    }

    @Test
    fun `should mapper API call response successfully when API call response parameters are not null`() {
        val epoch = Date().time / 1000L // EPOCH time

        val blockchainRaw = BlockchainRaw(
            timestamp = epoch, // EPOCH time
            value = BigDecimal.ZERO
        )
        val raw = BlockchainResponseRaw(
            status = "",
            values = listOf(blockchainRaw)
        )
        val expected = Blockchain(
            date = Date(epoch * 1000L),
            value = blockchainRaw.value!!
        )

        val mapper = BlockchainRemoteMapper()
        val blockchain = mapper.apply(raw)

        assertThat(blockchain)
            .isNotEmpty
            .first()
            .isEqualTo(expected)
    }
}