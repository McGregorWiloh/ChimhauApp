package com.mcgregor.chimhauapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mcgregor.chimhauapp.models.ProductTransaction
import com.mcgregor.chimhauapp.screens.*

@Composable
fun ChimhauNavigation() {
    val navController = rememberNavController()
    val selectedProductList = mutableListOf<ProductTransaction>()
    NavHost(
        navController = navController,
        startDestination = ChimhauScreens.ChimhauSplashScreen.name
    ) {
        composable(ChimhauScreens.ChimhauSplashScreen.name) {
            ChimhauSplashScreen(navController = navController)
        }

        composable(ChimhauScreens.MainScreen.name) {
            MainScreen(navController = navController, selectedProductList)
        }

        composable(
            ChimhauScreens.NewItemScreen.name) {
            NewItemScreen(navController = navController)
        }

        composable(ChimhauScreens.ProductSelectionScreen.name) {
            ProductSelectionScreen(navController, selectedProductList)
        }

        composable(ChimhauScreens.ItemListScreen.name) {
            ItemListScreen(navController = navController)
        }

    }
}