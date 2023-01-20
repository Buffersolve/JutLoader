package com.buffersolve.jutloader

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buffersolve.jutloader.model.Seria
import com.buffersolve.jutloader.ui.theme.JutloaderTheme

const val url = "https://jut.su/berserk/season-4/episode-1.html"

//const val url2 = "https://jut.su/shingekii-no-kyojin/"
const val url3 = "https://jut.su/vinland-saga/"
const val url4 = "https://jut.su/shingekii-no-kyojin/season-1/"
const val url5 = "https://jut.su/shingekii-no-kyojin/season-1/episode-1.html"

lateinit var viewModel: MainActivityViewModel
lateinit var webViewAgent: String

val checked: MutableLiveData<MutableList<String>> = MutableLiveData()

var checkedItems = listOf<String>()
val SeriesSnapshotStateList = SnapshotStateList<String>()
val SeriesLinkSnapshotStateList = SnapshotStateList<String>()
var openDialogValue = false
var input: String = ""


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JutloaderTheme {
                // A surface container using the 'background' color from the theme
                Column {
                    TextField()
//                    SelectButton(text = "Select")
//                    CardSelectSeries(this@MainActivity)
                    NavigationDialog(this@MainActivity, webViewAgent, this@MainActivity)

//                    CreateDialog(this@MainActivity, this@MainActivity)
                }

            }
        }
        webViewAgent = WebView(this).settings.userAgentString
        Log.d("AGENT", webViewAgent)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]


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

//@Composable
//fun SelectButton(
//    modifier: Modifier = Modifier,
//    text: String
//) {
//    OutlinedButton(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(start = 24.dp, end = 24.dp),
//        onClick = {
//            openDialogValue = true
//        },
//    ) {
//        Text(text = text)
//    }
//}

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

        AlertDialog(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp),
//            onDismissRequest = { controller.popBackStack() },
            onDismissRequest = { dialogShown.value = false },
            title = { Text(text = "title") },
            text = {
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
        LazyColumn {
            items(numberSeasonsList) {
                TextButton(
                    onClick = {
//                    currentListIndex.value = (currentListIndex.value + 1) % lists.size

                        val index = numberSeasonsList.indexOf(it)
                        Log.d("INDEX", index.toString())

                        var isHasOnlyOneSeasonToken: Boolean = false

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

    val checkedItem = mutableListOf<String>()

    Column {


//        val allChecked = rememberSaveable { mutableStateOf(false) }

//        Row {
//            Checkbox(
//                checked = allChecked.value,
//                onCheckedChange = {
//                    allChecked.value = !allChecked.value
//                }
//            )
//            Text("Pick All")
//        }

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

//                            SeriesSnapshotStateList = checkedItem.toList()
//                            checkedItems = checkedItem
                            Log.d("CHECKED", SeriesSnapshotStateList.toList().toString())
                            Log.d("CHECKED", SeriesLinkSnapshotStateList.toList().toString())

                        }
                    )
                    Text(item)
                }
            }
        }
    }
//                return checkedItems
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

