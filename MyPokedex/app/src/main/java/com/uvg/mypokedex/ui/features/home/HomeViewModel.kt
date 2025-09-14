package com.uvg.mypokedex.ui.features.home

import android.content.Context
import android.util.JsonReader
import com.uvg.mypokedex.data.model.Pokemon
import java.io.InputStreamReader


class HomeViewModel (val context: Context) {
    private var currentPage: Int = 0
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
    fun loadMorePokemon(context: Context = this.context, fileName: String = getPageFileName()): List<Pokemon> {
        val inputStream = context.assets.open(fileName)
        val reader = JsonReader(InputStreamReader(inputStream))
        return listOf()

    }
}