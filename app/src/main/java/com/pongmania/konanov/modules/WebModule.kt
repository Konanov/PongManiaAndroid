package com.pongmania.konanov.modules

import com.google.gson.GsonBuilder
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.interceptors.BasicAuthInterceptor
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
class WebModule(private val baseUrl: String) {

    private val apiBaseUrl = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024L * 1024L
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()!!
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, ctx: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(CredentialsPreference.getEmail(ctx),
                        CredentialsPreference.getPassword(ctx)))
        client.cache(cache)
        return client.build()!!
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()!!
    }

    //fun allPlayers(): Flowable<List<Player>> {
    //    return api.getPlayers()
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .subscribeOn(Schedulers.io())
    //}
}