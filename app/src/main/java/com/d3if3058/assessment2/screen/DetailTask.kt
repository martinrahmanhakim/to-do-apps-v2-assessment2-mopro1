package com.d3if3058.assessment2.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d3if3058.assessment2.R
import com.d3if3058.assessment2.util.ViewModelFactory
import com.d3if3058.mobpro1.database.TaskDb

const val KEY_ID_TASK = "idTask"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null){
    val context = LocalContext.current
    val db = TaskDb.getInstance(context)
    val factory = ViewModelFactory (db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }
    var prioritas by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.prioritas),
        stringResource(id = R.string.non_prioritas)
    )

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getTask(id) ?: return@LaunchedEffect
        judul = data.judul
        isi = data.isi
        prioritas = data.prioritas
    }

    val radioOption = listOf("Prioritas", "Non-Prioritas")

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                            id = R.string.kembali
                        ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(
                            text = stringResource(id = R.string.tambah_catatan)
                        )
                    else
                        Text(
                            text = stringResource(id = R.string.edit_catatan)
                        )

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (judul == "" || isi == "" || prioritas == ""){
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(judul, isi, prioritas)
                        } else {
                            viewModel.update(id, judul, isi, prioritas)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = stringResource(
                            id = R.string.simpan
                        ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteAction {
                            showDialog = true
                        }
                        DisplayALertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false}) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        },
    ){
            paddingValues ->  FormCatatan(
        title = judul,
        onTitleChange = { judul = it },
        desc = isi,
        onDescChange = { isi= it },
        jenis = prioritas,
        onPriorityChanged = {prioritas = it},
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        id = id,
                radioOption = radioOption
    )
    }
}

@Composable
fun FormCatatan(
    title: String, onTitleChange: (String) -> Unit,
    desc: String, onDescChange: (String) -> Unit,
    jenis: String, onPriorityChanged: (String) -> Unit,
    radioOption : List<String>,
    modifier: Modifier, navController: NavHostController, id: Long? = null,
) {
    val context = LocalContext.current
    val db = TaskDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var judul by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.prioritas),
        stringResource(id = R.string.non_prioritas)
    )
    var priority by rememberSaveable {
        mutableStateOf((radioOptions[0]))
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getTask(id) ?: return@LaunchedEffect
        judul = data.judul
        isi = data.isi
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(id = R.string.judul)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = desc,
            onValueChange = { onDescChange(it) },
            label = { Text(text = stringResource(id = R.string.isi_catatan)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {

        radioOption.forEach { text ->
            KelasOpsi(
                label = text,
                isSelected = jenis == text,
                modifier = Modifier
                    .selectable(
                        selected = jenis == text,
                        onClick = { onPriorityChanged(text) },
                        role = Role.RadioButton
                    )

                    .padding(16.dp)
                    )
            {
                }
            }
        }
    }
}

@Composable
fun KelasOpsi(label: String, isSelected: Boolean, modifier: Modifier, content: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            RadioButton(selected = true, onClick = null)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            RadioButton(selected = false, onClick = null)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {

            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.hapus))}, onClick = {
                expanded = false
                delete()})
        }

    }
}


