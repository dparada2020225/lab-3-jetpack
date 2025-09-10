package com.uvg.mypokedex.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val stats: Stats,
    val weight: Float,
    val height: Float,
    val name: String,
    val type: List<PokeType>,
)