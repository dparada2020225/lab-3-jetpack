package com.uvg.mypokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.ui.features.detail.DetailScreen
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.search.SearchToolsDialog

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreen.HomeScreen.route) {
        composable(AppScreen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = AppScreen.DetailScreen.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemon: Pokemon? =
                navController.previousBackStackEntry?.savedStateHandle?.get("pokemon")
            if (pokemon != null) {
                DetailScreen(pokemon = pokemon, navController = navController)
            }
        }
        dialog(AppScreen.SearchToolsDialog.route) {
            SearchToolsDialog(navController = navController)
        }
    }
}
