package com.example.flyingaround.resultlist.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface FlightsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFlyingItems(vararg stationEntities: FlightInfoItemEntity): Array<Long>

    @Query(GET_ALL_ITEMS)
    fun getAllFlightInfoItems(): Flowable<List<FlightInfoItemEntity>>

    @Query(GET_ITEM)
    fun getFlightInfoItem(flightNumber: String): Flowable<FlightInfoItemEntity>

    @Query(DELETE_ALL)
    fun deleteCurrentItems()

    companion object {
        private const val DELETE_ALL = "DELETE FROM ${FlightInfoItemEntity.TABLE_NAME}"
        private const val GET_ITEM = "SELECT * FROM ${FlightInfoItemEntity.TABLE_NAME} WHERE ${FlightInfoItemEntity.COLUMN_FLIGHT_NUMBER} = :flightNumber"
        private const val GET_ALL_ITEMS = "SELECT * FROM ${FlightInfoItemEntity.TABLE_NAME}"
    }
}