package com.example.flyingaround.search.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flyingaround.search.model.db.StationEntity.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class StationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_STATION_ID)
    val id: Int? = null,
    @ColumnInfo(name = COLUMN_STATION_CODE)
    val code: String,
    @ColumnInfo(name = COLUMN_STATION_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_STATION_ALTERNATE_NAME)
    val alternateName: String?,
    @ColumnInfo(name = COLUMN_STATION_COUNTRY_CODE)
    val countryCode: String,
    @ColumnInfo(name = COLUMN_STATION_COUNTRY_NAME)
    val countryName: String
) {
    companion object {
        const val TABLE_NAME = "stations"
        const val COLUMN_STATION_ID = "id"
        const val COLUMN_STATION_CODE = "code"
        const val COLUMN_STATION_NAME = "name"
        const val COLUMN_STATION_ALTERNATE_NAME = "alternateName"
        const val COLUMN_STATION_COUNTRY_NAME = "countryName"
        const val COLUMN_STATION_COUNTRY_CODE = "countryCode"
    }
}