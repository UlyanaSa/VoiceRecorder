package com.example.voicerecorder.removeDialog

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.voicerecorder.R
import com.example.voicerecorder.database.RecordDatabase
import com.example.voicerecorder.database.RecordDatabaseDAO
import kotlinx.coroutines.*
import java.io.File

class RemoveViewModel(
    private var databaseDAO: RecordDatabaseDAO,
    private var app: Application
    ): ViewModel() {
        private var job = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + job)
        fun removeItem(itemId: Long){
            databaseDAO = RecordDatabase.getInstance(app).recordDatabaseDAO
            try {
                uiScope.launch {
                    withContext(Dispatchers.IO){
                        databaseDAO.removeRecord(itemId)
                    }
                }
            } catch(e: Exception){
                Log.e("TAG", "removeItem: ", e )
            }
        }
        fun removeFile(filePath: String){
            val file = File(filePath)
            if(file.exists()){
                file.delete()
                Toast.makeText(app, R.string.file_deleted_text, Toast.LENGTH_SHORT).show()

            }
        }
}