package hu.bme.aut.android.bringazzokosan.database

import androidx.room.*

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    fun getAll(): List<ExerciseItem>

    @Insert
    fun insert(exerciseItems: ExerciseItem): Long

    @Update
    fun update(exerciseItem: ExerciseItem)

    @Delete
    fun deleteItem(exerciseItem: ExerciseItem)
}