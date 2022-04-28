package com.example.voicerecorder.removeDialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voicerecorder.database.RecordDatabaseDAO


class RemoveViewModelFactory(
    private var databaseDAO: RecordDatabaseDAO,
    private val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveViewModel::class.java)){
            return RemoveViewModel(databaseDAO, app) as T
    }
}
    throw IllegalArgumentException("unknown ViewModel class")
}

