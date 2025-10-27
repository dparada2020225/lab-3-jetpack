package com.uvg.mypokedex.data.repository

import android.content.Context
import android.util.Log
import com.uvg.mypokedex.data.local.PokemonDatabase
import com.uvg.mypokedex.data.local.entity.CachedPokemon
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.network.ConnectivityObserver
import com.uvg.mypokedex.data.network.NetworkConnectivityObserver
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.data.preferences.UserPreferencesRepository
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio centralizado para gestionar datos de Pokémon.
 * Implementa estrategia cache-first: siempre muestra datos locales,
 * y actualiza desde la red cuando está disponible.
 */
class PokemonRepository(private val context: Context) {

    private val database = PokemonDatabase.getDatabase(context)
    private val pokemonDao = database.pokemonDao()
    private val preferencesRepository = UserPreferencesRepository(context)
    private val connectivityObserver = NetworkConnectivityObserver(context)
    private val viewModel = HomeViewModel(context)

    companion object {
        private const val TAG = "PokemonRepository"
        private const val CACHE_EXPIRATION_TIME = 24 * 60 * 60 * 1000L // 24 horas
    }

    /**
     * Flow combinado que emite la lista de Pokémon ordenada según preferencias.
     * Se actualiza automáticamente cuando cambian los datos o las preferencias.
     */
    val pokemonListFlow: Flow<List<Pokemon>> = combine(
        pokemonDao.getAllPokemon(),
        preferencesRepository.sortOrderFlow
    ) { cachedPokemon, sortOrder ->
        // Convertir entidades de caché a modelos de dominio
        val pokemonList = cachedPokemon.map { it.toDomainModel() }

        // Aplicar ordenamiento según preferencia
        when (sortOrder) {
            SortOrder.NUMBER_ASC -> pokemonList.sortedBy { it.id }
            SortOrder.NUMBER_DESC -> pokemonList.sortedByDescending { it.id }
            SortOrder.NAME_ASC -> pokemonList.sortedBy { it.name.lowercase() }
            SortOrder.NAME_DESC -> pokemonList.sortedByDescending { it.name.lowercase() }
        }
    }

    /**
     * Flow que emite el estado de conectividad actual.
     */
    val networkStatus: Flow<ConnectivityObserver.Status> =
        connectivityObserver.observe()

    /**
     * Obtiene el tipo de ordenamiento actual.
     */
    val currentSortOrder: Flow<SortOrder> =
        preferencesRepository.sortOrderFlow

    /**
     * Guarda una nueva preferencia de ordenamiento.
     */
    suspend fun saveSortOrder(sortOrder: SortOrder) {
        Log.d(TAG, "Guardando nuevo orden: $sortOrder")
        preferencesRepository.saveSortOrder(sortOrder)
    }

    /**
     * Carga Pokémon desde la fuente de datos local (archivos JSON)
     * y los guarda en la base de datos.
     * Esta función debe llamarse cuando hay conexión disponible.
     */
    suspend fun refreshPokemonCache() {
        try {
            Log.d(TAG, "Iniciando actualización de caché...")

            // Verificar si hay conexión
            if (!connectivityObserver.isCurrentlyConnected()) {
                Log.d(TAG, "Sin conexión, cancelando actualización")
                return
            }

            // Cargar Pokémon desde los archivos JSON (simulando API)
            val pokemonList = mutableListOf<Pokemon>()
            var hasMore = true

            // Cargar múltiples páginas
            for (page in 0..14) { // Cargar 15 páginas (150 Pokémon)
                try {
                    val batch = viewModel.loadMorePokemon()
                    if (batch.isEmpty()) {
                        hasMore = false
                        break
                    }
                    pokemonList.addAll(batch)
                } catch (e: Exception) {
                    Log.e(TAG, "Error cargando página $page: ${e.message}")
                    break
                }
            }

            // Convertir a entidades de caché y guardar
            if (pokemonList.isNotEmpty()) {
                val cachedEntities = pokemonList.map {
                    CachedPokemon.fromDomainModel(it)
                }
                pokemonDao.insertPokemon(cachedEntities)
                Log.d(TAG, "Caché actualizado con ${cachedEntities.size} Pokémon")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando caché: ${e.message}", e)
        }
    }

    /**
     * Verifica si el caché necesita actualizarse.
     */
    suspend fun isCacheStale(): Boolean {
        val oldestFetchTime = pokemonDao.getOldestFetchTime() ?: return true
        val currentTime = System.currentTimeMillis()
        return (currentTime - oldestFetchTime) > CACHE_EXPIRATION_TIME
    }

    /**
     * Obtiene la cantidad de Pokémon en caché.
     */
    suspend fun getCachedPokemonCount(): Int {
        return pokemonDao.getPokemonCount()
    }

    /**
     * Busca Pokémon por nombre en el caché local.
     */
    fun searchPokemon(query: String): Flow<List<Pokemon>> {
        return pokemonDao.searchPokemon(query)
            .map { cachedList ->
                cachedList.map { it.toDomainModel() }
            }
    }

    /**
     * Limpia toda la base de datos local.
     * Útil para testing o reset de la app.
     */
    suspend fun clearCache() {
        Log.d(TAG, "Limpiando caché completo")
        pokemonDao.deleteAllPokemon()
    }
}
