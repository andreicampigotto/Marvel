package com.care_me.marvel.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.care_me.marvel.model.Hero

@Dao
interface HeroesDAO {

    @Query("SELECT * FROM Hero")
    suspend fun get(): List<Hero>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Hero>)

    @Query("SELECT * FROM Hero WHERE name LIKE 'like' || :query || '%'")
    suspend fun getFiltred(query: String): List<Hero>?

}