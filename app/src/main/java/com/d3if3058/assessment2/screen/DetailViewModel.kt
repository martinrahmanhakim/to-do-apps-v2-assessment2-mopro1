package com.d3if3058.assessment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d3if3058.assessment2.database.TaskDao
import com.d3if3058.assessment2.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DetailViewModel(private val dao: TaskDao) : ViewModel() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(judul: String, isi: String, prioritas: String) {
        val task = Task(
            judul = judul,
            isi = isi,
            prioritas = prioritas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(task)
        }
    }

    suspend fun getTask(id: Long): Task? {
        return dao.getTaskById(id)
    }

    fun update(id: Long, judul: String, isi: String, prioritas: String) {
        val task = Task(
            id = id,
            judul = judul,
            isi = isi,
            prioritas = prioritas

        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(task)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
