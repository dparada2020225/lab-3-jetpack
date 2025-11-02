package com.uvg.mypokedex.data.util

import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat
import com.uvg.mypokedex.data.network.dto.PokemonDetailDto

fun PokemonDetailDto.mapToDomain(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        weight = this.weight / 10f, // API devuelve en hectogramos
        height = this.height / 10f, // API devuelve en decímetros
        type = this.types.map { typeSlot ->
            PokeType.typeFromString(typeSlot.type.name)
        },
        stats = this.stats.map { statDto ->
            Stat(
                value = statDto.baseStat.toFloat(),
                name = statDto.stat.name
            )
        }
    )
}