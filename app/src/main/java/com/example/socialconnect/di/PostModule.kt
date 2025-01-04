package com.example.socialconnect.di

import com.example.socialconnect.post_feature.data.PostRepositoryImpl
import com.example.socialconnect.post_feature.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl:
        PostRepositoryImpl
    ): PostRepository
}