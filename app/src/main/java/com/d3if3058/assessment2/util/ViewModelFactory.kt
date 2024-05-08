package com.d3if3058.assessment2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d3if3058.assessment2.database.TaskDao
import com.d3if3058.assessment2.model.MainVIewModel
import com.d3if3058.assessment2.screen.DetailViewModel

class ViewModelFactory (
    private  val dao: TaskDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(MainVIewModel::class.java)){
            return MainVIewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}