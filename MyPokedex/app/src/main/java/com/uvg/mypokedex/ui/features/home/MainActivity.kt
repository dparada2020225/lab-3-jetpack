package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import com.uvg.mypokedex.ui.theme.MyPokedexTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokedexTheme {
                // Referencia del padding https://developer.android.com/develop/ui/compose/system/material-insets

                Scaffold { innerPadding ->
                    HomeScreen(
                        innerPadding
                    )
                }
            }
        }
    }
}