package com.zinc.data.di

import com.zinc.data.api.MyApi
import com.zinc.data.repository.MyRepository
import com.zinc.data.repository.MyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DataModule.ApiModule::class])
abstract class DataModule {
    @Binds
    abstract fun bindsMyRepository(
        repository: MyRepositoryImpl
    ): MyRepository

    @InstallIn(SingletonComponent::class)
    @Module
    internal object ApiModule {
//        @Provides
//        @Singleton
//        fun provideMyApi(
//            okHttpClient: OkHttpClient,
//            converterFactory: Converter.Factory
//        ): MyApi {
//            return Retrofit.Builder()
//                .baseUrl("http://13.124.49.86/my")
//                .addConverterFactory(converterFactory)
//                .client(okHttpClient)
//                .build()
//                .create(MyApi::class.java)
//        }

        @Provides
        @Singleton
        fun provideMyApi(
            retrofit: Retrofit
        ): MyApi {
            return retrofit.create(MyApi::class.java)
        }
    }
}