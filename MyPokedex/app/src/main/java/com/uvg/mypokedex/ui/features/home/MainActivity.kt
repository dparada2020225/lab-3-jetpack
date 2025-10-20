package com.uvg.mypokedex.ui.features.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uvg.mypokedex.navigation.AppNavigation
import com.uvg.mypokedex.ui.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokedexTheme {
                AppNavigation()
            }
        }
    }
}
