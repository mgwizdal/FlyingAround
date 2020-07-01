package com.example.flyingaround.search.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStations(vararg stationEntities: StationEntity): Array<Long>

    @Query(GET_NUMBER_OF_STATIONS_WITH_CODE)
    fun getNumberOfStationsWithCode(code: String): Boolean

    companion object {
        private const val GET_NUMBER_OF_STATIONS_WITH_CODE =
            "SELECT COUNT(*) FROM ${StationEntity.TABLE_NAME} WHERE ${StationEntity.TABLE_NAME}.${StationEntity.COLUMN_STATION_CODE} = :code"
    }
}