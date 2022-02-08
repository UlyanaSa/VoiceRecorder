package com.example.voicerecorder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [RecordingItem::class], version = 1, exportSchema = false) // аннотация для создания класса базы данных
// обращение к сущности, указание версии базы и схемы для GSON данных (false потому что не нужно нам)
abstract class RecordDatabase: RoomDatabase() {
    abstract val recordDatabaseDAO: RecordDatabaseDAO

    companion object { // объект, который позволяет привязать функцию или свойство прописанное внутри к классу, а не к экземпляру
        // доступ к стаким член доступен только через имя классе, а не через имя экзепляра класса
        // это нужно для создания бд в единственном виде (Singleton)
        // аннотация волатайл гарантирует, что читаема переменная поступает из основной памяти, а не из кеша
        @Volatile
        private var INSTANCE: RecordDatabase? = null


        fun getInstance(context: Context): RecordDatabase {
            synchronized(this){ // метод будет защищен от выполнения сразу несколькими потоками
                // он гарантирует инициализацию баззы данных только в одном потоке
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecordDatabase::class.java,
                        "recorder_app_database"
                    ).fallbackToDestructiveMigration() // при создании новой структуры будут удаляться старые данные
                       // .addMigrations(/передаем сюда файл SQL команды добавления нового поля/)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}