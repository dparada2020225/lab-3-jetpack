package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.R
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.theme.AppTypography

@Composable
fun PokemonMeasurements(pokemon: Pokemon) {
    Surface {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(R.drawable.scale),
                    contentDescription = "Weight icon"
                )
                Column(
                    modifier = Modifier.padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${pokemon.weight} kg",
                        style = AppTypography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Peso", style = AppTypography.bodyMedium)
                }
            }
            Row {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(R.drawable.straighten),
                    contentDescription = "Height icon"
                )
                Column(
                    modifier = Modifier.padding(5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${pokemon.height} cm",
                        style = AppTypography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Altura", style = AppTypography.bodyMedium)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonMeasurements() {
    val pokemon = (HomeViewModel().getPokemonList())[1]
    PokemonMeasurements(pokemon)
}