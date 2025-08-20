package com.senukai.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senukai.app.data.entities.ReadBookStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ReadBookStatusEntity>)

    @Query("SELECT * FROM read_status_book ORDER BY id")
    fun observeAll(): Flow<List<ReadBookStatusEntity>>
}