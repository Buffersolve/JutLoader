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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.buffersolve.jutloader.R
import com.buffersolve.jutloader.data.contentprovider.DownloadProgressObserver
import com.buffersolve.jutloader.presentation.ui.compose.ButtonSearch
import com.buffersolve.jutloader.presentation.ui.compose.GifImage
import com.buffersolve.jutloader.presentation.ui.theme.JutloaderTheme
import com.buffersolve.jutloader.presentation.ui.compose.TextField
import com.buffersolve.jutloader.presentation.ui.compose.dialog.NavigationDialog
import com.buffersolve.jutloader.presentation.ui.compose.landscape.PortraitScreenOnly

lateinit var viewModel: JutLoaderViewModel
lateinit var webViewAgent: String

val SeriesSnapshotStateList = SnapshotStateList<String>()
val SeriesLinkSnapshotStateList = SnapshotStateList<String>()
val DialogState = SnapshotStateList<Boolean>()
val ExceptionState = SnapshotStateList<String>()
var input: String = ""

class JutLoaderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JutloaderTheme {

                // Prevent Landscape
                PortraitScreenOnly()

                // Top App Bar && Card
                TopAppBarAndCard(
                    this@JutLoaderActivity,
                    this
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

        // Connectivity Service
        val connectivityService = this.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityService is ConnectivityManager) {
            val viewModelProviderFactory =
                JutLoaderViewModelFactory(application, connectivityService)
            viewModel =
                ViewModelProvider(this, viewModelProviderFactory)[JutLoaderViewModel::class.java]
        } else {
            Toast.makeText(this, "Smth with connectivityService", Toast.LENGTH_LONG).show()
        }
    }

//    val downlo =
}

// Compose UI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarAndCard(
    viewLifecycleOwner: LifecycleOwner,
    context: Context
) {
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
                scaffoldPadding = it,
                viewLifecycleOwner = viewLifecycleOwner,
                context = context
            )

            val progressState = remember {
                mutableStateOf(0L)
            }
            viewModel.progress.observe(viewLifecycleOwner) { prgrs ->
                progressState.value = prgrs
                Log.d("LONGSTATE", prgrs.toFloat().toString())

            }
            // Progress
            DownloadProgressBar(
                progressState.value * 0.01.toFloat()
            )

//            LinearProgressIndicator(
//                modifier = Modifier.padding(top = 300.dp).fillMaxWidth(),
//                progress = progressState.value * 0.01.toFloat()
//            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    scaffoldPadding: PaddingValues,
    viewLifecycleOwner: LifecycleOwner,
    context: Context
) {

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

    Card(
        modifier = Modifier
            .padding(scaffoldPadding)
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

            // Icon & Arrow
            CardIconAndArrow(
                arrowIcon = arrowIcon
            )

            // Text Field
            TextField()

            // Btn
            ButtonSearch(
                viewLifecycleOwner = viewLifecycleOwner,
            )

            // Dialog with choice
            NavigationDialog(
                viewLifecycleOwner = viewLifecycleOwner,
                userAgent = webViewAgent,
                context = context,
            )

            CardInfo(
                context = context
            )
        }
    }
}

@Composable
fun CardIconAndArrow(
    arrowIcon: MutableState<ImageVector>
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
}

@Composable
fun CardInfo(
    context: Context
) {
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
                startActivity(context, intent, null)
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

@Composable
fun DownloadProgressBar(progress: Float) {

    Card(
        modifier = Modifier
//            .padding(scaffoldPadding)
            .padding(30.dp)
            .padding(top = 500.dp)
            .fillMaxWidth()
//            .size(extraPadding.coerceAtLeast(0.dp)),
            .size(65.dp.coerceAtLeast(0.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
//        if (progress in 0..99) {
//            CircularProgressIndicator(progress.toFloat(), modifier = Modifier.padding(8.dp))
//        } else { }
        Box(
            modifier = Modifier.fillMaxSize(),
            Alignment.Center
        ) {
            Column {
                Text(modifier = Modifier.align(CenterHorizontally), text = "Download Progress", )
                LinearProgressIndicator(
                    modifier = Modifier.padding(top = 10.dp),
                    progress = progress,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

