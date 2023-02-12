package com.buffersolve.jutloader.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.buffersolve.jutloader.R
import com.buffersolve.jutloader.presentation.ui.compose.ButtonSearch
import com.buffersolve.jutloader.presentation.ui.compose.GifImage
import com.buffersolve.jutloader.presentation.ui.theme.JutloaderTheme
import com.buffersolve.jutloader.presentation.ui.compose.TextField
import com.buffersolve.jutloader.presentation.ui.compose.dialog.NavigationDialog
import com.buffersolve.jutloader.presentation.ui.compose.landscape.PortraitScreenOnly
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

lateinit var webViewAgent: String

val SeriesSnapshotStateList = SnapshotStateList<String>()
val SeriesLinkSnapshotStateList = SnapshotStateList<String>()
val DialogState = SnapshotStateList<Boolean>()
val ExceptionState = SnapshotStateList<String>()
var input: String = ""

@AndroidEntryPoint
class JutLoaderActivity : ComponentActivity() {

    private val viewModel: JutLoaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JutloaderTheme {

                // Prevent Landscape
                PortraitScreenOnly()

                // Top App Bar && Card && ProgressBar
                BarCardProgress(
                    viewLifecycleOwner = this@JutLoaderActivity,
                    context = applicationContext,
                    viewModel = viewModel,
                    lifecycleScope = lifecycleScope
                )

                // Naruto Bottom
                GifImage()
            }
        }

        // UserAgent
        webViewAgent = WebView(this).settings.userAgentString

    }

}

// Compose UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarCardProgress(
    viewLifecycleOwner: LifecycleOwner,
    context: Context,
    viewModel: JutLoaderViewModel,
    lifecycleScope: LifecycleCoroutineScope,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "JutLoader")
                },
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
            Column {
                Card(
                    scaffoldPadding = it,
                    viewLifecycleOwner = viewLifecycleOwner,
                    context = context,
                    viewModel = viewModel,
                    lifecycleScope = lifecycleScope
                )

                val progressState = remember {
                    mutableStateOf(0L)
                }

                LaunchedEffect(key1 = 1) {
                    lifecycleScope.launch {
                        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.progress.collect {
                                progressState.value = it

                            }
                        }
                    }
                }
//                viewModel.progress.observe(viewLifecycleOwner) { progress ->
//                    progressState.value = progress
//                }
                // Progress
                DownloadProgressBar(
                    progressState.value * 0.01.toFloat()
                )
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(
    scaffoldPadding: PaddingValues,
    viewLifecycleOwner: LifecycleOwner,
    context: Context,
    viewModel: JutLoaderViewModel,
    lifecycleScope: LifecycleCoroutineScope
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
            .padding(top = 30.dp, start = 30.dp, end = 30.dp)
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
                viewModel = viewModel,
                lifecycleScope = lifecycleScope
            )

            // Dialog with choice
            NavigationDialog(
                viewLifecycleOwner = viewLifecycleOwner,
                userAgent = webViewAgent,
                context = context,
                viewModel = viewModel,
                lifecycleScope = lifecycleScope,
            )

            // CardInfo
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

        Row(Modifier.align(CenterHorizontally)) {

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
            .padding(30.dp)
            .fillMaxWidth()
            .size(70.dp.coerceAtLeast(0.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            Alignment.Center
        ) {
            Column {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = "Download Progress"
                )
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 40.dp, end = 40.dp)
                        .fillMaxWidth()
                        .alpha(0.8F),
                    progress = progress,
                    trackColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }

        }
    }
}

