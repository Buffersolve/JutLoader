package com.buffersolve.jutloader.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.buffersolve.jutloader.R
import com.buffersolve.jutloader.data.parser.getmethods.GetResolution
import com.buffersolve.jutloader.presentation.ui.theme.JutloaderTheme

lateinit var viewModel: MainActivityViewModel
lateinit var viewModelUse: JutLoaderViewModel
lateinit var webViewAgent: String

val SeriesSnapshotStateList = SnapshotStateList<String>()
val SeriesLinkSnapshotStateList = SnapshotStateList<String>()
var input: String = ""

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            JutloaderTheme {
                // Prevent Landscape
                Screen()

                val arrowIcon = remember {
                    mutableStateOf(Icons.Outlined.KeyboardArrowDown)
                }

                val expanded = remember { mutableStateOf(false) }

                val extraPadding by animateDpAsState(
                    if (expanded.value) 400.dp else 270.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

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

                        Card(
                            modifier = Modifier
                                .padding(it)
                                .padding(30.dp)
                                .fillMaxWidth()
                                .size(extraPadding.coerceAtLeast(0.dp)),
//                                .padding(bottom = extraPadding.coerceAtLeast(0.dp)),
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
//                                            .align(Alignment.CenterHorizontally)

                                    )
                                }

                                TextField()
                                NavigationDialog(
                                    this@MainActivity,
                                    webViewAgent,
                                    this@MainActivity,
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
//                                    Spacer(modifier = Modifier.size(12.dp))

                                    Divider(
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        thickness = 1.dp,
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        GifImage()
                    }
            }
        }
        webViewAgent = WebView(this).settings.userAgentString
        Log.d("AGENT", webViewAgent)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModelUse = ViewModelProvider(this)[JutLoaderViewModel::class.java]

        ////
//        Thread {
//            GetResolution().execute("https://jut.su/berserk/season-4/episode-1.html", userAgent = webViewAgent)
//        }.start()


    }

}

//////

// Compose UI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField() {

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        value = text,
        label = { Text("Type the name of Anime") },
        onValueChange = {
            text = it
            input = it.text
        }
    )
}

@Composable
fun NavigationDialog(
    viewLifecycleOwner: LifecycleOwner,
    userAgent: String,
    context: Context
) {

    // Lists
    val seasonList = remember {
        mutableStateOf(listOf<String>())
    }
    viewModel.season.observe(viewLifecycleOwner) {
        seasonList.value = it.season
    }

    val seasonLink = remember {
        mutableStateOf(listOf<String>())
    }
    viewModel.season.observe(viewLifecycleOwner) {
        seasonLink.value = it.seasonLink
    }

    // Series
    val seriesList = remember {
        mutableStateOf(listOf<String>())
    }
    viewModel.seria.observe(viewLifecycleOwner) {
        seriesList.value = it.seria
//        seriesList.value = it.seriaLink

        Log.d("SERIES", seriesList.value.toString())
    }

    val seriesLink = remember {
        mutableStateOf(listOf<String>())
    }
    viewModel.seria.observe(viewLifecycleOwner) {
        seriesLink.value = it.seriaLink
        Log.d("SERIESLINK", seriesLink.value.toString())
    }

    val dialogShown = remember {
        mutableStateOf(false)
    }

    ///
    //Clean
//    val seasonListUse = remember {
//        mutableStateOf(listOf<String>())
//    }
//    viewModelUse.season.observe(viewLifecycleOwner) {
//        seasonListUse.value = it.season
//        Log.d("CASE1", seasonListUse.value.toString())
//
//    }
//
//    viewModelUse.getSeasons("https://jut.su/$input", userAgent)



    // Btn
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onClick = {
            dialogShown.value = true
            if (input.isNotEmpty()) {
                val search = "https://jut.su/$input"
                viewModel.networking(search, context, webViewAgent)
            }

        },
    ) {
        Text(text = "Search & Download")
    }

    val controller = rememberNavController()

    // Dialog
    if (dialogShown.value && input.isNotEmpty()) {

//        BackHandler {
//            controller.popBackStack()
//        }

        AlertDialog(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp)
                .wrapContentHeight()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
//            onDismissRequest = { controller.popBackStack() },
            onDismissRequest = { dialogShown.value = false },
            title = { Text(text = "Download") },
            text = {

//                if (seasonList.value.isEmpty()) CircularProgressIndicator(
//                    modifier = Modifier.padding(
//                        120.dp
//                    )
//                )


                NavHost(navController = controller, startDestination = "SeasonPeakList") {
                    composable("SeasonPeakList") {
                        SeasonPeakList(
                            controller,
                            seasonList.value,
                            seasonLink.value,
                            seriesList.value,
                            userAgent,
                            viewLifecycleOwner
                        )
                    }
                    composable("SeriesPeakList") {
                        SeriesPeakList(
                            controller,
                            seriesList.value,
//                            checkedList,
                            seriesLink.value,
                            userAgent,
                        )
                    }
                }

            },
            confirmButton = {
                Log.d("CONFIRMBUTTON", SeriesSnapshotStateList.toString())

                when (SeriesSnapshotStateList.isNotEmpty()) {
                    true -> TextButton(onClick = {

                        viewModel.networkingResolutionAndFinal(
                            context,
                            userAgent = userAgent,
                            listOfSeries = SeriesSnapshotStateList,
                            listOfLinks = SeriesLinkSnapshotStateList
                        )

                        dialogShown.value = false

                    }) { Text(text = "Confirm") }
                    else -> {
                        Text(text = "")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogShown.value = false }) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}

@Composable
fun SeasonPeakList(
    controller: NavHostController,
    numberSeasonsList: List<String>,
    linkSeriesList: List<String>,
    seriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner
) {



    Column {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (numberSeasonsList.isEmpty()) CircularProgressIndicator()
        }

//        BackHandler {
//            controller.backQueue.removeIf { it.destination.route == "SeasonPeakList" }
//            controller.popBackStack()
//        }

        LazyColumn {
            items(numberSeasonsList) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
//                    currentListIndex.value = (currentListIndex.value + 1) % lists.size

                        val index = numberSeasonsList.indexOf(it)
                        Log.d("INDEX", index.toString())

                        var isHasOnlyOneSeasonToken = false

                        viewModel.hasOnlyOneSeasonToken.observe(viewLifecycleOwner) {
                            isHasOnlyOneSeasonToken = it
                        }

                        if (!isHasOnlyOneSeasonToken) {
                            viewModel.networkSeries(
                                userAgent = userAgent,
                                urlForSeries = linkSeriesList[index]
                            )

                        }

//                        Log.d("INDEX", linkSeriesList[index])

                        controller.navigate("SeriesPeakList")
                    }
                ) {
                    Text(
                        text = it,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }
        }
    }

}

@Composable
fun SeriesPeakList(
    controller: NavHostController,
    seriesList: List<String>,
//    checkedList: MutableList<String>,
    linkSeriesList: List<String>,
    userAgent: String
) {

//    BackHandler {
//        controller.navigate("SeasonPeakList") {
//            popUpTo(controller.backQueue.first().id) {
//                inclusive = true
//            }
//        }
//
//        Log.d("NAVSTACK", controller.backQueue.first().toString())
//
//    }

//    Log.d("WHYNOT", seriesList.toString())
//    Log.d("WHYNOT", linkSeriesList.toString())

    val checkedItem = mutableListOf<String>()

    Column {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (seriesList.isEmpty()) CircularProgressIndicator()
        }

        LazyColumn {
            items(seriesList) { item ->

                val isChecked = rememberSaveable { mutableStateOf(false) }

                Row {

                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { newValue ->
                            isChecked.value = newValue
                            if (newValue) {
                                SeriesSnapshotStateList.add(item)

                                val index = seriesList.indexOf(item)
                                SeriesLinkSnapshotStateList.add(linkSeriesList[index])
                            } else {
                                SeriesSnapshotStateList.remove(item)

                                val index = seriesList.indexOf(item)
                                SeriesLinkSnapshotStateList.remove(linkSeriesList[index])
                            }
//                            Log.d("WHYNOT", SeriesSnapshotStateList.toList().toString())
//                            Log.d("WHYNOT", SeriesLinkSnapshotStateList.toList().toString())
                        }
                    )

                    Text(
                        item,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 11.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResolutionPeakList(
    controller: NavHostController,
    numberSeasonsList: List<String>,
    linkSeriesList: List<String>,
    seriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner
) {



    Column {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (numberSeasonsList.isEmpty()) CircularProgressIndicator()
        }

//        BackHandler {
//            controller.backQueue.removeIf { it.destination.route == "SeasonPeakList" }
//            controller.popBackStack()
//        }

        LazyColumn {
            items(numberSeasonsList) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
//                    currentListIndex.value = (currentListIndex.value + 1) % lists.size

                        val index = numberSeasonsList.indexOf(it)
                        Log.d("INDEX", index.toString())

//                        var isHasOnlyOneSeasonToken = false

//                        viewModel.hasOnlyOneSeasonToken.observe(viewLifecycleOwner) {
//                            isHasOnlyOneSeasonToken = it
//                        }

//                        if (!isHasOnlyOneSeasonToken) {
                            viewModel.networkSeries(
                                userAgent = userAgent,
                                urlForSeries = linkSeriesList[index]
                            )

//                        }

//                        Log.d("INDEX", linkSeriesList[index])

//                        controller.navigate("SeriesPeakList")
                    }
                ) {
                    Text(
                        text = it,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }
        }
    }

}



@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.naruto_gif).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier
//            .fillMaxWidth()
            .size(200.dp)
            .alpha(0.4f)
//            .padding(top = 550.dp),
    )
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun Screen() {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JutloaderTheme {
        TextField()
    }
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    JutloaderTheme {
//        CreateDialog()
    }
}

