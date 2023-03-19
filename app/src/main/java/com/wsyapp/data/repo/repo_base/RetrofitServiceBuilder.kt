package com.wsyapp.data.repo.repo_base

import android.util.Log
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wsyapp.data.repo.ApiService
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

const val MEDIA_TYPE_MULTIPART_FORM_DATA_VALUE = "multipart/form-data"

object RetrofitServiceBuilder {
    private var retrofit: Retrofit? = null

    init {
        buildRetrofit()
    }

    private fun buildGson(): Gson {
        return GsonBuilder().create()
    }

    private fun buildLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor() {
            Log.e("RetrofitServiceBuilder", it)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }


    private fun buildHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val okHttp =
            OkHttpClient().newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor)

        if (ENABLE_LOG) okHttp.addInterceptor(httpLoggingInterceptor)

        return okHttp
            .build()
    }

    private fun buildRetrofit(): Retrofit {
        val client =
            buildHttpClient(
                buildLoggingInterceptor(),
                RequestInterceptor
            )

        retrofit = Retrofit.Builder().baseUrl(
            SERVER_URL
        )
            .addConverterFactory(GsonConverterFactory.create(buildGson())).client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        return retrofit!!
    }

    fun createPartFromString(descriptionString: String?): RequestBody? {
        return RequestBody.create(
            MediaType.parse(MEDIA_TYPE_MULTIPART_FORM_DATA_VALUE),
            descriptionString ?: ""
        )
    }

    fun createFilePart(
        variableName: String?,
        filePath: String?,
        mediaType: MediaType?
    ): MultipartBody.Part? {
        val file = File(filePath)
        // create RequestBody instance from file
        val requestFile = RequestBody.create(mediaType, file)
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(variableName, file.name, requestFile)
    }

    fun createMultipartRequest(requestValuePairsMap: HashMap<String?, String?>): HashMap<String, RequestBody>? {
        val requestMap =
            HashMap<String, RequestBody>()
        val iterator: Iterator<*> = requestValuePairsMap.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next() as String
            val value = requestValuePairsMap[key]
            //  requestMap[key] = createPartFromString(value)
        }
        return requestMap
    }

    fun getApiService(): ApiService {
        return retrofit!!.create(ApiService::class.java)
    }

    fun getPhoneAuthProviderFirebaseService(): PhoneAuthProvider {
        return PhoneAuthProvider.getInstance()
    }
}