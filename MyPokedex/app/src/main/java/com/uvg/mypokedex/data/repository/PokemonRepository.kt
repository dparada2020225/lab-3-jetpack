package com.uvg.mypokedex.data.repository

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.source.PokemonRemoteDataSource
import com.uvg.mypokedex.data.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepository(
    private val remoteDataSource: PokemonRemoteDataSource = PokemonRemoteDataSource()
) {
    
    fun getPokemonList(limit: Int, offset: Int): Flow<Result<List<Pokemon>>> = flow {
        emit(Result.Loading)
        
        try {
            val result = remoteDataSource.getPokemonList(limit, offset)
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    fun getPokemonDetail(id: Int): Flow<Result<Pokemon>> = flow {
        emit(Result.Loading)
        
        try {
            val result = remoteDataSource.getPokemonDetail(id)
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    fun searchPokemonByName(name: String): Flow<Result<Pokemon>> = flow {
        emit(Result.Loading)
        
        try {
            val result = remoteDataSource.searchPokemonByName(name)
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}