package com.example.voicerecorder.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class PlayerVIewModelFactory(
    private val mediaPath: String,
    private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayerViewModel::class.java)){
            return PlayerViewModel(mediaPath, app) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }

}