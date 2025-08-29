package com.uvg.mypokedex.ui.features.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.Stat
import com.uvg.mypokedex.data.model.StatBar
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.theme.AppTypography

@Composable
fun StatRow(stat: Stat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.weight(1.2f),
            text = stat.name,
            textAlign = TextAlign.Center,
            style = AppTypography.bodyLarge
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${stat.number}",
            textAlign = TextAlign.Center,
            style = AppTypography.bodyLarge
        )
        stat.StatBar()
    }
}

@Composable
fun StatsRow(pokemon: Pokemon) {
    val stats = pokemon.stats
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatRow(stats.hp)
        StatRow(stats.attack)
        StatRow(stats.defense)
        StatRow(stats.specialAttack)
        StatRow(stats.specialDefense)
        StatRow(stats.speed)
    }
}

@Preview
@Composable
fun PreviewPokemonStatRow() {
    val pokemon = (HomeViewModel().getPokemonList())[1]
    StatsRow(pokemon)
}
