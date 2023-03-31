package com.mcgregor.chimhauapp.widgets

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mcgregor.chimhauapp.navigation.ChimhauScreens

@Composable
fun AppBar(navController: NavController) {
    TopAppBar(title = {
        Text(
            text = "Chimhau App",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }, actions = {
        val context = LocalContext.current
        val expanded = remember { mutableStateOf(false) }
        IconButton(onClick = { expanded.value = true }) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "more menu")
            TopAppBarDropDownMenu(navController, context, expanded)
        }
    })

}

@Composable
fun TopAppBarDropDownMenu(
    navController: NavController,
    context: Context,
    expanded: MutableState<Boolean>
) {
    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        Text(text = "Create New Item", modifier = Modifier
           .clickable { navController.navigate(ChimhauScreens.NewItemScreen.name) }
           .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
        )
        Text(text = "Edit Item", modifier = Modifier
           .clickable { navController.navigate(ChimhauScreens.ItemListScreen.name) }
           .padding(4.dp)
        )
    }
}
