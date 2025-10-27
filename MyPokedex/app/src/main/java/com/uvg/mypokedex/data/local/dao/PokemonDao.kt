package com.uvg.mypokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uvg.mypokedex.data.local.entity.CachedPokemon
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones con la tabla de Pokémon cacheados.
 * Todas las operaciones son asíncronas usando Flow o suspend functions.
 */
@Dao
interface PokemonDao {

    /**
     * Obtiene todos los Pokémon cacheados como un Flow.
     * El Flow se actualiza automáticamente cuando cambian los datos.
     */
    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<CachedPokemon>>

    /**
     * Obtiene un Pokémon específico por su ID.
     */
    @Query("SELECT * FROM cached_pokemon WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): CachedPokemon?

    /**
     * Inserta una lista de Pokémon. Si ya existe, reemplaza los datos.
     * OnConflictStrategy.REPLACE actualiza el registro existente.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<CachedPokemon>)

    /**
     * Inserta un solo Pokémon.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSinglePokemon(pokemon: CachedPokemon)

    /**
     * Elimina todos los Pokémon de la base de datos.
     * Útil para limpiar el caché cuando sea necesario.
     */
    @Query("DELETE FROM cached_pokemon")
    suspend fun deleteAllPokemon()

    /**
     * Obtiene el conteo total de Pokémon en caché.
     */
    @Query("SELECT COUNT(*) FROM cached_pokemon")
    suspend fun getPokemonCount(): Int

    /**
     * Obtiene Pokémon cuyo nombre contenga el texto buscado.
     * Útil para implementar búsqueda local.
     */
    @Query("SELECT * FROM cached_pokemon WHERE name LIKE '%' || :searchQuery || '%' ORDER BY id ASC")
    fun searchPokemon(searchQuery: String): Flow<List<CachedPokemon>>

    /**
     * Obtiene el timestamp más antiguo de actualización.
     * Útil para determinar si el caché está desactualizado.
     */
    @Query("SELECT MIN(lastFetchedAt) FROM cached_pokemon")
    suspend fun getOldestFetchTime(): Long?
}
