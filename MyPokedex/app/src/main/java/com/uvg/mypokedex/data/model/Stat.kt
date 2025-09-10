package com.uvg.mypokedex.data.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

data class Stats(
    val hp: Stat,
    val attack: Stat,
    val defense: Stat,
    val specialAttack: Stat,
    val specialDefense: Stat,
    val speed: Stat
)

data class Stat(
    val value: Float, val name: String
)

@Serializable
data class StatDTO(val name: String, val value: Float)

// Para este composable, si solicite ayuda de la IA. Preguntandole que necesitaba para hacer un barra de progreso para los stats,
// tomando los valores maximos que encontre en
// https://www.serebii.net/pokedex-xy/stat/hp.shtml, comparado con el valor que tiene el pokemon actual.

@Composable
fun Stat.StatBar() {
    val max = when (this.name) {
        "HP" -> 255F
        "Attack" -> 190F
        "Defense" -> 230F
        "Special Attack" -> 194F
        "Special Defense" -> 230F
        "Speed" -> 180F
        else -> 255F
    }
    val progress = this.value / max

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(0.5f),
        progress = { progress },
        color = Color(0xFFd6c76d),
        trackColor = Color(0xFF2f4e38),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}