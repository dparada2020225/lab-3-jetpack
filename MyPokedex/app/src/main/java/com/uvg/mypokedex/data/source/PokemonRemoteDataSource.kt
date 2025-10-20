package com.uvg.mypokedex.data.source

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.network.PokeApiService
import com.uvg.mypokedex.data.network.util.NetworkModule
import com.uvg.mypokedex.data.util.Result
import com.uvg.mypokedex.data.util.mapToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRemoteDataSource(
    private val apiService: PokeApiService = NetworkModule.providePokeApiService()
) {
    
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit, offset)
                
                // Obtener detalles de cada pokemon
                val pokemonList = mutableListOf<Pokemon>()
                
                for (item in response.results) {
                    // Extraer ID de la URL
                    val id = item.url.trimEnd('/').split("/").last().toInt()
                    val detailResult = getPokemonDetail(id)
                    
                    if (detailResult is Result.Success) {
                        pokemonList.add(detailResult.data)
                    }
                }
                
                Result.Success(pokemonList)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    suspend fun getPokemonDetail(id: Int): Result<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val detail = apiService.getPokemonDetail(id)
                Result.Success(detail.mapToDomain())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    suspend fun searchPokemonByName(name: String): Result<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val detail = apiService.getPokemonDetailByName(name.lowercase())
                Result.Success(detail.mapToDomain())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}