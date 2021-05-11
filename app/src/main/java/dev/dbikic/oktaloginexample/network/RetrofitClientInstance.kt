package dev.dbikic.oktaloginexample.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    lateinit var retrofit: Retrofit

    private const val BASE_URL = "http://192.168.7.78:8080/" // <1>

    private var token = ""

    val retrofitInstance: Retrofit
        get() {
            if (!this::retrofit.isInitialized) {
                val headersInterceptor = Interceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Authorization", "Bearer $token") // <2>
                    chain.proceed(requestBuilder.build())
                }
                val okHttpClient = OkHttpClient() // <3>
                    .newBuilder()
                    .followRedirects(true)
                    .addInterceptor(headersInterceptor) // <4>
                    .build()
                retrofit = Retrofit.Builder() // <5>
                    .baseUrl(BASE_URL) // <6>
                    .addConverterFactory(GsonConverterFactory.create()) // <7>
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // <8>
                    .client(okHttpClient) // <9>
                    .build()
            }
            return retrofit
        }

    fun setToken(token: String) { // <10>
        RetrofitClientInstance.token = token
    }
}
