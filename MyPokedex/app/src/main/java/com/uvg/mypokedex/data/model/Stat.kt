package com.uvg.mypokedex.data.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class Stats(
    val hp: Stat,
    val attack: Stat,
    val defense: Stat,
    val specialAttack: Stat,
    val specialDefense: Stat,
    val speed: Stat
)

data class Stat(
    val number: Float,
    val name: String
)

// Siguiendo los valores maximos que encontre en https://www.serebii.net/pokedex-xy/stat/hp.shtml, comparado con el valor del pokemon actual.

@Composable
fun Stat.StatBar() {
    val max = when(this.name) {
        "HP" -> 255F
        "Attack" -> 190F
        "Defense" -> 230F
        "Special Attack" -> 194F
        "Special Defense" -> 230F
        "Speed" -> 180F
        else -> 255F
    }
    val progress = this.number / max

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(0.5f),
        progress = { progress },
        color = Color(0xFFd6c76d),
        trackColor = Color(0xFF2f4e38),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}