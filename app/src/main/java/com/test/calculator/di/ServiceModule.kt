package com.test.calculator.di

import com.test.calculator.service.CalculatorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideCalculatorService(): CalculatorService {
        return CalculatorService()
    }
}