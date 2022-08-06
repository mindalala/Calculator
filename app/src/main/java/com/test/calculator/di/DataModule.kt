package com.test.calculator.di

import android.content.Context
import com.test.calculator.data.SharedRepository
import com.test.calculator.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context): Repository {
        return SharedRepository(context.getSharedPreferences("save", Context.MODE_PRIVATE))
    }
}