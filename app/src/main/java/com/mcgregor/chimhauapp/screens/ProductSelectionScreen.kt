package com.mcgregor.chimhauapp.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mcgregor.chimhauapp.models.Product
import com.mcgregor.chimhauapp.models.ProductTransaction
import com.mcgregor.chimhauapp.navigation.ChimhauScreens
import com.mcgregor.chimhauapp.viewmodels.ProductViewModel

@Composable
fun ProductSelectionScreen(
    navController: NavController,
    selectedProductList: MutableList<ProductTransaction>,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val productList = viewModel.products.collectAsState(initial = null)
    val context = LocalContext.current
    val itemQuantity = rememberSaveable { mutableStateOf("") }
    val selectedProduct = remember { mutableStateOf(Product("", "")) }
    val isVisible = rememberSaveable { mutableStateOf(false) }
    val show = rememberSaveable { mutableStateOf(true) }

    val expanded = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (show.value) {
            Text(text = "Use the arrow below to select your product:")
            IconButton(
                onClick = { expanded.value = true },
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Drop down icon"
                )
                productList.value?.let { TopAppBarDropDownMenu(it, expanded, context, selectedProduct, isVisible, show) }
            }
        }


        MyText(selectedProduct, isVisible.value)

        Row() {

            TextField(
                value = itemQuantity.value,
                label = { Text(text = "Enter Quantity") },
                onValueChange = {
                    if (it.isEmpty()) {
                        itemQuantity.value = it
                    } else {
                        itemQuantity.value = when (it.toDoubleOrNull()) {
                            null -> itemQuantity.value
                            else -> it
                        }

                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Button(onClick = {
            if (itemQuantity.value.isNotEmpty() && selectedProduct.value.productName.isNotEmpty()) {
                createProductTransaction(
                    selectedProduct.value,
                    itemQuantity.value.toDouble(),
                    selectedProductList
                )
                navController.navigate(ChimhauScreens.MainScreen.name) {
                    navController.popBackStack()
                }
            } else {
                Toast.makeText(context, "Please enter all details", Toast.LENGTH_SHORT).show()
            }

        }, modifier = Modifier.padding(top = 40.dp)) {
            Text(text = "Save Transaction")
        }
    }
}

@Composable
fun TopAppBarDropDownMenu(
    list: List<Product>,
    expanded: MutableState<Boolean>,
    context: Context,
    selectedProduct: MutableState<Product>,
    isVisible: MutableState<Boolean>,
    show: MutableState<Boolean>
) {

    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        for (i in list) {
            Text(text = i.productName!!, modifier = Modifier
                .clickable {
                    selectedProduct.value = i
                    isVisible.value = true
                    show.value = false
                    expanded.value = false
                }
                .padding(start = 4.dp, end = 4.dp, bottom = 30.dp))
        }
    }
}

@Composable
fun MyText(selectedProduct: MutableState<Product>, isVisible: Boolean) {
    if (isVisible) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedProduct.value.productName,
                Modifier.padding(end = 10.dp),
                fontWeight = FontWeight.Bold
            )
            Text(text = "M${selectedProduct.value.productPrice}/kg")
        }

    }
}

fun createProductTransaction(
    product: Product,
    itemQuantity: Double,
    selectedProductList: MutableList<ProductTransaction>
) {
    val productTotalAmount = product.productPrice.toDouble().times(itemQuantity)
    val productTransaction = ProductTransaction(product, itemQuantity, productTotalAmount)
    selectedProductList.add(productTransaction)

}