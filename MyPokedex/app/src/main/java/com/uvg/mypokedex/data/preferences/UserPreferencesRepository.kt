package com.uvg.mypokedex.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extensión para crear el DataStore de preferencias.
 * Se crea como propiedad delegada del Context.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

/**
 * Tipos de ordenamiento disponibles para la lista de Pokémon.
 */
enum class SortOrder {
    NUMBER_ASC,      // Por número ascendente (1, 2, 3...)
    NUMBER_DESC,     // Por número descendente (999, 998, 997...)
    NAME_ASC,        // Por nombre ascendente (A-Z)
    NAME_DESC;       // Por nombre descendente (Z-A)

    companion object {
        fun fromString(value: String): SortOrder {
            return try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                NUMBER_ASC // Valor por defecto
            }
        }
    }
}

/**
 * Repositorio para gestionar las preferencias del usuario usando DataStore.
 * Implementa operaciones de lectura y escritura de forma reactiva con Flow.
 */
class UserPreferencesRepository(private val context: Context) {

    companion object {
        // Key para almacenar el tipo de ordenamiento
        private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
    }

    /**
     * Flow que emite el tipo de ordenamiento actual.
     * Se actualiza automáticamente cuando cambia la preferencia.
     */
    val sortOrderFlow: Flow<SortOrder> = context.dataStore.data
        .map { preferences ->
            val sortOrderString = preferences[SORT_ORDER_KEY] ?: SortOrder.NUMBER_ASC.name
            SortOrder.fromString(sortOrderString)
        }

    /**
     * Guarda el nuevo tipo de ordenamiento en DataStore.
     * Es una operación suspend, debe llamarse desde una corrutina.
     */
    suspend fun saveSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder.name
        }
    }

    /**
     * Obtiene el tipo de ordenamiento actual de forma síncrona.
     * Útil para operaciones puntuales sin observación continua.
     */
    suspend fun getCurrentSortOrder(): SortOrder {
        val preferences = context.dataStore.data.map { it[SORT_ORDER_KEY] }
        return SortOrder.fromString(preferences.toString())
    }
}
