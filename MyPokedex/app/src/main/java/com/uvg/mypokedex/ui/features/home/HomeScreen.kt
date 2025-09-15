package com.uvg.mypokedex.ui.features.home

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
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

fun orderPokemon(pokemonList: List<Pokemon>, order: Boolean?): List<Pokemon> {
    return when (order) {
        true -> {
            pokemonList.sortedBy {
                it.name
            }
        }

        false -> {
            pokemonList.sortedByDescending { it.name }
        }

        else -> {
            pokemonList
        }
    }
}

// Referencia: https://developer.android.com/develop/ui/compose/components/menu
@Composable
fun HomeScreen(
    paddingValues: PaddingValues, viewModel: HomeViewModel = HomeViewModel(LocalContext.current)
) {
    // Estado para mantener la posición del scroll
    val savedState = rememberLazyGridState()

    // Estado para mantener el orden de lista
    var ordered by rememberSaveable { mutableStateOf(false) }

    // Estado para controlar la visibilidad del menu
    var expanded by rememberSaveable { mutableStateOf(false) }

    // Texto para búqueda que persiste recomposiciones
    var searchText by rememberSaveable { mutableStateOf("") }

    // Lista mutable que contiene todos los Pokémon cargados
    val pokemonList = remember {
        mutableStateListOf<Pokemon>().apply {
            addAll(viewModel.loadMorePokemon())
        }
    }

    // Estado que calcula automáticamente la lista filtrada y ordenada
    val filteredAndOrdered by remember(searchText, pokemonList, ordered) {
        derivedStateOf {
            orderPokemon(filterPokemon(pokemonList, searchText), order = null)
        }
    }

    // Contenedor de la pantalla
    Column(
        modifier = Modifier.padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fila que contiene el campo de búsqueda y el botón de menú
        Row(modifier = Modifier.padding(20.dp)) {
            TextField(value = searchText, onValueChange = {
                searchText = it
            }, label = {
                Text("Filtrar por nombre")
            })
            // Contenedor para el botón flotante y su menú desplegable
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
                // Menú desplegable con opciones de ordenamiento
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

        // LazyGrid que muestra las tarjetas de Pokémon en 2 columnas
        LazyVerticalGrid(
            state = savedState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            columns = GridCells.Fixed(2)
        ) {
            items(
                items = filteredAndOrdered, key = { pokemon -> pokemon.id }) { pokemon ->
                PokemonCard(pokemon)
            }
        }

        // Efecto para implementar scroll infinito
        // Aquí si le pedí ayuda a la IA, pues le pregunte que llave podia utilizar y formas de implementarla para lograr mi proposito de que cargaran más pokemones al llegar al final.
        LaunchedEffect(savedState) {
            // Visualización de cambios en el Layout
            snapshotFlow { savedState.layoutInfo }.map { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val shouldLoad = totalItems > 0 && lastVisibleItemIndex >= totalItems - 4
                shouldLoad
            }.distinctUntilChanged().filter { it }.collect {
                val newPokemonBatch = viewModel.loadMorePokemon()
                if (newPokemonBatch.isNotEmpty()) {

                    // Cargar mas pokemones del ViewModel
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