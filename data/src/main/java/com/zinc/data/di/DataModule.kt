package com.zinc.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zinc.data.api.DetailApi
import com.zinc.data.api.MyApi
import com.zinc.data.repository.DetailRepository
import com.zinc.data.repository.DetailRepositoryImpl
import com.zinc.data.repository.MyRepository
import com.zinc.data.repository.MyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
abstract class DataModule {
    @Binds
    abstract fun bindMyRepository(
        repository: MyRepositoryImpl
    ): MyRepository

    @Binds
    abstract fun bindDetailRepository(
        repository: DetailRepositoryImpl
    ): DetailRepository

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ApiModule {
        @Provides
        @Singleton
        fun provideConverter(): Converter.Factory {
            return Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType())
        }

        @Provides
        @Singleton
        fun provideMyApi(
            retrofit: Retrofit
        ): MyApi {
            return retrofit.create(MyApi::class.java)
        }

        @Provides
        @Singleton
        fun provideDetailApi(
            retrofit: Retrofit
        ): DetailApi {
            return retrofit.create(DetailApi::class.java)
        }

//        @Provides
//        @Singleton
//        fun provideGithubApi(
//            okHttpClient: OkHttpClient,
//            converterFactory: Converter.Factory
//        ): GithubApi {
//            return Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(converterFactory)
//                .client(okHttpClient)
//                .build()
//                .create(GithubApi::class.java)
//        }
    }
}