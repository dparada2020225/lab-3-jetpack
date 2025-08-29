package com.uvg.mypokedex.data.model

// Usé https://www.pokemon.com/us/pokedex/ditto como referencia de que atributos usar.

data class Pokemon(
    val id: Int,
    val stats: Stats,
    val weight: Float,
    val height: Float,
    val name: String,
    val category: String,
    val abilities: String,
    val gender: String,
    val type: List<PokeType>,
    val weaknesses: String
)
