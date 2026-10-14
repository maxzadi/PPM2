package com.example.movilab7

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import kotlinx.serialization.Serializable

@Serializable
data object Login

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    LaunchedEffect(uiState.data.guardadoExitoso) {
        if (uiState.data.guardadoExitoso) {
            navController.navigate(CharactersGraph) {
                popUpTo<Login> {
                    inclusive = true
                }
            }
        }
    }

    val image = painterResource(
        id = R.drawable.rick_and_morty
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(5f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = image,
                contentDescription = "Logo"
            )
        }
        Box(modifier = Modifier.weight(2f)){
            OutlinedTextField(
                value = uiState.data.nombre,
                onValueChange = viewModel::onNombreChange,
                label = {
                    Text("Nombre")
                },
                placeholder = {
                    Text("Escribe tu nombre")
                },
                supportingText = {
                    if (uiState.hasError) {
                        Text(
                            text = "Debes escribir tu nombre",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = uiState.hasError,
                enabled = !uiState.isLoading,
                singleLine = true,
                modifier = Modifier
                    .width(290.dp)

            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Box(
            modifier = Modifier.weight(4f),
            contentAlignment = Alignment.TopCenter
        ) {
            Button(
                onClick = viewModel::setNombre,
                enabled = !uiState.isLoading,
                modifier = Modifier.width(250.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        color =
                            MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Text("Matías Zamora #25760")
        }
    }
}