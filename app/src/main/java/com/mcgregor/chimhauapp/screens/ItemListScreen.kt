package com.mcgregor.chimhauapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mcgregor.chimhauapp.models.Product
import com.mcgregor.chimhauapp.navigation.ChimhauScreens
import com.mcgregor.chimhauapp.viewmodels.ProductViewModel

@Composable
fun ItemListScreen(navController: NavController, viewModel: ProductViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val productsList = viewModel.products.collectAsState(initial = emptyList())
    val openDialog = remember { mutableStateOf(false) }
    val product = remember { mutableStateOf<Product>(Product("", "")) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 8.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Please select a product below to edit", fontWeight = FontWeight.Bold)
        }
        Divider(modifier = Modifier.padding(bottom = 8.dp))

        LazyColumn {
            items(productsList.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(10.dp),
                    elevation = 5.dp
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = it.productName, modifier = Modifier
                                .weight(0.4F)
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = "M${it.productPrice}/kg", modifier = Modifier
                                .weight(0.4F)
                                .padding(start = 20.dp)
                        )

                        IconButton(
                            //go to NewItemScreen passing in productId
                            onClick = {
                                val productId = product.value.id
                                navController.navigate(ChimhauScreens.NewItemScreen.name) },
                            modifier = Modifier
                                .weight(0.1F)
                                .padding(start = 5.dp, end = 15.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
                        }
                        IconButton(
                            onClick = {
                                openDialog.value = true
                                product.value = it
                            },
                            modifier = Modifier.weight(0.1F)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon"
                            )
                        }

                        if (openDialog.value) {

                            AlertDialog(
                                onDismissRequest = {
                                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                                    // button. If you want to disable that functionality, simply use an empty
                                    // onCloseRequest.
                                    openDialog.value = false
                                },
                                title = {
                                    Text(text = "Delete Item")
                                },
                                text = {
                                    Text("Are you sure you want to delete this item? ")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            openDialog.value = false
                                            viewModel.deleteProductFromDatabase(product.value)
                                            Toast.makeText(
                                                context,
                                                "Item deleted successfully!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            openDialog.value = false
                                        }) {
                                        Text("No")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}