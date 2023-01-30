package com.buffersolve.jutloader.presentation.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.buffersolve.jutloader.presentation.ui.input
import com.buffersolve.jutloader.presentation.ui.theme.JutloaderTheme
import com.buffersolve.jutloader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField() {

    val text = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val trailingIcon = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val error = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        value = text.value,
        leadingIcon = { Icon(Icons.Outlined.Search, null) },
        trailingIcon = { Icon(painterResource(id = R.drawable.outline_error), null) },
        label = { Text("Name of Anime") },
        onValueChange = {
            text.value = it
            input = it.text

        },
        singleLine = true,

        )
}

//fun isValidValue(): Boolean {
//
//    try {
//
//    }
//
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JutloaderTheme {
        TextField()
    }
}