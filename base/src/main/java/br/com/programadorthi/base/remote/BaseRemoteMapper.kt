package br.com.programadorthi.base.remote

import br.com.programadorthi.base.exception.BaseException

/**
 * Base mapper used for inheritance to map network response in feature model
 *
 * @param Raw The data from server
 * @param Model The feature model to create from Raw
 */
abstract class BaseRemoteMapper<Raw, Model> {

    @Throws(BaseException.EssentialParamMissingException::class)
    fun apply(raw: Raw): Model {
        assertEssentialParams(raw)
        return copyValues(raw)
    }

    /**
     * Check if the required parameters were returned from server
     *
     * @param raw The data from server
     * @throws BaseException.EssentialParamMissingException When a required parameter is missing
     */
    private fun assertEssentialParams(raw: Raw) {
        val fields = checkParams(raw)
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
     * @param raw The data from server
     * @return A missing parameters list or empty when is all ok
     */
    protected abstract fun checkParams(raw: Raw): List<String>

    /**
     * Create a [Model] using the values in [Raw]
     *
     * @param raw The data from server
     * @return A model with the raw's values
     */
    protected abstract fun copyValues(raw: Raw): Model
}