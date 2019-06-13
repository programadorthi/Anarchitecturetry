package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException
import io.reactivex.functions.Function

/**
 * Base mapper used for inheritance to map network response in feature model
 *
 * @param Raw The result from server
 * @param Model The feature model to create from Raw
 */
abstract class BaseRemoteMapper<Raw, Model> : Function<Raw, Model> {

    @Throws(BaseException.EssentialParamMissingException::class)
    override fun apply(raw: Raw): Model {
        assertEssentialParams(raw)
        return copyValuesAfterCheckRequiredParams(raw)
    }

    /**
     * Check if the required parameters were returned from server
     *
     * @param raw The result from server
     * @throws BaseException.EssentialParamMissingException When a required parameter is missing
     */
    private fun assertEssentialParams(raw: Raw) {
        val fields = checkRequiredParamsBeforeCopyValues(raw)
        if (fields.isNotEmpty()) {
            val params = fields.joinToString(prefix = "[", postfix = "]")
            throw BaseException.EssentialParamMissingException(
                missingParam = params,
                rawObject = raw!!
            )
        }
    }

    /**
     * Check if the specific implementation parameters were return from server
     *
     * @param raw The result from server
     * @return The missing parameters list
     */
    protected abstract fun checkRequiredParamsBeforeCopyValues(raw: Raw): List<String>

    /**
     * Create a [Model] using the values in [Raw]
     *
     * @param raw The result from server
     * @return A model with the raw's values
     */
    protected abstract fun copyValuesAfterCheckRequiredParams(raw: Raw): Model
}