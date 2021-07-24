package br.com.danielassumpcao.inter.services

import br.com.danielassumpcao.inter.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitConfig {

    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) httpClient.addInterceptor(logging)
        httpClient.connectTimeout(BuildConfig.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(BuildConfig.TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(BuildConfig.TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(Interceptor { chain ->
            val request: Request = chain
                .request()
                .newBuilder()
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Authorization", BuildConfig.AUTHORIZATION)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
            chain.proceed(request)
        })

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

    }

    fun getGithubService(): GithubService {
        return retrofit.create(GithubService::class.java)
    }

}