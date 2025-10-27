package com.uvg.mypokedex.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uvg.mypokedex.data.network.ConnectivityObserver
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.navigation.AppScreen
import com.uvg.mypokedex.ui.components.PokemonCard

/**
 * Pantalla principal de la Pokédex.
 * Muestra una lista de Pokémon con funcionalidades de:
 * - Filtrado por nombre
 * - Ordenamiento persistente
 * - Indicador de conectividad
 * - Cache-first loading
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = HomeViewModel(LocalContext.current)
) {
    // Observar estados del ViewModel
    val pokemonList by viewModel.pokemonList.collectAsState()
    val networkStatus by viewModel.networkStatus.collectAsState()
    val currentSortOrder by viewModel.currentSortOrder.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    // Estados locales de UI
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    // Filtrar Pokémon según búsqueda
    val filteredPokemon = remember(pokemonList, searchText) {
        if (searchText.isBlank()) {
            pokemonList
        } else {
            pokemonList.filter {
                it.name.contains(searchText, ignoreCase = true)
            }
        }
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra de estado de conexión
                NetworkStatusBar(networkStatus)

                // Barra de búsqueda y controles
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    onSearchToolsClick = {
                        navController.navigate(AppScreen.SearchToolsDialog.route)
                    },
                    currentSortOrder = currentSortOrder,
                    onSortOrderChange = { viewModel.changeSortOrder(it) },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                )

                // Contenido principal
                if (isLoading && pokemonList.isEmpty()) {
                    // Mostrar loading solo si no hay datos en caché
                    LoadingState()
                } else if (filteredPokemon.isEmpty() && searchText.isNotBlank()) {
                    // No hay resultados de búsqueda
                    EmptySearchState(searchText)
                } else if (filteredPokemon.isEmpty()) {
                    // No hay datos en absoluto
                    EmptyState(
                        networkStatus = networkStatus,
                        onRetry = { viewModel.refreshCache() }
                    )
                } else {
                    // Mostrar lista de Pokémon
                    PokemonGrid(
                        pokemon = filteredPokemon,
                        onPokemonClick = { pokemon ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "pokemon", pokemon
                            )
                            navController.navigate(
                                AppScreen.DetailScreen.createRoute(pokemon.id)
                            )
                        }
                    )
                }
            }

            // Snackbar para mensajes de estado
            statusMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    action = {
                        androidx.compose.material3.TextButton(
                            onClick = { viewModel.clearStatusMessage() }
                        ) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(message)
                }
            }
        }
    }
}

/**
 * Barra que muestra el estado de la conexión de red.
 */
@Composable
private fun NetworkStatusBar(status: ConnectivityObserver.Status) {
    val (backgroundColor, icon, text) = when (status) {
        ConnectivityObserver.Status.AVAILABLE -> Triple(
            Color(0xFF4CAF50),
            Icons.Default.Wifi,
            "Conectado"
        )
        ConnectivityObserver.Status.UNAVAILABLE,
        ConnectivityObserver.Status.LOST -> Triple(
            Color(0xFFF44336),
            Icons.Default.CloudOff,
            "Sin conexión - Modo offline"
        )
        ConnectivityObserver.Status.LOSING -> Triple(
            Color(0xFFFF9800),
            Icons.Default.CloudOff,
            "Perdiendo conexión..."
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

/**
 * Barra de búsqueda con controles de ordenamiento.
 */
@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchToolsClick: () -> Unit,
    currentSortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text("Filtrar por nombre") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small,
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = onSearchToolsClick,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Herramientas de búsqueda"
            )
        }

        Box {
            FloatingActionButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { onExpandedChange(!expanded) }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menú de ordenamiento"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Por número ↑" + if (currentSortOrder == SortOrder.NUMBER_ASC) " ✓" else ""
                        )
                    },
                    onClick = {
                        onSortOrderChange(SortOrder.NUMBER_ASC)
                        onExpandedChange(false)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Por número ↓" + if (currentSortOrder == SortOrder.NUMBER_DESC) " ✓" else ""
                        )
                    },
                    onClick = {
                        onSortOrderChange(SortOrder.NUMBER_DESC)
                        onExpandedChange(false)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Por nombre A-Z" + if (currentSortOrder == SortOrder.NAME_ASC) " ✓" else ""
                        )
                    },
                    onClick = {
                        onSortOrderChange(SortOrder.NAME_ASC)
                        onExpandedChange(false)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Por nombre Z-A" + if (currentSortOrder == SortOrder.NAME_DESC) " ✓" else ""
                        )
                    },
                    onClick = {
                        onSortOrderChange(SortOrder.NAME_DESC)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

/**
 * Grid de Pokémon.
 */
@Composable
private fun PokemonGrid(
    pokemon: List<com.uvg.mypokedex.data.model.Pokemon>,
    onPokemonClick: (com.uvg.mypokedex.data.model.Pokemon) -> Unit
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(
            items = pokemon,
            key = { it.id }
        ) { poke ->
            PokemonCard(pokemon = poke, onClick = { onPokemonClick(poke) })
        }
    }
}

/**
 * Estado de carga.
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.padding(16.dp))
            Text("Cargando Pokémon...")
        }
    }
}

/**
 * Estado vacío cuando no hay Pokémon.
 */
@Composable
private fun EmptyState(
    networkStatus: ConnectivityObserver.Status,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = if (networkStatus == ConnectivityObserver.Status.AVAILABLE) {
                    "No hay Pokémon guardados"
                } else {
                    "Sin conexión y sin datos guardados"
                },
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            if (networkStatus == ConnectivityObserver.Status.AVAILABLE) {
                androidx.compose.material3.Button(onClick = onRetry) {
                    Text("Cargar datos")
                }
            }
        }
    }
}

/**
 * Estado cuando no hay resultados de búsqueda.
 */
@Composable
private fun EmptySearchState(searchText: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "No se encontraron Pokémon",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "con el nombre \"$searchText\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
