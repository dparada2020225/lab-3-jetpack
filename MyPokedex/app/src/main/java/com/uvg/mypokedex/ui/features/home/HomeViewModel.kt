package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat
import com.uvg.mypokedex.data.model.Stats

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
                stats = Stats(
                    hp = Stat(130f, "HP"),
                    attack = Stat(85f, "Attack"),
                    defense = Stat(80f, "Defense"),
                    specialAttack = Stat(85f, "Sp. Attack"),
                    specialDefense = Stat(95f, "Sp. Defense"),
                    speed = Stat(60f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(53f, "HP"),
                    attack = Stat(51f, "Attack"),
                    defense = Stat(53f, "Defense"),
                    specialAttack = Stat(61f, "Sp. Attack"),
                    specialDefense = Stat(56f, "Sp. Defense"),
                    speed = Stat(40f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(35f, "HP"),
                    attack = Stat(55f, "Attack"),
                    defense = Stat(40f, "Defense"),
                    specialAttack = Stat(50f, "Sp. Attack"),
                    specialDefense = Stat(50f, "Sp. Defense"),
                    speed = Stat(90f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(73f, "HP"),
                    attack = Stat(76f, "Attack"),
                    defense = Stat(75f, "Defense"),
                    specialAttack = Stat(81f, "Sp. Attack"),
                    specialDefense = Stat(100f, "Sp. Defense"),
                    speed = Stat(100f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(60f, "HP"),
                    attack = Stat(70f, "Attack"),
                    defense = Stat(65f, "Defense"),
                    specialAttack = Stat(125f, "Sp. Attack"),
                    specialDefense = Stat(105f, "Sp. Defense"),
                    speed = Stat(90f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(48f, "HP"),
                    attack = Stat(48f, "Attack"),
                    defense = Stat(48f, "Defense"),
                    specialAttack = Stat(48f, "Sp. Attack"),
                    specialDefense = Stat(48f, "Sp. Defense"),
                    speed = Stat(48f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(100f, "HP"),
                    attack = Stat(100f, "Attack"),
                    defense = Stat(100f, "Defense"),
                    specialAttack = Stat(100f, "Sp. Attack"),
                    specialDefense = Stat(100f, "Sp. Defense"),
                    speed = Stat(100f, "Speed")
                )
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
                stats = Stats(
                    hp = Stat(57f, "HP"),
                    attack = Stat(80f, "Attack"),
                    defense = Stat(91f, "Defense"),
                    specialAttack = Stat(80f, "Sp. Attack"),
                    specialDefense = Stat(87f, "Sp. Defense"),
                    speed = Stat(75f, "Speed")
                )
            ),
            Pokemon(
                id = 2,
                name = "Ivysaur",
                height = 20.32F,
                weight = 6.6F,
                category = "Seed",
                abilities = "Overgrow",
                gender = "Male, Female",
                type = listOf(PokeType.GRASS, PokeType.POISON),
                weaknesses = "Fire, Ice, Flying, Psychic",
                stats = Stats(
                    hp = Stat(57f, "HP"),
                    attack = Stat(80f, "Attack"),
                    defense = Stat(91f, "Defense"),
                    specialAttack = Stat(80f, "Sp. Attack"),
                    specialDefense = Stat(87f, "Sp. Defense"),
                    speed = Stat(75f, "Speed")
                )
            ),
            Pokemon(
                id = 7,
                name = "Squirtle",
                height = 20.32F,
                weight = 6.6F,
                category = "Tiny Turtle",
                abilities = "Torrent",
                gender = "Male, Female",
                type = listOf(PokeType.WATER),
                weaknesses = "Grass, Electric",
                stats = Stats(
                    hp = Stat(57f, "HP"),
                    attack = Stat(80f, "Attack"),
                    defense = Stat(91f, "Defense"),
                    specialAttack = Stat(80f, "Sp. Attack"),
                    specialDefense = Stat(87f, "Sp. Defense"),
                    speed = Stat(75f, "Speed")
                )
            )
        )
    }
}