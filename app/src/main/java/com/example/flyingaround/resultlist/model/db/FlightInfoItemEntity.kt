package com.example.flyingaround.resultlist.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flyingaround.resultlist.model.db.FlightInfoItemEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class FlightInfoItemEntity(
    @ColumnInfo(name = COLUMN_ORIGIN)
    val origin: String,
    @ColumnInfo(name = COLUMN_DESTINATION)
    val destination: String,
    @ColumnInfo(name = COLUMN_FLIGHT_DATE)
    val flightDate: String,
    @ColumnInfo(name = COLUMN_FLIGHT_NUMBER)
    val flightNumber: String,
    @ColumnInfo(name = COLUMN_DURATION)
    val duration: String,
    @ColumnInfo(name = COLUMN_REGULAR_FARE_PRICE)
    val regularFarePrice: Float,
    @ColumnInfo(name = COLUMN_INFANTS_LEFT)
    val infantsLeft: Int,
    @ColumnInfo(name = COLUMN_FARE_CLASS)
    val fareClass: String,
    @ColumnInfo(name = COLUMN_DISCOUNT_IN_PERCENT)
    val discountInPercent: Int,
    @ColumnInfo(name = COLUMN_CURRENCY)
    val currency: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0
) {

    companion object {
        const val TABLE_NAME = "flying_item"
        const val COLUMN_ID = "id"
        const val COLUMN_ORIGIN = "origin"
        const val COLUMN_DESTINATION = "destination"
        const val COLUMN_FLIGHT_DATE = "flight_date"
        const val COLUMN_FLIGHT_NUMBER = "flight_number"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_REGULAR_FARE_PRICE = "regular_fare_price"
        const val COLUMN_INFANTS_LEFT = "infants_left"
        const val COLUMN_FARE_CLASS = "fare_class"
        const val COLUMN_DISCOUNT_IN_PERCENT = "discount_in_percent"
        const val COLUMN_CURRENCY = "currency"
    }
}