package com.uvg.mypokedex.ui.features.home

import android.content.Context
import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat
import org.json.JSONArray
import java.nio.charset.Charset


class HomeViewModel(val context: Context) {
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

    fun loadMorePokemon(
        context: Context = this.context, fileName: String = getPageFileName()
    ): List<Pokemon> {
        val pokemonList = mutableListOf<Pokemon>()
        try {
            val inputStream = context.assets.open(fileName)
            val jsonString =
                inputStream.bufferedReader(Charset.defaultCharset()).use { it.readText() }
            val jsonArray = JSONArray(jsonString)

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
            e.printStackTrace()
            return emptyList()
        }
        return pokemonList
    }
}