package com.buffersolve.jutloader

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.ScrollContainerInfo
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buffersolve.jutloader.ui.theme.JutloaderTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.File

lateinit var viewModel: MainActivityViewModel
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

//                val cardSize = remember {
//                    mutableStateOf(270.dp)
//                }

                val deviceHeightDensity = LocalConfiguration.current.screenHeightDp.dp
                val percentage = deviceHeightDensity.value * 0.83
                val finalValueDp = percentage.dp

                Log.d("density", finalValueDp.toString())

                val arrowIcon = remember {
                    mutableStateOf(Icons.Outlined.KeyboardArrowDown)
                }

                val expanded = remember { mutableStateOf(false) }

                val extraPadding by animateDpAsState(
                    if (expanded.value) finalValueDp else 270.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )


                val systemUiController = rememberSystemUiController()
                systemUiController.setNavigationBarColor(
                    color = MaterialTheme.colorScheme.background
                )

                val statusBarColor = MaterialTheme.colorScheme.secondaryContainer
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "JutLoader") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor =  MaterialTheme.colorScheme.secondaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        )
                    },
                    content = {

                        ElevatedCard(
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
//                                when (extraPadding) {
//                                    270.dp -> expanded.value = true
//                                    3000.dp -> expanded.value = false
//                                }
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

//                                CompositionLocalProvider(LocaConte) {
//                                    Text("High Emphasis Text")
//                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 26.dp, start = 20.dp, end = 20.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa vitae tortor condimentum lacinia quis vel. Sed nisi lacus sed viverra tellus. Porttitor leo a diam sollicitudin tempor id. Eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis. Quam id leo in vitae turpis massa sed. Congue nisi vitae suscipit tellus mauris a diam maecenas. Diam in arcu cursus euismod quis. Amet cursus sit amet dictum sit amet justo donec. Ornare arcu odio ut sem nulla. Leo vel orci porta non. Nascetur ridiculus mus mauris vitae ultricies. Convallis convallis tellus id interdum velit laoreet. Sagittis id consectetur purus ut faucibus pulvinar. Vitae justo eget magna fermentum iaculis eu non diam phasellus. Id eu nisl nunc mi ipsum faucibus vitae aliquet. Volutpat commodo sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. Pretium quam vulputate dignissim suspendisse in est ante in nibh. Tristique magna sit amet purus. Non tellus orci ac auctor augue mauris augue. Diam sit amet nisl suscipit adipiscing bibendum est. Varius morbi enim nunc faucibus a pellentesque sit. Pharetra magna ac placerat vestibulum. Integer enim neque volutpat ac tincidunt. Id faucibus nisl tincidunt eget nullam non nisi est sit. Nam libero justo laoreet sit amet cursus sit amet. Aliquam nulla facilisi cras fermentum odio eu feugiat. Aliquam purus sit amet luctus venenatis lectus. Rhoncus aenean vel elit scelerisque mauris pellentesque pulvinar pellentesque. Vitae tempus quam pellentesque nec nam aliquam sem. Nulla malesuada pellentesque elit eget gravida cum sociis. Dui sapien eget mi proin sed. Id neque aliquam vestibulum morbi blandit cursus risus at. Commodo quis imperdiet massa tincidunt nunc pulvinar sapien. Mauris in aliquam sem fringilla ut morbi tincidunt. Elit ullamcorper dignissim cras tincidunt lobortis feugiat. Elit at imperdiet dui accumsan sit amet nulla facilisi. Eget mauris pharetra et ultrices. Pretium aenean"
                                    )

                                }
                            }
                        }
                    }
                )
            }
        }
        webViewAgent = WebView(this).settings.userAgentString
        Log.d("AGENT", webViewAgent)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::
        class.java]


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
        label = { Text("Input") },
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

    // Btn
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onClick = {
            dialogShown.value = true
            if (input.isNotEmpty()) {
                viewModel.networking(input, context, webViewAgent)
            }

        },
    ) {
        Text(text = "Select")
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

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (numberSeasonsList.isEmpty()) CircularProgressIndicator()
        }

//        BackHandler {
//            controller.backQueue.removeIf { it.destination.route == "SeasonPeakList" }
//            controller.popBackStack()
//        }

        LazyColumn {
            items(numberSeasonsList) {
                TextButton(
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

                        Log.d("INDEX", linkSeriesList[index])

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


    val checkedItem = mutableListOf<String>()

    Column {

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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

