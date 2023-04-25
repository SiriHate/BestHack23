package com.siri_hate.besthack23.di

import android.content.Context
import com.siri_hate.besthack23.data.FileAnalyzerImpl
import com.siri_hate.besthack23.data.FileManagerImpl
import com.siri_hate.besthack23.domain.FileAnalyzer
import com.siri_hate.besthack23.domain.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFileManager(@ApplicationContext context: Context): FileManager = FileManagerImpl(context)

    @Singleton
    @Provides
    fun provideFileAnalyzer(@ApplicationContext context: Context): FileAnalyzer = FileAnalyzerImpl()

}