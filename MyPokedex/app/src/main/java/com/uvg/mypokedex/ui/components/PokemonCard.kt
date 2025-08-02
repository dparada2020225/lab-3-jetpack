package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.uvg.mypokedex.data.model.*
import com.uvg.mypokedex.ui.theme.AppTypography
import com.uvg.mypokedex.ui.theme.MyPokedexTheme


// Los datos son de https://www.pokemon.com/us/pokedex/ditto
val testPokemon = Pokemon(
    id = 132,
    name = "Ditto",
    height = 30.48,
    weight = 8.8,
    category = "Transform",
    abilities = "Limber",
    gender = "Unknown",
    type = PokeType.NORMAL,
    weaknesses = PokeType.FIGHTING
)

@Preview
@Composable
fun PokemonCardPreview() {
    PokemonCard(testPokemon)
}

fun getTypeColor(pokemon: Pokemon): Color {
    when (pokemon.type) {
        PokeType.BUG -> return bugColor
        PokeType.DRAGON -> return dragonColor
        PokeType.FAIRY -> return fairyColor
        PokeType.FIRE -> return fireColor
        PokeType.GHOST -> return ghostColor
        PokeType.GROUND -> return groundColor
        PokeType.NORMAL -> return normalColor
        PokeType.PSYCHIC -> return psychicColor
        PokeType.STEEL -> return steelColor
        PokeType.DARK -> return darkColor
        PokeType.ELECTRIC -> return electricColor
        PokeType.FIGHTING -> return fightingColor
        PokeType.FLYING -> return flyingColor
        PokeType.GRASS -> return grassColor
        PokeType.ICE -> return iceColor
        PokeType.POISON -> return poisonColor
        PokeType.ROCK -> return rockColor
        PokeType.WATER -> return waterColor
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon) {
    val typeColor = getTypeColor(pokemon)
    MyPokedexTheme {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pokemon.name,
                        style = AppTypography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "#0${pokemon.id}",
                        style = AppTypography.headlineSmall
                    )
                }
                AsyncImage(
                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/refs/heads/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp)
                )
                Surface(
                    modifier = Modifier.padding(5.dp),
                    shape = MaterialTheme.shapes.small,
                    color = typeColor
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "${pokemon.type}",
                        style = AppTypography.bodyLarge
                    )
                }
                Surface(
                    modifier = Modifier.padding(10.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Height: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text("${pokemon.height} cm", style = AppTypography.bodyMedium)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Weight: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text("${pokemon.weight} lbs", style = AppTypography.bodyMedium)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Gender: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(pokemon.gender, style = AppTypography.bodyMedium)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Category: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(pokemon.category, style = AppTypography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Abilities: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(pokemon.abilities, style = AppTypography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Weaknesses: ",
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text("${pokemon.weaknesses}", style = AppTypography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
