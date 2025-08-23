package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon

class HomeViewModel {
    fun getPokemonList(): List<Pokemon> {
        // Los datos son de https://www.pokemon.com/us/pokedex/
        return listOf(
            Pokemon(
                id = 131,
                name = "Lapras",
                height = 248.92F,
                weight = 485.0F,
                category = "Transport",
                abilities = "Water Absorb",
                gender = "Male, Female",
                type = listOf(PokeType.WATER),
                weaknesses = "Grass, Electric, Fighting, Rock",
                stats = TODO()
            ), Pokemon(
                id = 393,
                name = "Piplup",
                height = 40.64F,
                weight = 11.5F,
                category = "Penguin",
                abilities = "Torrent",
                gender = "Male, Female",
                type = listOf(PokeType.WATER),
                weaknesses = "Grass, Electric",
                stats = TODO()
            ), Pokemon(
                id = 25,
                name = "Pikachu",
                height = 40.64F,
                weight = 13.2F,
                category = "Mouse",
                abilities = "Static",
                gender = "Male, Female",
                type = listOf(PokeType.ELECTRIC),
                weaknesses = "Ground",
                stats = TODO()
            ), Pokemon(
                id = 38,
                name = "Ninetales",
                height = 109.22F,
                weight = 43.9F,
                category = "Fox",
                abilities = "Flash Fire",
                gender = "Male, Female",
                type = listOf(PokeType.FIRE),
                weaknesses = "Water, Ground, Rock",
                stats = TODO()
            ), Pokemon(
                id = 407,
                name = "Roserade",
                height = 88.9F,
                weight = 32.0F,
                category = "Bouquet",
                abilities = "Poison Point, Natural Cure",
                gender = "Male, Female",
                type = listOf(PokeType.GRASS, PokeType.POISON),
                weaknesses = "Fire, Ice, Flying, Psychic",
                stats = TODO()
            ), Pokemon(
                id = 132,
                name = "Ditto",
                height = 30.48F,
                weight = 8.8F,
                category = "Transform",
                abilities = "Limber",
                gender = "Unknown",
                type = listOf(PokeType.NORMAL),
                weaknesses = "Fighting",
                stats = TODO()
            ), Pokemon(
                id = 151,
                name = "Mew",
                height = 40.64F,
                weight = 8.8F,
                category = "New Species",
                abilities = "Synchronize",
                gender = "Unknown",
                type = listOf(PokeType.PSYCHIC),
                weaknesses = "Bug, Ghost, Dark",
                stats = TODO()
            ), Pokemon(
                id = 707,
                name = "Klefki",
                height = 20.32F,
                weight = 6.6F,
                category = "Key Ring",
                abilities = "Prankster",
                gender = "Male, Female",
                type = listOf(PokeType.STEEL, PokeType.FAIRY),
                weaknesses = "Fire, Ground",
                stats = TODO()
            )
        )
    }
}