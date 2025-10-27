package com.uvg.mypokedex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uvg.mypokedex.data.local.converters.PokeTypeListConverter
import com.uvg.mypokedex.data.local.converters.StatListConverter
import com.uvg.mypokedex.data.local.dao.PokemonDao
import com.uvg.mypokedex.data.local.entity.CachedPokemon

/**
 * Base de datos Room para almacenamiento local de Pokémon.
 * Implementa el patrón Singleton para asegurar una sola instancia.
 */
@Database(
    entities = [CachedPokemon::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PokeTypeListConverter::class, StatListConverter::class)
abstract class PokemonDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de Pokémon.
     */
    abstract fun pokemonDao(): PokemonDao

    companion object {
        // Volatile asegura que el valor de INSTANCE sea visible
        // inmediatamente para todos los threads
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos.
         * Si no existe, la crea de forma thread-safe.
         */
        fun getDatabase(context: Context): PokemonDatabase {
            // Si INSTANCE no es null, retornarla
            // Si es null, crear la base de datos
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                )
                    // Estrategia de migración destructiva para desarrollo
                    // En producción, deberías crear migraciones adecuadas
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
