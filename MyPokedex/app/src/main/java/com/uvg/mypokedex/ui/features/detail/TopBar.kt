package com.uvg.mypokedex.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.theme.AppTypography

/* TopBar: La barra superior con la flecha para atrás, el nombre y el
corazón. ¿Será una buena idea que éste integre al corazón? ¿Qué sucede
si más adelante quiero usar el TopBar dónde no se usa el corazón? ¿Qué
sucede si el corazón lo quiero reusar incluyendo su comportamiento? */

// Referencia: https://developer.android.com/develop/ui/compose/components/icon-button

@Preview
@Composable
fun BookmarkPageHeart() {
    var isToggled by remember { mutableStateOf(false) }
    Box() {
        IconButton(
            onClick = { isToggled = !isToggled }
        ) {
            Icon(
                imageVector = if (isToggled) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isToggled) "Selected filled favorite button icon" else "Unselected outlined favorite button icon",
                tint = if (isToggled) Color.Red else if (isSystemInDarkTheme()) Color.White else Color.Black
             )
        }
    }
}

@Composable
fun nameTopBar(
    pokemon: Pokemon,
    withHeart: Boolean
) {
    Row() {
        IconButton(
            onClick = TODO()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text(
            text = "${pokemon.name}",
            style = AppTypography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}