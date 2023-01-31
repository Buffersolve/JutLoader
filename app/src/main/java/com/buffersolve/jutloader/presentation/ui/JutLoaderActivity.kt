package com.buffersolve.jutloader.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import com.buffersolve.jutloader.R
import com.buffersolve.jutloader.presentation.ui.compose.ButtonSearch
import com.buffersolve.jutloader.presentation.ui.compose.GifImage
import com.buffersolve.jutloader.presentation.ui.theme.JutloaderTheme
import com.buffersolve.jutloader.presentation.ui.compose.TextField
import com.buffersolve.jutloader.presentation.ui.compose.dialog.NavigationDialog
import com.buffersolve.jutloader.presentation.ui.compose.landscape.Screen

lateinit var viewModel: JutLoaderViewModel
lateinit var webViewAgent: String

val SeriesSnapshotStateList = SnapshotStateList<String>()
val SeriesLinkSnapshotStateList = SnapshotStateList<String>()
val DialogState = SnapshotStateList<Boolean>()
val ExceptionState = SnapshotStateList<String>()
var input: String = ""

class JutLoaderActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            JutloaderTheme {

                // Prevent Landscape
                Screen()

                // Card animation & Arrow
                val expanded = remember { mutableStateOf(false) }

                val extraPadding by animateDpAsState(
                    if (expanded.value) 400.dp else 270.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                val arrowIcon = remember {
                    mutableStateOf(Icons.Outlined.KeyboardArrowDown)
                }

                // Top App Bar && Card
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "JutLoader") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        )
                    },
                    content = {

                        // Initial value for the dialog
                        DialogState.add(false)

                        // Content Card
                        Card(
                            modifier = Modifier
                                .padding(it)
                                .padding(30.dp)
                                .fillMaxWidth()
                                .size(extraPadding.coerceAtLeast(0.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            onClick = {
                                expanded.value = !expanded.value

                                when (expanded.value) {
                                    true -> arrowIcon.value = Icons.Outlined.KeyboardArrowUp
                                    false -> arrowIcon.value = Icons.Outlined.KeyboardArrowDown
                                }
                            },
                        ) {

                            Column(
                                modifier = Modifier
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.dp, end = 24.dp, start = 28.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween

                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.logo_jutsu),
                                        contentDescription = null,
                                        Modifier.size(60.dp)
                                    )
                                    Icon(
                                        arrowIcon.value,
                                        contentDescription = null,
                                        Modifier
                                            .size(30.dp)
                                    )
                                }

                                // Text Field
                                TextField()

                                // Btn
                                ButtonSearch(
                                    viewLifecycleOwner = this@JutLoaderActivity,
//                                    exceptionState = exceptionState,
                                )

                                // Dialog with choice
                                NavigationDialog(
                                    viewLifecycleOwner = this@JutLoaderActivity,
                                    userAgent = webViewAgent,
//                                    exceptionState = exceptionState,
                                    context = this@JutLoaderActivity,
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 26.dp, start = 20.dp, end = 20.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = "Just type the name of anime you want to download",
                                        textAlign = TextAlign.Center,
                                        fontSize = 17.sp
                                    )

                                    Divider(
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )

                                    Row(Modifier.align(Alignment.CenterHorizontally)) {

                                        Text(
                                            text = "Source:",
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(top = 12.dp)
                                        )

                                        TextButton(onClick = {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://jut.su")
                                            )
                                            startActivity(intent)
                                        }) {
                                            Text(
                                                text = "jut.su",
                                                fontSize = 20.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = "MIT License",
                                        fontSize = 10.sp,
                                        modifier = Modifier.align(Alignment.End)
                                    )


                                }
                            }
                        }
                    }
                )

                // Naruto Bottom
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    GifImage()
                }
            }
        }

        // UserAgent & ViewModel
        webViewAgent = WebView(this).settings.userAgentString

        val connectivityService = this.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityService is ConnectivityManager) {
            val viewModelProviderFactory =
                JutLoaderViewModelFactory(application, connectivityService)
            viewModel =
                ViewModelProvider(this, viewModelProviderFactory)[JutLoaderViewModel::class.java]
        } else {
            Toast.makeText(this, "Smth with connectivityService", Toast.LENGTH_LONG).show()
            Log.d("LOGConnectivityService", "Smth with connectivityService")
        }
    }
}

// Compose UI
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JutloaderTheme {
//        TextField()
    }
}

