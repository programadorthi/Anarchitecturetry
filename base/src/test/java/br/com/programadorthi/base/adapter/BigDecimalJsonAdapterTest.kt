package br.com.programadorthi.base.adapter

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

class BigDecimalJsonAdapterTest {

    private val moshi = Moshi.Builder()
        .add(BigDecimal::class.java, BigDecimalJsonAdapter())
        .build()

    @Test
    fun `should convert from BigDecimal 1234_5678 to String 1234_5678`() {
        val expected = "{\"value\":\"1234.5678\"}"

        val model = TestModel(
            value = BigDecimal("1234.5678")
        )
        val json = moshi.adapter(TestModel::class.java).toJson(model)

        assertThat(json).isEqualTo(expected)
    }

    @Test
    fun `should convert from String 1234_5678 to BigDecimal 1234_5678`() {
        val expected = TestModel(
            value = BigDecimal("1234.5678")
        )

        val json = "{\"value\":\"1234.5678\"}"
        val model = moshi.adapter(TestModel::class.java).fromJson(json)

        assertThat(model).isEqualTo(expected)
    }

    private data class TestModel(
        @Json(name = "value")
        val value: BigDecimal
    )
}