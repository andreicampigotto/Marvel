package com.care_me.marvel.di

import android.content.Context
import com.care_me.marvel.database.AppDatabase
import com.care_me.marvel.database.dao.HeroesDAO
import com.care_me.marvel.repository.MarvelRepository
import com.care_me.marvel.service.MarvelAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideMarvel(retrofit: Retrofit): MarvelAPI = retrofit.create(MarvelAPI::class.java)

    @Provides
    fun provideHeroDao(@ApplicationContext context: Context): HeroesDAO {
        return AppDatabase.getDatabase(context).getHeroDao()
    }

    @Provides
    fun provideHeroRepository(service: MarvelAPI, dao: HeroesDAO): MarvelRepository =
        MarvelRepository(service, dao)
}