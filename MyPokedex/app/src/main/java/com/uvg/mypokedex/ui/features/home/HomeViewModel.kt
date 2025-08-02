package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon

class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        return listOf(
            Pokemon(
                id = 131,
                name = "Lapras",
                height = 248.92,
                weight = 485.0,
                category = "Transport",
                abilities = "Water Absorb",
                gender = "Male, Female",
                type = listOf(PokeType.WATER),
                weaknesses = "Grass, Electric, Fighting, Rock"
            ), Pokemon(
                id = 393,
                name = "Piplup",
                height = 40.64,
                weight = 11.5,
                category = "Penguin",
                abilities = "Torrent",
                gender = "Male, Female",
                type = listOf(PokeType.WATER),
                weaknesses = "Grass, Electric"
            ), Pokemon(
                id = "025".toInt(),
                name = "Pikachu",
                height = 40.64,
                weight = 13.2,
                category = "Mouse",
                abilities = "Static",
                gender = "Male, Female",
                type = listOf(PokeType.ELECTRIC),
                weaknesses = "Ground"
            ), Pokemon(
                id = "10104".toInt(),
                name = "Alolan Ninetales",
                height = 109.22,
                weight = 43.9,
                category = "Fox",
                abilities = "Snow Cloak",
                gender = "Male, Female",
                type = listOf(PokeType.ICE, PokeType.FAIRY),
                weaknesses = "Fire, Poison, Rock, Steel"
            ), Pokemon(
                id = 407,
                name = "Roserade",
                height = 88.9,
                weight = 32.0,
                category = "Bouquet",
                abilities = "Poison Point, Natural Cure",
                gender = "Male, Female",
                type = listOf(PokeType.GRASS, PokeType.POISON),
                weaknesses = "Fire, Ice, Flying, Psychic"
            ), Pokemon(
                id = 132,
                name = "Ditto",
                height = 30.48,
                weight = 8.8,
                category = "Transform",
                abilities = "Limber",
                gender = "Unknown",
                type = listOf(PokeType.NORMAL),
                weaknesses = "Fighting"
            )
        )
    }
}