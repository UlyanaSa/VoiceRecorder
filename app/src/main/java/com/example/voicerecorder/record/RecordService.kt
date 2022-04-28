package com.example.voicerecorder.record

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.voicerecorder.MainActivity
import com.example.voicerecorder.R
import com.example.voicerecorder.database.RecordDatabase
import com.example.voicerecorder.database.RecordDatabaseDAO
import com.example.voicerecorder.database.RecordingItem
import kotlinx.coroutines.*
import java.io.EOFException
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RecordService:Service() {

    private var mFileName: String? = null
    private var mFilePath: String? = null
    private var mCountRecord: Int? = null
    private var mRecorder: MediaRecorder? = null // для записи звука

    private var mStartingTimeMillis: Long = 0
    private var mElapsedTimeMillis: Long = 0 // прошедшее время

    private var mRecordDatabase: RecordDatabaseDAO? = null
    private val mJob = Job() // фоновая работа корутины
    private val mUiScope = CoroutineScope(Dispatchers.Main+mJob) // ui скоп, в который мы передаем наш поток к главному потоку
    private val CHANNEL_ID: String = getString(R.string.notification_channel_id)



    override fun onBind(p0: Intent?): IBinder? { // возвращает интерфейс для связи клиентов с сервисом
       return null // нам не нужно, поэтому отправляем null
    }

    override fun onCreate() {
        super.onCreate()
        mRecordDatabase = RecordDatabase.getInstance(application).recordDatabaseDAO
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int { //вызывается после старта сервиса

        mCountRecord = intent?.extras?.get("COUNT")!! as Int
        startRecording()
        return START_STICKY // сервис не будет перезапущен после старта сервиса
    }

    private fun startRecording(){
        setFileNameAndPath()

        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder?.setOutputFile(mFilePath)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setAudioChannels(1)
        mRecorder?.setAudioEncodingBitRate(192000)
        try{
            mRecorder?.prepare() // подготовка
            mRecorder?.start()
            mStartingTimeMillis = System.currentTimeMillis() // сохраняем текущее время
            startForeground(1, createNotification()) // запуск сервиса
        }catch (e: IOException){
            Log.e("RecordingService", "prepare failed")
        }
    }
    fun createNotification(): Notification?{ // возвращает уведомления
        val mBuilder: NotificationCompat.Builder
        = NotificationCompat.Builder(applicationContext, CHANNEL_ID) // канал уведомления
            .setSmallIcon(R.drawable.ic_mic_play) // иконка
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_recording))
            .setOngoing(true) //отображение уведомления поверх других. не даст его смахнуть
        mBuilder.setContentIntent(PendingIntent.getActivities( //интент ожидания, который будет отправлен
                applicationContext, 0, arrayOf(
                Intent(applicationContext,MainActivity::class.java)
                ),0
            )
        )
        return mBuilder.build()
    }

    private fun setFileNameAndPath(){
        var count = 0
        var f:File
        // переменная для сохранения времени файла
        val dataTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis())
        do{
            mFileName = getString(R.string.default_file_name) + "_" + dataTime + count +".mp4"
            mFilePath = application.getExternalFilesDir(null)?.absolutePath
            mFilePath+="/$mFileName"
            count++
            f = File(mFilePath)
        }while(f.exists()&&!f.isDirectory)
    }

    private fun stopRecording(){
        val recordingItem = RecordingItem()
        mRecorder?.stop()
        mElapsedTimeMillis = System.currentTimeMillis() - mStartingTimeMillis
        mRecorder?.release()
        Toast.makeText(this, getString(R.string.toast_recording_finish),
        Toast.LENGTH_SHORT).show()

        recordingItem.name = mFileName.toString()
        recordingItem.filePath = mFilePath.toString()
        recordingItem.length = mElapsedTimeMillis
        recordingItem.time = System.currentTimeMillis()

        mRecorder = null

        try{
            mUiScope.launch {
                withContext(Dispatchers.IO){
                    mRecordDatabase?.insert(recordingItem)
                }
            }
        } catch (e:Exception){
            Log.e("RecordService", "exception", e)
        }
    }

    override fun onDestroy() {
        if(mRecorder != null){
            stopRecording()
        }
        super.onDestroy()
    }
}