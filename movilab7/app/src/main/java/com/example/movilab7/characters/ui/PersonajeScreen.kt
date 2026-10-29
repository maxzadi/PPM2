package com.example.movilab7.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.movilab7.characters.data.PersonajeDatos
import com.example.movilab7.components.ErrorScreen
import com.example.movilab7.components.LoadingScreen
import kotlinx.serialization.Serializable
import com.example.movilab7.core.room.Character

@Serializable
data class PersonajeID(
    val id: Int
)



@Composable
fun CharacterDetailScreen(
    navController: NavController,
    viewModel: CharacterDetailViewModel = viewModel()
) {
    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    when {
        uiState.isLoading -> {
            LoadingScreen(
                mensaje = "Cargando personaje...",
                onClick = viewModel::simulateError
            )
        }

        uiState.hasError -> {
            ErrorScreen(
                mensaje = "No se pudo cargar el personaje",
                onRetry = viewModel::loadCharacter
            )
        }

        else -> {
            PersonajeScreen(
                personaje = uiState.data,
                navController = navController,
                onFavoritoClick = viewModel::cambiarFavorito
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajeScreen(
    personaje: Character?,
    navController: NavController,
    onFavoritoClick: () -> Unit
) {

    if (personaje == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Personaje no encontrado")
        }

        return ;
    }
    //val personaje = personajes.get(0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Character Detail")
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
                ),
                actions = {
                    IconButton(onClick = onFavoritoClick) {
                        Icon(
                            imageVector = if (personaje.favorito) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = if (personaje.favorito) {
                                "Quitar de favoritos"
                            } else {
                                "Agregar a favoritos"
                            }
                        )
                    }
                },
            )
        }
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding,)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp),
                model = personaje.imagen,
                contentDescription =
                    "Imagen de ${personaje.nombre}",
                contentScale = ContentScale.Crop
            )
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .padding(16.dp)
                ,
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
            ){Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Column(modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text("Species:")
                    Text("Status:")
                    Text("Gender:")
                }
                Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text(personaje.especie ?: "Desconocida")
                    Text(personaje.status ?: "Desconocido")
                    Text(personaje.genero ?: "Desconocido")
                }
            }
            }
        }

    }
}