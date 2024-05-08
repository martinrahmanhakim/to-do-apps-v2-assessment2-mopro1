package com.d3if3058.mobpro1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.d3if3058.assessment2.database.TaskDao
import com.d3if3058.assessment2.model.Task

@Database(entities = [Task:: class], version = 1, exportSchema = false)
abstract class TaskDb : RoomDatabase(){
    abstract val dao: TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDb? = null

        fun getInstance(context: Context): TaskDb {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDb::class.java,
                        "ToDoApps.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}