package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class BaseRemoteMapperTest {

    @Rule
    @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    private val mapper = FakeMapper()

    @Test
    fun `should throw an EssentialParamMissingException when there are missing required params in the API call JSON response`() {
        val raw = FakeResponseRaw(rawValue = null)

        expectedException.expect(BaseException.EssentialParamMissingException::class.java)
        expectedException.expectMessage("[rawValue] are missing in received object: $raw")

        mapper.apply(raw)
    }

    @Test
    fun `should mapper to model when API call JSON response is valid`() {
        val raw = FakeResponseRaw(rawValue = "sample value value")
        val expected = FakeModel(value = raw.rawValue!!)

        val result = mapper.apply(raw)

        assertThat(result).isEqualTo(expected)
    }

    private class FakeMapper : BaseRemoteMapper<FakeResponseRaw, FakeModel>() {
        override fun checkParams(raw: FakeResponseRaw): List<String> {
            val missingFields = mutableListOf<String>()
            if (raw.rawValue == null) {
                missingFields.add("rawValue")
            }
            return missingFields
        }

        override fun copyValues(raw: FakeResponseRaw): FakeModel {
            // sample value was checked in the checkParams method
            return FakeModel(value = raw.rawValue!!)
        }
    }

    private data class FakeResponseRaw(val rawValue: String?)

    private data class FakeModel(val value: String)
}