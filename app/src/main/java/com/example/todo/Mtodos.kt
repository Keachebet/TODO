package com.example.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Todo::class],
    version = 3
)
abstract class Mytododb : RoomDatabase(){
    abstract fun todosDao(): TodoDao
    companion object {
        @Volatile private var instance: Mytododb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            Mytododb::class.java, "todostore.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}


