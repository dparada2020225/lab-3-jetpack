package com.uvg.mypokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uvg.mypokedex.data.local.converters.PokeTypeListConverter
import com.uvg.mypokedex.data.local.converters.StatListConverter
import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat

/**
 * Entidad Room que representa un Pokémon en caché local.
 * Almacena toda la información necesaria para mostrar en la UI sin conexión.
 */
@Entity(tableName = "cached_pokemon")
@TypeConverters(PokeTypeListConverter::class, StatListConverter::class)
data class CachedPokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokeType>,
    val stats: List<Stat>,
    val weight: Float,
    val height: Float,
    val lastFetchedAt: Long // Timestamp en milisegundos
) {
    /**
     * Convierte esta entidad de caché a un objeto Pokemon del dominio.
     */
    fun toDomainModel(): Pokemon {
        return Pokemon(
            id = id,
            name = name,
            type = types,
            stats = stats,
            weight = weight,
            height = height
        )
    }

    companion object {
        /**
         * Crea un CachedPokemon a partir de un Pokemon del dominio.
         */
        fun fromDomainModel(pokemon: Pokemon): CachedPokemon {
            return CachedPokemon(
                id = pokemon.id,
                name = pokemon.name,
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/refs/heads/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                types = pokemon.type,
                stats = pokemon.stats,
                weight = pokemon.weight,
                height = pokemon.height,
                lastFetchedAt = System.currentTimeMillis()
            )
        }
    }
}
