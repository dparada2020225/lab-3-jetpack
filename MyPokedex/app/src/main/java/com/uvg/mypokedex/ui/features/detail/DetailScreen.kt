package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.theme.MyPokedexTheme


@Composable
fun DetailScreen(pokemon: Pokemon, navController: NavController) {
    MyPokedexTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NameTopBar(
                    pokemonName = pokemon.name,
                    withHeart = true,
                    onBackClicked = { navController.popBackStack() })
                PokemonHeader(pokemon)
                PokemonMeasurements(pokemon)
                StatsRow(pokemon)
            }
        }
    }
}
