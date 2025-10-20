package com.uvg.mypokedex.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val stats: List<Stat>,
    val weight: Float,
    val height: Float,
    val name: String,
    val type: List<PokeType>
) : Parcelable