package com.zinc.data.api

import android.util.Log
import com.zinc.datastore.login.LoginPreferenceDataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val loginPreferenceDataStoreModule: LoginPreferenceDataStoreModule
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val accessToken = loginPreferenceDataStoreModule.loadAccessToken.first()
            Log.e("ayhan", "AcceesToken : $accessToken")
            val request = if (accessToken.isNotEmpty()) {
                chain.request().putTokenHeader(accessToken)
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
    }

    private fun Request.putTokenHeader(accessToken: String): Request {
        return this.newBuilder()
            .addHeader(AUTHORIZATION, accessToken)
            .build()
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}