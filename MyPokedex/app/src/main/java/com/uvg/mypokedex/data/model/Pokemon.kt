package com.uvg.mypokedex.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Pokemon(
    val id: Int,
    val stats: List<Stat>,
    val weight: Float,
    val height: Float,
    val name: String,
    val type: List<PokeType>
) : Parcelable {
    fun toPokemon(typeString: String): Pokemon {
        return Pokemon(
            id = this.id,
            stats = this.stats,
            weight = this.weight,
            height = this.height,
            name = this.name,
            type = this.type.map { type -> PokeType.typeFromString(typeString) })
    }
}
