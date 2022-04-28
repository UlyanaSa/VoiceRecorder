package com.example.voicerecorder.listrecord

import androidx.lifecycle.ViewModel
import com.example.voicerecorder.database.RecordDatabaseDAO

class ListRecordViewModel(val dataSourse:RecordDatabaseDAO): ViewModel() {
    val database = dataSourse
    val records = database.getAllRecord()

}