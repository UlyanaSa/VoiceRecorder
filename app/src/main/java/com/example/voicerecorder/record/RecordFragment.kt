package com.example.voicerecorder.record

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.voicerecorder.MainActivity
import com.example.voicerecorder.R
import com.example.voicerecorder.database.RecordDatabase
import com.example.voicerecorder.database.RecordDatabaseDAO
import com.example.voicerecorder.databinding.FragmentRecordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.jar.Manifest

class RecordFragment : Fragment() {

    private lateinit var viewModel: RecordViewModel
    private lateinit var mainActivity: MainActivity
    private var database: RecordDatabaseDAO? = null
    private val MY_PERMISSIONS_RECORD_AUDIO = 123
    private lateinit var binding: FragmentRecordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_record,
        container,
        false)

        database = context?.let{ RecordDatabase.getInstance(it).recordDatabaseDAO}
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        binding.recordViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner // определяем владельца жизненного цикла
        if(!mainActivity.isServiceRunning()){
            viewModel.resetTimer()
        }else{
            binding.playButton.setImageResource(R.drawable.ic_stop_24)
        }

        binding.playButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                arrayOf(android.Manifest.permission.RECORD_AUDIO),MY_PERMISSIONS_RECORD_AUDIO
            )
        }else{
                if(mainActivity.isServiceRunning()){
                    onRecord(false)
                    binding.playButton.setImageResource(R.drawable.ic_mic_white)
                    viewModel.stopTimer()
                }else{
                    onRecord(true)
                    binding.playButton.setImageResource(R.drawable.ic_stop_24)
                    viewModel.startTimer()
                }
        }
        }
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
            )
        return binding.root
    }
    private fun onRecord(start: Boolean){
        val intent: Intent = Intent(activity, RecordService::class.java)

        if(start){
            Toast.makeText(activity,getString(R.string.toast_recording_start), Toast.LENGTH_SHORT).show()
            val folder = File(activity?.getExternalFilesDir(null)?.
            absolutePath.toString() + "/VoiceRecorder")

            if(!folder.exists()){
                folder.mkdir()
            }
            activity?.startService(intent)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }else{
            activity?.stopService(intent)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_PERMISSIONS_RECORD_AUDIO -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onRecord(true)
                    viewModel.startTimer()
                }else{
                    Toast.makeText(activity,getString(R.string.toast_recording_permissions),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun createChannel(channelId:String, channelName:String){
        //все уведомления рапределены по каналам, чтобы обеспечить тонкую настройку уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
                setSound(null, null)
            }
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}