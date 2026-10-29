package com.example.movilab7.characters

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.movilab7.characters.ui.CharactersViewModel
import com.example.movilab7.components.ErrorScreen
import com.example.movilab7.components.LoadingScreen
import kotlinx.serialization.Serializable
import com.example.movilab7.core.room.Character
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Serializable
data object Characters



@Composable
fun CharactersScreen(
    navController: NavController,
    viewModel: CharactersViewModel
) {
    val uiState = viewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    when {
        uiState.isLoading -> {
            LoadingScreen(
                mensaje = "Cargando personajes...",
                onClick = viewModel::simulateError
            )
        }

        uiState.hasError -> {
            ErrorScreen(
                mensaje = "No se pudieron cargar los personajes",
                onRetry = viewModel::loadCharacters
            )
        }

        else -> {
            CharacterScreenContent(
                personajesData = uiState.data,
                navController = navController,
                onFavoritoClick = viewModel::cambiarFavorito
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreenContent(
    personajesData: List<Character>,
    navController: NavController,
    onFavoritoClick: (Int, Boolean) -> Unit
) {
    val activity = LocalContext.current as? Activity

    var soloFavoritos by rememberSaveable {
        mutableStateOf(false)
    }

    val personajesVisibles = if (soloFavoritos) {
        personajesData.filter { it.favorito }
    } else {
        personajesData
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!navController.popBackStack()) {
                                activity?.finish()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    TextButton(
                        onClick = {
                            soloFavoritos = !soloFavoritos
                        }
                    ) {
                        Text(
                            text = if (soloFavoritos) "Ver todos" else "Ver favoritos",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (personajesVisibles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (soloFavoritos) {
                                "No hay favoritos para mostrar"
                            } else {
                                "No hay elementos para mostrar"
                            }
                        )
                    }
                }
            }
            items(
                items = personajesVisibles,
                key = { personaje -> personaje.id }
            ) { personaje ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                PersonajeID(id = personaje.id)
                            )
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = personaje.imagen,
                            contentDescription =
                                "Imagen de ${personaje.nombre ?: "personaje"}",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = personaje.nombre ?: "Sin nombre",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "${personaje.especie ?: "Desconocida"} - " +
                                        (personaje.status ?: "Desconocido"),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        IconButton(
                            onClick = {
                                onFavoritoClick(
                                    personaje.id,
                                    !personaje.favorito
                                )
                            }
                        ) {
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
                                },
                                tint = if (personaje.favorito) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}