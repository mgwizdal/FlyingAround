package com.example.flyingaround.search.view

data class StationAutoComplete(
    val code: String,
    val name: String,
    val alternateName: String?
) {
    override fun toString(): String {
        val localAlternateName = alternateName?.let { ", $it" } ?: ""
        return "${name}$localAlternateName ($code)"
    }
}