package hu.bme.aut.android.bringazzokosan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseItem::class], version = 1)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseItemDao(): ExerciseDao

    companion object {
        fun getDatabase(applicationContext: Context): ExerciseDatabase {
            return Room.databaseBuilder(
                applicationContext,
                ExerciseDatabase::class.java,
                "exercise-list"
            ).build();
        }
    }
}
