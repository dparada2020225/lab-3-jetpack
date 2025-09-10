package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Surface(
        modifier = Modifier.padding(horizontal = 30.dp),
        color = if (isSystemInDarkTheme()) Color(0xFF2f4e38) else Color(0xFFe7e2d4),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.scale), contentDescription = "Weight icon"
                )
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${pokemon.weight} kg",
                        style = AppTypography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text("Weight", style = AppTypography.bodyMedium)
                }
            }
            VerticalDivider(
                thickness = 2.dp, modifier = Modifier.height(24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.straighten),
                    contentDescription = "Height icon"
                )
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${pokemon.height} cm",
                        style = AppTypography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text("Height", style = AppTypography.bodyMedium)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonMeasurements() {
    val pokemon = (HomeViewModel().loadMorePokemon())[1]
    PokemonMeasurements(pokemon)
}