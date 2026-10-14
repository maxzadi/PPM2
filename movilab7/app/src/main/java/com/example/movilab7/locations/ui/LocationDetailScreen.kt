package com.example.movilab7.locations.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movilab7.components.LoadingScreen
import com.example.movilab7.components.ErrorScreen
import com.example.movilab7.locations.data.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationID(val id: Int)

@Composable
fun LocationDetailScreen(
    navController: NavController,
    viewModel: LocationDetailViewModel = viewModel()
) {
    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    when {
        uiState.isLoading -> {
            LoadingScreen(
                mensaje = "Cargando ubicación...",
                onClick = viewModel::simulateError
            )
        }

        uiState.hasError -> {
            ErrorScreen(
                mensaje = "No se pudo cargar la ubicación",
                onRetry = viewModel::loadLocation
            )
        }

        uiState.data != null -> {
            LocationDetailContent(
                location = uiState.data,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailContent(location: Location?, navController: NavController) {

    if (location == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Location no encontrado")
        }

        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Location Detail")
                }, navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()
                    }) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Card(
                modifier = Modifier
                    .width(280.dp)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
            ){
                Text(location.name, style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Column(modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text("ID:")
                    Text("Type:")
                    Text("Dimensions:")
                }
                Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text(location.id.toString())
                    Text(location.type)
                    Text(location.dimension)
                }
            }
            }
        }

    }
}