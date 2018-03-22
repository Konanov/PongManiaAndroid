package com.pongmania.konanov.dagger.modules

import com.google.gson.GsonBuilder
import com.pongmania.konanov.interceptors.BasicAuthInterceptor
import com.pongmania.konanov.util.CredentialsPreference
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Application
import android.content.Context
import javax.inject.Singleton
import dagger.Provides
import okhttp3.Cache
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import dagger.Module

@Module
class WebModule(private val app: Application) {

    companion object {
        const val API_URL = "http://10.0.2.2:8080/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val cacheSize = 10L * 1024L * 1024L
        val cache = Cache(app.cacheDir, cacheSize)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        val builder =  gsonBuilder.create()!!

        val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(
                        CredentialsPreference.getEmail(app),
                        CredentialsPreference.getPassword(app))
                )
        client.cache(cache)
        val okHttpClient = client.build()!!

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(builder))
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()!!
    }
}