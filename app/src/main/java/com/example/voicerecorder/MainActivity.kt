package com.example.voicerecorder

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        NavigationUI.setupWithNavController(bottomNavigation,
            Navigation.findNavController(this, R.id.nav_host_fragment_container)
        )


        }
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