package com.d3if3058.assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.d3if3058.assessment2.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)
    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM task ORDER by prioritas DESC")
    fun getTask(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Long)
}
