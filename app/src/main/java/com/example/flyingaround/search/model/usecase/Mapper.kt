package com.example.flyingaround.search.model.usecase

import com.example.flyingaround.search.model.db.StationEntity
import com.example.flyingaround.search.network.StationDto
import com.example.flyingaround.search.view.StationAutoComplete

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

fun StationEntity.mapToAutoCompleteItem(): StationAutoComplete {
    return StationAutoComplete(
        code,
        name,
        alternateName
    )
}