package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    
    val filteredPokemon = remember(uiState.pokemonList, searchText) {
        if (searchText.isBlank()) {
            uiState.pokemonList
        } else {
            uiState.pokemonList.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.id.toString().contains(searchText)
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
            // Barra de búsqueda y opciones
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
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
                            text = { Text("Ordenar A-Z") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.BY_NAME_ASC)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar Z-A") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.BY_NAME_DESC)
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // Manejo de estados
            when {
                uiState.error != null && uiState.pokemonList.isEmpty() -> {
                    ErrorState(
                        message = uiState.error ?: "Error desconocido",
                        onRetry = { viewModel.retryLoading() }
                    )
                }
                uiState.isLoading && uiState.pokemonList.isEmpty() -> {
                    LoadingState()
                }
                filteredPokemon.isEmpty() && !uiState.isLoading -> {
                    EmptyState()
                }
                else -> {
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
                        
                        // Indicador de carga al final
                        if (uiState.isLoading && uiState.pokemonList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
            
            // Infinite scroll
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
                        if (!uiState.isLoading) {
                            viewModel.loadPokemonList()
                        }
                    }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando Pokémon...")
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No se encontraron Pokémon",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Intenta con otro término de búsqueda",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}