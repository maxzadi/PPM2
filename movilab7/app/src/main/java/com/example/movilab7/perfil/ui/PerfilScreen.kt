package com.example.movilab7

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movilab7.components.ErrorScreen
import com.example.movilab7.components.LoadingScreen
import kotlinx.serialization.Serializable

@Serializable
data object Profile

@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    LaunchedEffect(
        uiState.data.cierreSesionExitoso
    ) {
        if (uiState.data.cierreSesionExitoso) {
            navController.navigate(Login) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        }
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(
                mensaje = "Cargando...",
                onClick = {}
            )
        }

        uiState.hasError -> {
            ErrorScreen(
                mensaje = "Ocurrió un error",
                onRetry = viewModel::cargarPerfil
            )
        }

        else -> {
            PerfilContent(
                nombre = uiState.data.nombre,
                onCerrarSesion = viewModel::cerrarSesion
            )
        }
    }
}
@Composable
fun PerfilContent(
    nombre: String,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(
                    R.drawable.ic_launcher_background
                ),
                contentDescription = "Logo"
            )
        }

        Card(
            modifier = Modifier
                .width(280.dp)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(15.dp)
                    ) {
                        Text("Nombre:")
                        Text("Carné:")
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement =
                            Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = nombre.ifBlank {
                                "Sin nombre"
                            }
                        )

                        Text("25760")
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.width(250.dp),
            shape = CircleShape
        ) {
            Text("Cerrar sesión")
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}