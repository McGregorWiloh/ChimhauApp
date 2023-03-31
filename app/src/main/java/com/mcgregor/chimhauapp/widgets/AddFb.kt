package com.mcgregor.chimhauapp.widgets

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mcgregor.chimhauapp.navigation.ChimhauScreens

//Floating action button for adding new transaction
@Composable
fun AddFb(navController: NavController) {
    FloatingActionButton(onClick = { navController.navigate(ChimhauScreens.ProductSelectionScreen.name) }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new transaction")
    }
}
