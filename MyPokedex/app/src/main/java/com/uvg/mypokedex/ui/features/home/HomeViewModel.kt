package com.uvg.mypokedex.ui.features.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat
import com.uvg.mypokedex.data.network.ConnectivityObserver
import com.uvg.mypokedex.data.preferences.SortOrder
import com.uvg.mypokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset

/**
 * ViewModel para la pantalla principal (Home).
 * Gestiona el estado de la UI y coordina las operaciones del Repository.
 * Sigue el patrón MVVM: la UI observa StateFlows, el ViewModel gestiona lógica.
 */
class HomeViewModel(val context: Context) : ViewModel() {

    // Repository centralizado para todas las operaciones de datos
    private val repository = PokemonRepository(context)

    // Estado privado mutable para el progreso de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mostrar mensajes al usuario
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Página actual para la carga legacy (archivos JSON)
    private var currentPage: Int = 0

    /**
     * StateFlow con la lista de Pokémon ordenada.
     * Se actualiza automáticamente desde el Repository.
     * stateIn convierte el Flow frío del Repository en un StateFlow caliente.
     */
    val pokemonList: StateFlow<List<Pokemon>> = repository.pokemonListFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * StateFlow con el estado de conectividad.
     * La UI puede observarlo para mostrar indicadores de conexión.
     */
    val networkStatus: StateFlow<ConnectivityObserver.Status> =
        repository.networkStatus
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ConnectivityObserver.Status.UNAVAILABLE
            )

    /**
     * StateFlow con el tipo de ordenamiento actual.
     */
    val currentSortOrder: StateFlow<SortOrder> =
        repository.currentSortOrder
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SortOrder.NUMBER_ASC
            )

    init {
        // Inicializar carga de datos
        initializeData()

        // Observar cambios de conectividad para refrescar caché
        observeNetworkChanges()
    }

    /**
     * Inicializa los datos al crear el ViewModel.
     * Si no hay datos en caché, carga desde archivos JSON.
     */
    private fun initializeData() {
        viewModelScope.launch {
            try {
                val cachedCount = repository.getCachedPokemonCount()

                if (cachedCount == 0) {
                    // Primera vez: cargar datos iniciales
                    Log.d(TAG, "Caché vacío, cargando datos iniciales...")
                    _statusMessage.value = "Cargando datos por primera vez..."
                    repository.refreshPokemonCache()
                    _statusMessage.value = null
                } else {
                    Log.d(TAG, "Caché existente con $cachedCount Pokémon")

                    // Verificar si el caché está desactualizado
                    if (repository.isCacheStale()) {
                        Log.d(TAG, "Caché desactualizado, marcando para actualización")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error inicializando datos: ${e.message}", e)
                _statusMessage.value = "Error cargando datos: ${e.message}"
            }
        }
    }

    /**
     * Observa cambios en la conectividad de red.
     * Cuando se recupera conexión, actualiza el caché automáticamente.
     */
    private fun observeNetworkChanges() {
        viewModelScope.launch {
            networkStatus.collect { status ->
                when (status) {
                    ConnectivityObserver.Status.AVAILABLE -> {
                        Log.d(TAG, "Conexión disponible")
                        // Actualizar caché si está desactualizado
                        if (repository.isCacheStale()) {
                            refreshCache()
                        }
                    }
                    ConnectivityObserver.Status.LOST,
                    ConnectivityObserver.Status.UNAVAILABLE -> {
                        Log.d(TAG, "Conexión perdida, mostrando datos en caché")
                        _statusMessage.value = "Sin conexión - Mostrando datos guardados"
                    }
                    else -> { /* No hacer nada para LOSING */ }
                }
            }
        }
    }

    /**
     * Cambia el tipo de ordenamiento y lo persiste.
     * El cambio se refleja automáticamente en la UI gracias a los Flows.
     */
    fun changeSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Cambiando orden a: $sortOrder")
                repository.saveSortOrder(sortOrder)
                _statusMessage.value = "Ordenamiento actualizado"

                // Limpiar mensaje después de 2 segundos
                kotlinx.coroutines.delay(2000)
                _statusMessage.value = null
            } catch (e: Exception) {
                Log.e(TAG, "Error cambiando orden: ${e.message}", e)
                _statusMessage.value = "Error al cambiar ordenamiento"
            }
        }
    }

    /**
     * Fuerza una actualización del caché desde la fuente de datos.
     * Útil para implementar pull-to-refresh.
     */
    fun refreshCache() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _statusMessage.value = "Actualizando datos..."

                repository.refreshPokemonCache()

                _statusMessage.value = "Datos actualizados"
                kotlinx.coroutines.delay(2000)
                _statusMessage.value = null
            } catch (e: Exception) {
                Log.e(TAG, "Error actualizando caché: ${e.message}", e)
                _statusMessage.value = "Error al actualizar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Busca Pokémon por nombre en el caché local.
     */
    fun searchPokemon(query: String): StateFlow<List<Pokemon>> {
        return repository.searchPokemon(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Limpia el mensaje de estado.
     */
    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    // ========== MÉTODOS LEGACY PARA CARGA DESDE JSON ==========
    // Estos métodos se mantienen para compatibilidad con el código existente

    private fun getPageFileName(): String {
        val fileName: String
        val ending = currentPage + 1
        fileName = if (currentPage >= 0 && currentPage < 9) {
            "pokemon_0${currentPage}1_0${ending}0.json"
        } else if (currentPage == 9) {
            "pokemon_0${currentPage}1_${ending}0.json"
        } else {
            "pokemon_${currentPage}1_${ending}0.json"
        }
        currentPage++
        return fileName
    }

    fun loadMorePokemon(fileName: String = getPageFileName()): List<Pokemon> {
        val pokemonList = mutableListOf<Pokemon>()
        try {
            val inputStream = context.assets.open(fileName)
            val jsonString = inputStream.bufferedReader(Charset.defaultCharset()).use { it.readText() }

            val jsonOriginalObject = JSONObject(jsonString)
            val jsonArray = jsonOriginalObject.getJSONArray("items")

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val name = jsonObject.getString("name")
                val weight = jsonObject.getDouble("weight").toFloat()
                val height = jsonObject.getDouble("height").toFloat()

                val statsList = mutableListOf<Stat>()
                val statsJsonArray = jsonObject.getJSONArray("stats")
                for (j in 0 until statsJsonArray.length()) {
                    val statObject = statsJsonArray.getJSONObject(j)
                    val value = statObject.getDouble("value").toFloat()
                    val statName = statObject.getString("name")
                    statsList.add(Stat(value, statName))
                }

                val typeList = mutableListOf<PokeType>()
                val typeJsonArray = jsonObject.getJSONArray("type")
                for (k in 0 until typeJsonArray.length()) {
                    val typeString = typeJsonArray.getString(k)
                    typeList.add(PokeType.typeFromString(typeString))
                }

                val pokemon = Pokemon(
                    id = id,
                    name = name,
                    weight = weight,
                    height = height,
                    stats = statsList,
                    type = typeList
                )
                pokemonList.add(pokemon)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cargando Pokémon desde JSON: ${e.message}", e)
            return emptyList()
        }
        return pokemonList
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}