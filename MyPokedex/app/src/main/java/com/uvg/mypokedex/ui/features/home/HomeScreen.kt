package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uvg.mypokedex.navigation.AppScreen
import com.uvg.mypokedex.ui.components.PokemonCard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyGridState = rememberLazyGridState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    // Detectar cuando llegamos al final de la lista para cargar más
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo }
            .map { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                totalItems > 0 && lastVisibleItemIndex >= totalItems - 4
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                if (!uiState.isLoading && uiState.error == null) {
                    viewModel.loadPokemonList()
                }
            }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra de búsqueda y filtros
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.searchPokemon(it)
                    },
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
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Tools"
                    )
                }
                
                Box {
                    FloatingActionButton(
                        modifier = Modifier.padding(horizontal = 7.dp),
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Order list button"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ordenar por número") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.BY_NUMBER)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar por nombre (A-Z)") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.BY_NAME_ASC)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar por nombre (Z-A)") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.BY_NAME_DESC)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Contenido principal
            Box(modifier = Modifier.fillMaxSize()) {
                val filteredPokemon = viewModel.getFilteredPokemon()

                if (uiState.error != null && uiState.pokemonList.isEmpty()) {
                    // Estado de error (sin datos previos)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = { viewModel.retryLoading() }) {
                            Text("Reintentar")
                        }
                    }
                } else if (filteredPokemon.isEmpty() && !uiState.isLoading) {
                    // Estado vacío
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se encontraron Pokémon")
                    }
                } else {
                    // Lista de Pokémon
                    LazyVerticalGrid(
                        state = lazyGridState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(
                            items = filteredPokemon,
                            key = { pokemon -> pokemon.id }
                        ) { pokemon ->
                            PokemonCard(pokemon) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "pokemon",
                                    pokemon
                                )
                                navController.navigate(AppScreen.DetailScreen.createRoute(pokemon.id))
                            }
                        }
                    }

                    // Indicador de carga al final
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.BottomCenter),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}