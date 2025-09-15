package com.uvg.mypokedex.data.model

enum class PokeType {
    BUG, DRAGON, FAIRY, FIRE, GHOST, GROUND, NORMAL, PSYCHIC, STEEL, DARK, ELECTRIC, FIGHTING, FLYING, GRASS, ICE, POISON, ROCK, WATER;

    companion object {
        fun typeFromString(type: String): PokeType {
            return when (type) {
                "bug" -> BUG
                "dragon" -> DRAGON
                "fairy" -> FAIRY
                "fire" -> FIRE
                "ghost" -> GHOST
                "ground" -> GROUND
                "normal" -> NORMAL
                "psychic" -> PSYCHIC
                "steel" -> STEEL
                "dark" -> DARK
                "electric" -> ELECTRIC
                "fighting" -> FIGHTING
                "flying" -> FLYING
                "grass" -> GRASS
                "ice" -> ICE
                "poison" -> POISON
                "rock" -> ROCK
                "water" -> WATER
                else -> {
                    throw IllegalArgumentException("Invalid type: $type")
                }
            }
        }
    }
}