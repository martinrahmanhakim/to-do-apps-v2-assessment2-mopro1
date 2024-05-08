package com.d3if3058.assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val isi: String,
    val prioritas: String
)