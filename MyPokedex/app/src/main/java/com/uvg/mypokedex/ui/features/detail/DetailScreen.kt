package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

@Preview
@Composable
fun DetailScreen(pokemonList: List<Pokemon> = (HomeViewModel().loadMorePokemon())) {
    val pokemon = pokemonList[7]
    MyPokedexTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NameTopBar(pokemon.name, true)
                PokemonHeader(pokemon)
                PokemonMeasurements(pokemon)
                StatsRow(pokemon)
            }
        }
    }
}

