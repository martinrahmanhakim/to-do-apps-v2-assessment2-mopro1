package com.d3if3058.assessment2.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d3if3058.assessment2.database.TaskDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainVIewModel (dao: TaskDao) : ViewModel() {
    val optionArray : Array<String> = arrayOf("Prioritas", "Non-Prioritas")

    val data: StateFlow<List<Task>> = dao.getTask().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}