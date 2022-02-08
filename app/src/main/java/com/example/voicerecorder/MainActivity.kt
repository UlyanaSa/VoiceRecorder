package com.example.voicerecorder

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.voicerecorder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        NavigationUI.setupWithNavController(bind.bottomNavMenu,
            Navigation.findNavController(this, R.id.nav_host_fragment_container))

        fun isServiceRunning():Boolean{
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager // проверка запуска сервиса
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if("com.example.voicerecorder.record.RecordService" == service.service.className){
                    return true
                }
            }
            return false
        }
    }
}