package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.components.PokemonCard

fun FilterPokemon(pokemonList: List<Pokemon>, searchText: String): List<Pokemon> {
    if (searchText.isBlank()) {
        return pokemonList
    } else {
        return pokemonList.filter { it.name.contains(searchText, true) }
    }
}

fun OrderPokemon(pokemonList: List<Pokemon>, order: Boolean): List<Pokemon> {
    if (order) {
        return pokemonList.sortedBy {
            it.name
        }
    } else {
        return pokemonList.sortedByDescending { it.name }
    }
}

// Referencia: https://developer.android.com/develop/ui/compose/components/menu
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current)
) {

    var ordered by rememberSaveable { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val pokemonList = viewModel.loadMorePokemon()
    var searchText by rememberSaveable { mutableStateOf("") }
    var filteredPokemonList by rememberSaveable { mutableStateOf(pokemonList) }
    Column(
        modifier = Modifier.padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            TextField(value = searchText, onValueChange = {
                searchText = it
                filteredPokemonList = FilterPokemon(pokemonList, searchText)
            }, label = {
                Text("Filtrar por nombre")
            })
            Box {
                FloatingActionButton(
                    modifier = Modifier.padding(horizontal = 7.dp),
                    onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Order list button",
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                }
                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Ordenar descendentemente") },
                        onClick = { ordered = false })
                    DropdownMenuItem(
                        text = { Text("Ordenar ascendentemente") },
                        onClick = { ordered = true })
                }
            }
        }
        LazyVerticalGrid(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            columns = GridCells.Fixed(2)
        ) {
            items(OrderPokemon(filteredPokemonList, ordered)) { pokemon ->
                PokemonCard(pokemon)
            }
        }
    }
}