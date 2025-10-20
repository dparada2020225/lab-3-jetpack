package com.uvg.mypokedex.data.model

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Stat(
    val value: Float, val name: String
) : Parcelable

// Para este composable, si solicite ayuda de la IA. Preguntandole que necesitaba para hacer un barra de progreso para los stats, tomando los valores maximos que encontre en
// https://www.serebii.net/pokedex-xy/stat/hp.shtml, comparado con el valor que tiene el pokemon actual.

@Composable
fun Stat.StatBar() {
    val max = when (this.name) {
        "hp" -> 255F
        "attack" -> 190F
        "defense" -> 230F
        "special-attack" -> 194F
        "special-defense" -> 230F
        "speed" -> 180F
        else -> 255F
    }
    val progress = this.value / max

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(0.5f),
        progress = { progress },
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}