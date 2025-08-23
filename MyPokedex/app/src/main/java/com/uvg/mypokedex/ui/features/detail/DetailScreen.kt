package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.NameTopBar
import com.uvg.mypokedex.ui.components.PokemonMeasurements
import com.uvg.mypokedex.ui.components.StatsRow
import com.uvg.mypokedex.ui.features.home.HomeViewModel

@Preview
@Composable
fun DetailScreen(pokemonList: List<Pokemon> = (HomeViewModel().getPokemonList())) {
    val pokemon = pokemonList[1]
    Scaffold { paddingValues ->
        Column (modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            NameTopBar(pokemon.name, true)
            PokemonHeader(pokemon)
            PokemonMeasurements(pokemon)
            StatsRow(pokemon)
        }
    }
}

