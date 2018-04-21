package app.obywatel.toggl.client.internal.retrofit

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

internal object RetrofitFactory {

    fun create(apiToken: String, baseUrl: String): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(apiToken))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val objectMapper = jacksonObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }

    inline fun <reified TService> create(apiToken: String, baseUrl: String): TService = create(apiToken, baseUrl).create(TService::class.java)

    private class BasicAuthInterceptor(user: String, password: String) : Interceptor {

        private val credentials: String = Credentials.basic(user, password)

        constructor(apiToken: String) : this(apiToken, "api_token")

        override fun intercept(chain: Interceptor.Chain): Response {
            val authenticatedRequest = chain.request().newBuilder()
                .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }
    }
}