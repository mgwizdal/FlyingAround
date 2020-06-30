package com.example.flyingaround.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flyingaround.search.model.db.StationDao
import com.example.flyingaround.search.model.db.StationEntity


@Database(
    entities = [
        StationEntity::class
    ],
    version = 1
)
abstract class FlyingAroundDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao

    companion object {
        private const val DB_NAME = "FlyingAroundDatabase"

        @Volatile
        private var INSTANCE: FlyingAroundDatabase? = null

        fun getInstance(context: Context): FlyingAroundDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FlyingAroundDatabase::class.java,
                DB_NAME
            )
                .build()
        }
    }
}
