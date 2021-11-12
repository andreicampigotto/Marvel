package com.care_me.marvel.repository

import com.care_me.marvel.BuildConfig.PRIVATE_KEY
import com.care_me.marvel.BuildConfig.PUBLIC_KEY
import com.care_me.marvel.database.dao.HeroesDAO
import com.care_me.marvel.model.Hero
import com.care_me.marvel.service.MarvelAPI
import com.care_me.marvel.utils.md5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Query
import java.time.ZoneOffset
import javax.inject.Inject

class MarvelRepository @Inject constructor(
    private val service: MarvelAPI,
    private val dao: HeroesDAO
) {

    private suspend fun getAllFromDatabase(): List<Hero>? {
        return dao.get()
    }

    private suspend fun insertIntoDatabase(listOf: List<Hero>): Boolean {
        return withContext(Dispatchers.Default) {
            dao.insert(listOf)
            true
        }
    }

    private suspend fun getFilteredFromDatabase(query: String): List<Hero>? {
        return dao.getFiltered(query)
    }

    suspend fun fetchHeroes(offset: Int, checkInternet: Boolean): List<Hero>? {
        val ts = (System.currentTimeMillis() / 1000).toString()
        if (checkInternet) {
            val resultAPI = withContext(Dispatchers.Default) {
                val response = service.fetchHeroes(
                    ts = ts,
                    apikey = PUBLIC_KEY,
                    hash = md5(
                        "$ts$PRIVATE_KEY$PUBLIC_KEY"
                    ),
                    offset = offset,
                    limit = 20,
                )
                val processResponse = processData(response)
                processResponse?.data?.results
            }
            resultAPI?.let {
                insertIntoDatabase(it)
            }
            return resultAPI
        }
        return getAllFromDatabase()
    }

    suspend fun fetchHeroesByName(offset: Int, name: String, checkInternet: Boolean): List<Hero>? {
        val ts = (System.currentTimeMillis() / 1000).toString()
        if (checkInternet) {
            return withContext(Dispatchers.Default) {
                val response = service.fetchHeroesByName(
                    ts = ts,
                    apikey = PUBLIC_KEY,
                    hash = md5(
                        "$ts$PRIVATE_KEY$PUBLIC_KEY"
                    ),
                    offset = offset,
                    nameStartsWith = name
                )
                val processResponse = processData(response)
                processResponse?.data?.results
            }
        }
        return getFilteredFromDatabase(query = name)
    }

    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body() else null
    }
}