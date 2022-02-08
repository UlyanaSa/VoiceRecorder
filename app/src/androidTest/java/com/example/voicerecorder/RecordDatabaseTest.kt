package com.example.voicerecorder


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.voicerecorder.database.RecordDatabase
import com.example.voicerecorder.database.RecordDatabaseDAO
import com.example.voicerecorder.database.RecordingItem
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecordDatabaseTest {

    private lateinit var recordDatabaseDao: RecordDatabaseDAO
    private lateinit var database: RecordDatabase

    @Before
    fun createDb() {
        // контекс предоставляется классом instr..R, который создает бд в памяти. она будет удалена после теста
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecordDatabase::class.java)
            .allowMainThreadQueries() // создается в главном потоке (только во время тестирования)
            .build()
        recordDatabaseDao = database.recordDatabaseDAO
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(IOException::class)
    fun testDatabase(){
        recordDatabaseDao.insert(RecordingItem())
        val getCount = recordDatabaseDao.getCount()
        assertEquals(getCount, 1) // функция сравнивает значения getCount с контрольным
    }
}