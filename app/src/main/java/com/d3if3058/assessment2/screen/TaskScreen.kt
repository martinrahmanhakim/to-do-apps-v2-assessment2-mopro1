package com.d3if3058.assessment2.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.d3if3058.assessment2.R
import com.d3if3058.assessment2.model.MainVIewModel
import com.d3if3058.assessment2.model.Task
import com.d3if3058.assessment2.navigation.Screen
import com.d3if3058.assessment2.ui.theme.Assessment2Theme
import com.d3if3058.assessment2.util.SettingsDataStore
import com.d3if3058.assessment2.util.ViewModelFactory
import com.d3if3058.mobpro1.database.TaskDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController){
    val dataStore = SettingsDataStore (LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.daftarToDo)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.FormBaru.route)
            }
            ) {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = MaterialTheme.colorScheme.primary
                )

            }
        }
    ) {
            paddingValues ->  ScreenContent(showList, Modifier.padding(paddingValues), navController)
    }
}

@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController){
    val context = LocalContext.current
    val db = TaskDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainVIewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val sortedData = remember(data) {
        data.sortedByDescending { it.id }
    }

    if (data.isEmpty()) {

        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable._214833),
                contentDescription = stringResource(id = R.string.gambarNoData),
                modifier = Modifier
                    .size(230.dp)
            )
            Text(
                text = stringResource(id = R.string.belumAdaData),
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )

        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(sortedData) { task ->
                    ListItem(
                        task = task,
                        onClick = { navController.navigate(Screen.FormUbah.withId(task.id)) },
                        id = task.id,
                        navController = navController

                    )
                    Divider()
                }
            }

        }
        else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ){
                items(sortedData) {
                    GridItem(task = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(task: Task, onClick: () -> Unit, id: Long, navController: NavHostController) {
    val context = LocalContext.current
    val db = TaskDb.getInstance(context)
    val factory = ViewModelFactory (db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = task.judul,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.isi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.prioritas,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.hapusTugas))
            }

            if (id != null){

                DisplayALertDialog(
                    openDialog = showDialog,
                    onDismissRequest = { showDialog = false}) {
                    showDialog = false
                    viewModel.delete(id)
                }
            }

            IconButton(onClick = {
                shareData(
                    context = context,
                    message = context.getString(R.string.bagikanTemplate, task.judul, task.isi),
                    priority = task.prioritas
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(id = R.string.bagikan),
                    Modifier.scale(0.8f)
                )
            }
        }
    }
}

@Composable
fun GridItem(task: Task, onClick: () -> Unit){
    val context = LocalContext.current
    val db = TaskDb.getInstance(context)
    val factory = ViewModelFactory (db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var showDialog by remember { mutableStateOf(false) }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,),
        border = BorderStroke(1.dp, Color.Gray)
    ){
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            Text(text = task.judul,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(text = task.isi,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = task.prioritas,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun shareData(context: Context, message: String, priority: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        var messageWithPriority = message
        if (priority.isNotBlank()) {
            messageWithPriority += "\nStatus: $priority"
        }
        putExtra(Intent.EXTRA_TEXT, messageWithPriority)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    Assessment2Theme {
        MainScreen(rememberNavController())
    }
}