package com.uvg.mypokedex.data.model

data class Stats(
    val hp: Stat,
    val attack: Stat,
    val defense: Stat,
    val specialAttack: Stat,
    val specialDefense: Stat,
    val speed: Stat
)

data class Stat(
    val number: Float,
    val name: String
)