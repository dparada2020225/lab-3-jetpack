package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.uvg.mypokedex.data.model.Pokemon

val testPokemon = Pokemon(
    id = 132,
    name = "Ditto",
    height = 30.48,
    weight = 8.8,
    category = "Transform",
    abilities = "Limber",
    gender = "Unknown",
    type = "Normal",
    weaknesses = "Fighting"
)

@Preview
@Composable
fun PokemonCardPreview() {
    PokemonCard(testPokemon)
}

@Composable
fun PokemonCard(pokemon: Pokemon) {
    val typeColor = Color(0xFF9EEFFE)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = typeColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/refs/heads/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                contentDescription = null,
            )
        }
    }
}
