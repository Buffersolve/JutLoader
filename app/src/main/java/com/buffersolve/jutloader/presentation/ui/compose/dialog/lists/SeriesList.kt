package com.buffersolve.jutloader.presentation.ui.compose.dialog.lists

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.buffersolve.jutloader.presentation.ui.SeriesLinkSnapshotStateList
import com.buffersolve.jutloader.presentation.ui.SeriesSnapshotStateList

@Composable
fun SeriesPeakList(
    controller: NavHostController,
    seriesList: List<String>,
//    checkedList: MutableList<String>,
    linkSeriesList: List<String>,
    userAgent: String
) {

    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (seriesList.isEmpty()) CircularProgressIndicator()
        }

//        BackHandler {
//            DialogState.add(false)
//            controller.popBackStack("SeasonPeakList", inclusive = false, saveState = false)
//        }

//        val hashmap: HashMap<String, String> = seriesList.zip(linkSeriesList).associate { it.first to it.second }
//        val hashMap2 = hashMapOf<String, String>()
//        hashmap.entries.remo

        val hashmap = seriesList.zip(linkSeriesList).toMap(HashMap())

        Log.d("HASHMAP1", "$hashmap")

        LazyColumn {
//            items(hashmap.keys.toList()) { item ->
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
//                BackHandler {
//                    DialogState.add(false)
//                    controller.popBackStack("SeasonPeakList", inclusive = false, saveState = false)
//                }
            }
        }
    }
}