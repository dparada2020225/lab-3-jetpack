package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.getTypeColor
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.theme.AppTypography
import java.util.Locale.getDefault

@Composable
fun PokemonHeader(pokemon: Pokemon) {
    Column(
        modifier = Modifier.padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://raw.githubusercontent.com/PokeAPI/sprites/refs/heads/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
            contentDescription = null,
            modifier = Modifier.padding(10.dp)
        )
        Column(
            modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val lowercaseName = (pokemon.name).lowercase()
            val capitalizedName = lowercaseName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    getDefault()
                ) else it.toString()
            }
            Text(
                text = (capitalizedName),
                style = AppTypography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(bottom = 15.dp),
                text = "#0${pokemon.id}",
                style = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row {
                for (type in pokemon.type) {
                    val typeColor = getTypeColor(type)
                    Surface(
                        modifier = Modifier.padding(4.dp),
                        shape = MaterialTheme.shapes.small,
                        color = typeColor
                    ) {
                        Text(
                            modifier = Modifier.padding(7.dp),
                            text = "$type",
                            style = AppTypography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonHeader() {
    val pokemon = (HomeViewModel().loadMorePokemon())[1]
    PokemonHeader(pokemon)
}