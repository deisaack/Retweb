package co.ke.deisaac.retweb.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    private const val JSON_BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val MOCK_BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"

    private val xyzInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .header("Interceptor-Header", "xyz")
            .build()
        chain.proceed(newRequest)
    }

    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", "AppConstants.tmdbApiKey")
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(xyzInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()


    private fun getRetrofit(base_url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val jsonPlaceholderApi: JsonPlaceholderApi = getRetrofit(base_url = JSON_BASE_URL).create(JsonPlaceholderApi::class.java)
    val mockApi: MockApi = getRetrofit(base_url = MOCK_BASE_URL).create(MockApi::class.java)
}

