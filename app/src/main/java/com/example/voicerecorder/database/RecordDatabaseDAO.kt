package com.example.voicerecorder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDatabaseDAO {
    @Insert
    fun insert(record: RecordingItem) // добавление данных в таблицу

    @Update
    fun update(record: RecordingItem) // обновляет данные

    @Query("SELECT * from recording_table WHERE id = :key") // выборки данных из бд  операции над ними
    fun getRecord(key: Long?):RecordingItem // выбор по id

    @Query("DELETE from recording_table")
    fun clearAll() // удаление всех записей

    @Query("DELETE FROM recording_table WHERE id = :key")
    fun removeRecord(key: Long?) // удаление записи по id

    @Query("SELECT * FROM recording_table ORDER BY id DESC")
    fun getAllRecord(): LiveData<MutableList<RecordingItem>> // возвращает все хаписи таблицы и записывает их в LiveData

    @Query("SELECT COUNT (*) FROM recording_table")
    fun getCount():Int // возвращает количество записей
}