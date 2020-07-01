package com.example.flyingaround.search.model.db

import com.example.flyingaround.search.view.StationAutoComplete

class StationRepository (private val stationDao: StationDao) {
    fun isStationCodeCorrect(code: String): Boolean {
        return stationDao.getNumberOfStationsWithCode(code)
    }
}