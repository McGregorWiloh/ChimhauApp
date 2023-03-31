package com.mcgregor.chimhauapp.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcgregor.chimhauapp.models.Product

@Composable
fun ProductsList(productList: MutableState<List<Product>>) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item { Text(text = "Products' List") }
            items(productList.value) {
                Row() {
                    Text(text = it.productName.toString(), modifier = Modifier.padding(end = 32.dp))
                    Text(text = it.productPrice.toString())
                }


            }
        }

    }

}