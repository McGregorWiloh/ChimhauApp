package com.mcgregor.chimhauapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mcgregor.chimhauapp.models.Product
import com.mcgregor.chimhauapp.navigation.ChimhauScreens
import com.mcgregor.chimhauapp.viewmodels.ProductViewModel

@Composable
fun NewItemScreen(navController: NavController) {
    val viewModel: ProductViewModel = hiltViewModel()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val itemName = remember { mutableStateOf("") }
        val itemPrice = remember { mutableStateOf("") }

        Row {

            TextField(
                value = itemName.value,
                label = { Text(text = "Item Name") },
                onValueChange = {
                    itemName.value = it
                })
        }

        Row(modifier = Modifier.padding(top = 24.dp)) {

            TextField(value = itemPrice.value, label = { Text(text = "Price") }, onValueChange = {
                if (it.isEmpty()) {
                    itemPrice.value = it
                } else {
                    itemPrice.value = when (it.toDoubleOrNull()) {
                        null -> itemPrice.value
                        else -> it
                    }

                }
            },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Row(modifier = Modifier.padding(top = 32.dp), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                if (itemName.value.isNotEmpty() && itemPrice.value.isNotEmpty()) {
                    val newProduct = Product(itemName.value, itemPrice.value)
                    viewModel.saveProductToDatabase(newProduct)
                    navController.navigate(ChimhauScreens.MainScreen.name) {
                        navController.popBackStack()

                    }
                } else {
                    Toast.makeText(context, "Please enter item name and price", Toast.LENGTH_SHORT)
                        .show()
                }

            }) {
                Text(text = "Save Item")
            }
        }

    }

}

