package com.pongmania.konanov.interceptors

import android.app.Application
import com.pongmania.konanov.util.CredentialsPreference
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicAuthInterceptor(val app: Application) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val cred = Credentials.basic(CredentialsPreference.getEmail(app),
                CredentialsPreference.getPassword(app))
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
                .header("Authorization", cred).build()
        return chain.proceed(authenticatedRequest)
    }

}