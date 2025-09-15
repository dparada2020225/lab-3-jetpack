package com.uvg.mypokedex.ui.features.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.navigation.AppScreen
import com.uvg.mypokedex.ui.components.PokemonCard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

fun filterPokemon(pokemonList: List<Pokemon>, searchText: String): List<Pokemon> {
    return if (searchText.isBlank()) {
        pokemonList
    } else {
        pokemonList.filter { it.name.contains(searchText, true) }
    }
}

fun orderPokemon(pokemonList: List<Pokemon>, order: String): List<Pokemon> {
    return when (order) {
        "true" -> {
            pokemonList.sortedBy { it.name }
        }

        "false" -> {
            pokemonList.sortedByDescending { it.name }
        }

        else -> {
            pokemonList
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = HomeViewModel(LocalContext.current)
) {
    val lazyGridState = rememberLazyGridState()
    var ordered by rememberSaveable { mutableStateOf("null") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    val pokemonList = rememberSaveable {
        mutableStateListOf<Pokemon>().apply {
            val initialPokemon = viewModel.loadMorePokemon()
            addAll(initialPokemon.distinctBy { it.id })
        }
    }

    val filteredAndOrdered by rememberSaveable(searchText, pokemonList, ordered) {
        mutableStateOf(orderPokemon(filterPokemon(pokemonList, searchText), order = ordered))
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Filtrar por nombre") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.small
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreen.SearchToolsDialog.route) },
                    modifier = Modifier.padding(horizontal = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "Search Tools"
                    )
                }
                Box {
                    FloatingActionButton(
                        modifier = Modifier.padding(horizontal = 7.dp),
                        onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Order list button"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Ordenar descendentemente") },
                            onClick = { ordered = "false"; expanded = false })
                        DropdownMenuItem(
                            text = { Text("Ordenar ascendentemente") },
                            onClick = { ordered = "true"; expanded = false })
                    }
                }
            }

            LazyVerticalGrid(
                state = lazyGridState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                columns = GridCells.Fixed(2)
            ) {
                items(
                    items = filteredAndOrdered, key = { pokemon -> pokemon.id }) { pokemon ->
                    PokemonCard(pokemon) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "pokemon", pokemon
                        )
                        navController.navigate(AppScreen.DetailScreen.createRoute(pokemon.id))
                    }
                }
            }

            LaunchedEffect(lazyGridState) {
                snapshotFlow { lazyGridState.layoutInfo }.map { layoutInfo ->
                    val totalItems = layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                    val shouldLoad = totalItems > 0 && lastVisibleItemIndex >= totalItems - 4
                    shouldLoad
                }.distinctUntilChanged().filter { it }.collect {
                    val newPokemonBatch = viewModel.loadMorePokemon()
                    if (newPokemonBatch.isNotEmpty()) {

                        // Aqui le pedi ayuda a la IA por que me daba el problema de al ordenar la lista muchas veces seguidas, se daba un ID repetido. Me recomendo hacer un set.

                        val existingIds = pokemonList.map { it.id }.toSet()
                        val uniqueNewPokemon =
                            newPokemonBatch.filter { it.id !in existingIds }.distinctBy { it.id }

                        if (uniqueNewPokemon.isNotEmpty()) {
                            pokemonList.addAll(uniqueNewPokemon)
                        } else {
                            Log.d(
                                "HomeScreen",
                                "Error when loading new pokemon viewModel.loadMorePokemon()"
                            )
                        }
                    } else {
                        Log.d(
                            "HomeScreen", "viewModel.loadMorePokemon() returned empty."
                        )
                    }
                }
            }
        }
    }
}
