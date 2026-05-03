package com.beepstop.widget.model

data class CircuitWidgetData(
    val raceName: String,
    val date: String,
    val circuitId: String,
    val backgroundColorHex: String,
    val textColorHex: String
)

data class CountdownWidgetData(
    val targetDate: String,
    val label: String,
    val raceName: String,
    val backgroundColorHex: String,
    val fontColorHex: String
)

data class StandingsWidgetItem(
    val id: String,
    val name: String,
    val code: String,
    val points: String,
    val constructorId: String,
    val mode: String
)
