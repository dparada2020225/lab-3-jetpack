package com.uvg.mypokedex.data.local.converters

import androidx.room.TypeConverter
import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Stat
import org.json.JSONArray
import org.json.JSONObject

/**
 * Conversor para almacenar listas de PokeType en Room.
 * Room no puede almacenar listas directamente, por lo que las convertimos a JSON.
 */
class PokeTypeListConverter {

    @TypeConverter
    fun fromPokeTypeList(types: List<PokeType>): String {
        val jsonArray = JSONArray()
        types.forEach { type ->
            jsonArray.put(type.name)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toPokeTypeList(typesString: String): List<PokeType> {
        val types = mutableListOf<PokeType>()
        val jsonArray = JSONArray(typesString)

        for (i in 0 until jsonArray.length()) {
            val typeName = jsonArray.getString(i)
            types.add(PokeType.valueOf(typeName))
        }

        return types
    }
}

/**
 * Conversor para almacenar listas de Stat en Room.
 */
class StatListConverter {

    @TypeConverter
    fun fromStatList(stats: List<Stat>): String {
        val jsonArray = JSONArray()
        stats.forEach { stat ->
            val jsonObject = JSONObject()
            jsonObject.put("name", stat.name)
            jsonObject.put("value", stat.value.toDouble())
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toStatList(statsString: String): List<Stat> {
        val stats = mutableListOf<Stat>()
        val jsonArray = JSONArray(statsString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val value = jsonObject.getDouble("value").toFloat()
            stats.add(Stat(value = value, name = name))
        }

        return stats
    }
}
