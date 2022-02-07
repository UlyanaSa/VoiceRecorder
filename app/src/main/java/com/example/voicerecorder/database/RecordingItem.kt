package com.example.voicerecorder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recording_table") // аннотация создания таблицы
data class RecordingItem(
    @PrimaryKey(autoGenerate = true) // ключевое поле
    val id: Long = 0L,
    @ColumnInfo(name = "name") // название полей таблицы
    var name: String = "",
    @ColumnInfo(name = "filePath")
    var filePath: String = "",
    @ColumnInfo(name = "length")
    var length: Long = 0L,
    @ColumnInfo(name = "time")
    var time: Long = 0L
) {
}