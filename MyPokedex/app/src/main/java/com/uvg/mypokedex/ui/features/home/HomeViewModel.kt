package com.uvg.mypokedex.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.data.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOrder {
    BY_NUMBER,
    BY_NAME_ASC,
    BY_NAME_DESC
}

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.BY_NUMBER
)

class HomeViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private var currentOffset = 0
    private val pageSize = 20
    private var isLoadingMore = false
    private var hasMoreData = true
    
    init {
        loadPokemonList()
    }
    
    fun loadPokemonList() {
        if (isLoadingMore || !hasMoreData) return
        
        isLoadingMore = true
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getPokemonList(pageSize, currentOffset).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        val newList = result.data
                        hasMoreData = newList.size == pageSize
                        
                        _uiState.update { currentState ->
                            val updatedList = (currentState.pokemonList + newList).distinctBy { it.id }
                            currentState.copy(
                                pokemonList = applySorting(updatedList, currentState.sortOrder),
                                isLoading = false,
                                error = null
                            )
                        }
                        
                        currentOffset += pageSize
                        isLoadingMore = false
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error desconocido al cargar Pokémon"
                            )
                        }
                        isLoadingMore = false
                    }
                }
            }
        }
    }
    
    fun searchPokemon(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
    
    fun setSortOrder(order: SortOrder) {
        _uiState.update { currentState ->
            currentState.copy(
                sortOrder = order,
                pokemonList = applySorting(currentState.pokemonList, order)
            )
        }
    }
    
    fun retryLoading() {
        _uiState.update { it.copy(error = null) }
        loadPokemonList()
    }
    
    private fun applySorting(list: List<Pokemon>, order: SortOrder): List<Pokemon> {
        return when (order) {
            SortOrder.BY_NUMBER -> list.sortedBy { it.id }
            SortOrder.BY_NAME_ASC -> list.sortedBy { it.name }
            SortOrder.BY_NAME_DESC -> list.sortedByDescending { it.name }
        }
    }
    
    fun getFilteredPokemon(): List<Pokemon> {
        val currentState = _uiState.value
        return if (currentState.searchQuery.isBlank()) {
            currentState.pokemonList
        } else {
            currentState.pokemonList.filter {
                it.name.contains(currentState.searchQuery, ignoreCase = true) ||
                        it.id.toString().contains(currentState.searchQuery)
            }
        }
    }
}