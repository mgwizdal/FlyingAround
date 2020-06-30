package com.example.flyingaround.search.model.usecase

import com.example.flyingaround.search.model.db.StationEntity
import com.example.flyingaround.search.network.StationDto

fun StationDto.mapToEntity(): StationEntity {
    return StationEntity(
        null,
        code,
        name,
        alternateName,
        countryCode,
        countryName
    )
}