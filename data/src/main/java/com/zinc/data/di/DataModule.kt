package com.zinc.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.repository.*
import com.zinc.domain.repository.*
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
    abstract fun bindLoginRepository(
        repository: LoginRepositoryImpl
    ): LoginRepository

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

    @Binds
    abstract fun bindSearchRepository(
        repository: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindReportRepository(
        repository: ReportRepositoryImpl
    ): ReportRepository

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
    }
}