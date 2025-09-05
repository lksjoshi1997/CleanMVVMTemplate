package com.eva.checkin.data.remote

import com.example.mvvmcleantemplate.data.remote.ApiInterface
import com.example.mvvmcleantemplate.data.remote.AuthInterceptor
import com.example.mvvmcleantemplate.data.remote.HostSelectionInterceptor
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private var BASE_URL: String = ""
    private val hostInterceptor = HostSelectionInterceptor()
    private var tokenProvider: (() -> String?) = { null }

    // Logging Interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp Client
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(hostInterceptor) // for dynamic host
        .addInterceptor(AuthInterceptor { tokenProvider() }) // for dynamic auth
        .addInterceptor(loggingInterceptor) // for logging request/response
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance (baseUrl dummy, overridden by HostSelectionInterceptor)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // dummy, actual comes from interceptor
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiInterface = retrofit.create(ApiInterface::class.java)

    // Change base host at runtime
    fun setBaseHost(baseUrl: String) {
        val httpUrl = baseUrl.toHttpUrl()
        hostInterceptor.host = httpUrl.host // ✅ only domain part
        hostInterceptor.scheme = httpUrl.scheme // ✅ keep scheme (http/https)
    }

    // Provide token dynamically
    fun setAuthTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

}