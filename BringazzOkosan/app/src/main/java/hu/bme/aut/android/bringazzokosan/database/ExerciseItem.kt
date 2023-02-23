package hu.bme.aut.android.bringazzokosan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "maxspeed") var maxspeed: String,
    @ColumnInfo(name = "avgspeed") var avgspeed: String,
    @ColumnInfo(name = "distance") var distance: String,
    @ColumnInfo(name = "duration") var duration: String
) {}