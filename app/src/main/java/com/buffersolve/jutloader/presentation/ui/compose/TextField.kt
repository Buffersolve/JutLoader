package com.buffersolve.jutloader.presentation.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.buffersolve.jutloader.presentation.ui.input

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField() {

    val text = remember {
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
        trailingIcon = {
            if (text.value.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        text.value = TextFieldValue("")
                        input = ""
                    },
                ) { Icon(Icons.Outlined.Clear, null) }
            }
        },
        label = {
            if (!text.value.text.isValid()) {
//                Text("Incorrect")
                Text("Name of Anime")
                error.value = true
            } else {
                Text("Name of Anime")
                error.value = false
            }
        },
        onValueChange = {
            text.value = it
            input = it.text
        },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        isError = error.value,
        )
}

// Validation
fun String.isValid(): Boolean {
//    return this.isNotEmpty() && this.length > 1
    return this.length != 1
}