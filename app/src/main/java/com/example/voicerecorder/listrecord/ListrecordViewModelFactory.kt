package com.example.voicerecorder.listrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voicerecorder.database.RecordDatabaseDAO

class ListRecordViewModelFactory(private val databaseDAO: RecordDatabaseDAO):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRecordViewModel::class.java)){
        return ListRecordViewModel(databaseDAO) as T
    }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}