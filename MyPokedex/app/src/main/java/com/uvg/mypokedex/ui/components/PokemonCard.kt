package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon

@Composable
fun Card(
    modifier: Modifier = Modifier.padding(8.dp).fillMaxWidth(),
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    colors: CardColors = CardDefaults.cardColors(
    containerColor = Color.Gray
),
    pokemon: Pokemon
) {
    
}
