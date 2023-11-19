package com.zinc.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zinc.data.api.BerryBucketApi
import com.zinc.data.repository.AlarmRepositoryImpl
import com.zinc.data.repository.CategoryRepositoryImpl
import com.zinc.data.repository.CommonRepositoryImpl
import com.zinc.data.repository.DetailRepositoryImpl
import com.zinc.data.repository.FeedRepositoryImpl
import com.zinc.data.repository.KeywordRepositoryImpl
import com.zinc.data.repository.LoginRepositoryImpl
import com.zinc.data.repository.MoreRepositoryImpl
import com.zinc.data.repository.MyRepositoryImpl
import com.zinc.data.repository.ReportRepositoryImpl
import com.zinc.data.repository.SearchRepositoryImpl
import com.zinc.data.repository.WriteRepositoryImpl
import com.zinc.domain.repository.AlarmRepository
import com.zinc.domain.repository.CategoryRepository
import com.zinc.domain.repository.CommonRepository
import com.zinc.domain.repository.DetailRepository
import com.zinc.domain.repository.FeedRepository
import com.zinc.domain.repository.KeywordRepository
import com.zinc.domain.repository.LoginRepository
import com.zinc.domain.repository.MoreRepository
import com.zinc.domain.repository.MyRepository
import com.zinc.domain.repository.ReportRepository
import com.zinc.domain.repository.SearchRepository
import com.zinc.domain.repository.WriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
internal abstract class DataModule {
    @Binds
    abstract fun bindCommonRepository(
        repository: CommonRepositoryImpl
    ): CommonRepository

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

    @Binds
    abstract fun bindCategoryRepository(
        repository: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindWriteRepository(
        repository: WriteRepositoryImpl
    ): WriteRepository

    @Binds
    abstract fun bindAlarmRepository(
        repository: AlarmRepositoryImpl
    ): AlarmRepository

    @Binds
    abstract fun bindKeywordRepository(
        repository: KeywordRepositoryImpl
    ): KeywordRepository

    @Binds
    abstract fun bindMoreRepository(
        repository: MoreRepositoryImpl
    ): MoreRepository

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ApiModule {
        @OptIn(ExperimentalSerializationApi::class)
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