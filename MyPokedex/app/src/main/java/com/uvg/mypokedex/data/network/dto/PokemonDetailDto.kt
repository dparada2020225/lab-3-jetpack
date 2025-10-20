package com.uvg.mypokedex.data.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("types")
    val types: List<TypeSlot>,
    @SerializedName("stats")
    val stats: List<StatDto>
)

data class TypeSlot(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: TypeInfo
)

data class TypeInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class StatDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: StatInfo
)

data class StatInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)