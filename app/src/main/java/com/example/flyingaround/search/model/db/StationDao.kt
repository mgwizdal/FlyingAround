package com.example.flyingaround.search.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStations(vararg stationEntities: StationEntity): Array<Long>


}