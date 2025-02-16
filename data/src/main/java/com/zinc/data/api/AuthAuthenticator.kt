package com.zinc.data.api

import com.zinc.datastore.login.PreferenceDataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val preferenceDataStoreModule: PreferenceDataStoreModule,
    //  private val berryBucketApi: BerryBucketApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        if (response.code == HTTP_UNAUTHORIZED) {
            val token = runBlocking {
                preferenceDataStoreModule.loadRefreshToken.first()
            }
            // The access token is expired. Refresh the credentials.
//            synchronized(this) {
//                // Make sure only one coroutine refreshes the token at a time.
//                return runBlocking {
//                    val newTokenResult = //berryBucketApi.refreshToken(token)
//                    if (newTokenResult.success) {
//                        val accessToken = newTokenResult.data?.accessToken
//                        val refreshToken = newTokenResult.data?.refreshToken
//                        // Update the access token in your storage.
//                        loginPreferenceDataStoreModule.setAccessToken(accessToken.orEmpty())
//                        loginPreferenceDataStoreModule.setRefreshToken(refreshToken.orEmpty())
//                        return@runBlocking response.request.newBuilder()
//                            .header("Authorization", "Bearer $accessToken")
//                            .build()
//                    } else {
//                        return@runBlocking response.request
//                    }
//                }
//            }
        }
        return response.request
    }
}