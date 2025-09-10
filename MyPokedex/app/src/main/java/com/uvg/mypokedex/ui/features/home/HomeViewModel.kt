package com.uvg.mypokedex.ui.features.home

import com.uvg.mypokedex.data.model.Pokemon

class HomeViewModel {
    private var currentPage: Int = 0

    init {
        loadMorePokemon()
    }

    fun getPageFileName(): String {
        var fileName: String
        val ending = currentPage + 1
        fileName = if (currentPage >= 0 && currentPage < 10) {
            "pokemon_0${currentPage}1_10.json"
        } else {
            "pokemon_${currentPage}1_${ending}0.json"
        }
        currentPage++
        return fileName
    }
    fun loadMorePokemon(fileName: String = getPageFileName()): List<Pokemon> {
        return listOf()
    }
}