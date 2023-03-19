package com.wsyapp.data.repo.repo_base

import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection.*
import java.net.SocketTimeoutException

open class CallbackWrapper<T>(private val callback: HttpCallback<T>) :
    DisposableObserver<Response<T>>() {

    interface HttpCallback<T> {
        fun onCreated(t: T?)
        fun onSuccess(t: T?)
        fun onNetworkError()
        fun onNotFound()
        fun onServerError()
        fun onForbidden()
        fun onFail(t: T, message: String)

    }

    private fun onSuccess(t: Response<T>) {
        when (t.code()) {
            HTTP_OK -> callback.onSuccess(t.body())
            HTTP_CREATED -> callback.onCreated(t.body())
            HTTP_FORBIDDEN -> callback.onForbidden()
            HTTP_NOT_FOUND -> callback.onNotFound()
            HTTP_BAD_REQUEST -> callback.onServerError()
        }
    }

    override fun onComplete() {

    }

    override fun onNext(t: Response<T>) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {
            val response = e.response()
            when (response.code()) {
                HTTP_FORBIDDEN, HTTP_UNAUTHORIZED -> callback.onForbidden()
                else -> callback.onServerError()
            }
        } else if (e is SocketTimeoutException) {
            callback.onNetworkError()
        } else if (e is IOException) {
            callback.onNetworkError()
        } else {
            callback.onNetworkError()
        }
    }

    abstract class HttpHandler<T>(val callback: Repocallback<T>) :
        HttpCallback<T> {
        override fun onServerError() {
            callback.onResult(
                Resource(
                    null,
                    Resource.Status.SERVER_ERROR,
                    ""
                )
            )
        }

        override fun onFail(t: T, message: String) {
            callback.onResult(
                Resource(
                    null,
                    Resource.Status.FAIL,
                    ""
                )
            )
        }

        override fun onForbidden() {
            callback.onResult(
                Resource(
                    null,
                    Resource.Status.FORBIDDEN,
                    ""
                )
            )
        }

        override fun onNetworkError() {
            callback.onResult(
                Resource(
                    null,
                    Resource.Status.CONNECTION_ERROR,
                    ""
                )
            )
        }

        override fun onNotFound() {
            callback.onResult(
                Resource(
                    null,
                    Resource.Status.NOT_FOUND,
                    ""
                )
            )
        }
    }
}