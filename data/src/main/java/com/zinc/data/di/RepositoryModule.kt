package com.zinc.data.di

import com.zinc.data.api.MyApi
import com.zinc.data.repository.MyRepository
import com.zinc.data.repository.MyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideMyRepository(
        myApi: MyApi
    ): MyRepository =
        MyRepositoryImpl(myApi)
// SavedStateHandle 를 통한 주입이 가능해짐 // 첫 화면 노출시에는 `handle.get<String>("id")` 에 값이 없다.
// 화면이 죽었다 살아난 경우 값이 있는걸 확인 할 수 있다.
}
