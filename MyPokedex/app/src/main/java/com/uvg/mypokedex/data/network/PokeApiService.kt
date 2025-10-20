package com.uvg.mypokedex.data.network

import com.uvg.mypokedex.data.network.dto.PokemonDetailDto
import com.uvg.mypokedex.data.network.dto.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse
    
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto
    
    @GET("pokemon/{name}")
    suspend fun getPokemonDetailByName(
        @Path("name") name: String
    ): PokemonDetailDto
}