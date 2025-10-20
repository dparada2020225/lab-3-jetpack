package com.uvg.mypokedex.navigation

sealed class AppScreen(val route: String) {
    object HomeScreen : AppScreen("home_screen")
    object DetailScreen : AppScreen("detail_screen/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "detail_screen/$pokemonId"
    }
    object SearchToolsDialog : AppScreen("search_tools_dialog")
}
