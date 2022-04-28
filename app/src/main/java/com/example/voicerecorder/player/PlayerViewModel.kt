package com.example.voicerecorder.player

import android.app.Application
import android.media.MediaDescription
import android.media.browse.MediaBrowser
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerViewModel(itemPath: String?, application: Application): AndroidViewModel(application), LifecycleObserver {
    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player
    private var contentPosition = 0L
    private var playWhenReady = true
    var itemPath: String? = itemPath

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    // запуск создания
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded(){
        setupPlayer()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded(){
        releaseExoPlayer()
    }
    //создание плеера из элементов
    private fun setupPlayer() {
     val dataSourceFactory = DefaultDataSourceFactory(getApplication(),
         Util.getUserAgent(getApplication(), "recorder"))
         val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
           MediaItem.fromUri(Uri.parse(itemPath)))
       val player = SimpleExoPlayer.Builder(getApplication()).build()
        player.prepare(mediaSource)
        player.playWhenReady = playWhenReady
        player.seekTo(contentPosition)
        this._player.value = player
    }

    //остановка и помощь сборщику мусора
    fun releaseExoPlayer(){
        val player =  _player.value ?: return
        this._player.value = null
        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()

    }

    override fun onCleared() {
        super.onCleared()
        releaseExoPlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}


