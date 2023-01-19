package com.buffersolve.jutloader.ui

class oldui {


    //@Composable
//fun Navigation() {
//    val currentScreen = remember { mutableStateOf(0) }
//    val screens = listOf(
//        { SeasonList() },
//        { Screen2() }
//    )
//    AnimatedContent(
//        current = currentScreen,
//        children = screens
//    ) { screen, animation ->
//        LazyColumn {
//            items(screens[screen]) {
//                    item ->
//                item(modifier = Modifier.animation(animation))
//            }
//        }
//    }
//}
//
//@Composable
//fun SeasonList(list: List<String>) {
//    LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp)) {
//        items(list) {
//            TextButton(
//                onClick = {
////                    currentListIndex.value = (currentListIndex.value + 1) % lists.size
//                }
//            ) {
//                Text(
//                    text = it,
//                    style = TextStyle(fontSize = 20.sp))
//            }
//        }
//
//    }
//}









    ////////////





    //@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CardSelectSeries(viewLifecycleOwner: LifecycleOwner) {
//
//    val seriesValue = remember {
//        mutableStateOf(listOf<String>())
//    }
//
//    viewModel.seria.observe(viewLifecycleOwner) {
//        seriesValue.value = it.seria
//    }
//
//    Card (modifier = Modifier
//        .fillMaxSize()
//        .padding(24.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        )
//    ) {
////        FilterChip(
////            selected = ,
////            onClick = { /*TODO*/ },
////            label = { Text(text = "Chip", color = Color.Green) },
////        )
//        Text(text = "Pick Series")
//
//        Row(modifier = Modifier) {
//            for (items in seriesValue.value) {
//                Chip(items)
//
//            }
//        }
//
//
////        Chip(viewLifecycleOwner)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Chip(text: String) {
//
//    val selectedState = remember {
//        mutableStateOf(false)
//    }
//
//
//    FilterChip (
//        selected = selectedState.value,
//        onClick = { selectedState.value = !selectedState.value },
//        label = { Text(text) },
//        leadingIcon = if (selectedState.value) {
//            {
//                Icon(
//                    imageVector = Icons.Filled.Done,
//                    contentDescription = "Description",
//                    modifier = Modifier.size(FilterChipDefaults.IconSize)
//                )
//            }
//        } else {
//            null
//        }
//    )
//}
}