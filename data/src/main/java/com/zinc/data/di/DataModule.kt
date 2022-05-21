package com.zinc.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.repository.DetailRepositoryImpl
import com.zinc.data.repository.FeedRepositoryImpl
import com.zinc.data.repository.MyRepositoryImpl
import com.zinc.domain.repository.DetailRepository
import com.zinc.domain.repository.FeedRepository
import com.zinc.domain.repository.MyRepository
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
internal abstract class DataModule {
    @Binds
    abstract fun bindMyRepository(
        repository: MyRepositoryImpl
    ): MyRepository

    @Binds
    abstract fun bindDetailRepository(
        repository: DetailRepositoryImpl
    ): DetailRepository

    @Binds
    abstract fun bindFeedRepository(
        repository: FeedRepositoryImpl
    ): FeedRepository

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
        fun provideBerryBucketApi(
            retrofit: Retrofit
        ): BerryBucketApi {
            return retrofit.create(BerryBucketApi::class.java)
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