package com.example.assessmenttest.utils

import retrofit2.Response

sealed class AppResult<out T> {

    data class Success<out T>(val successData: T) : AppResult<T>()
    class Error(
        val exception: java.lang.Exception,
        val message: String = exception.localizedMessage
    ) : AppResult<Nothing>()
}

fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
//    val error = ApiErrorUtils.parseError(resp)
//    return AppResult.Error(Exception(error.message))

    return AppResult.Error(Exception(resp.errorBody().toString()))
}

fun handleNetworkError(): AppResult.Error {
    return AppResult.Error(
        Exception("Network Not Available nor data "),
        message = "Network not available"
    )
}

fun <T : Any> handleSuccess(response: Response<T>): AppResult<T> {
    response.body()?.let {
        return AppResult.Success(it)
    } ?: return handleApiError(response)
}