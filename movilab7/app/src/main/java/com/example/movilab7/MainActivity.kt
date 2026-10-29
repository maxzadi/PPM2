package com.example.movilab7

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movilab7.ui.theme.Movilab7Theme

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.room.Room


import com.example.movilab7.characters.CharacterDetailScreen
import com.example.movilab7.characters.CharacterDetailViewModel
import com.example.movilab7.characters.CharacterDetailViewModelFactory
import com.example.movilab7.characters.CharactersScreen
import com.example.movilab7.characters.data.PersonajeDatos
import com.example.movilab7.characters.Characters
import com.example.movilab7.characters.CharactersViewModelFactory
import com.example.movilab7.characters.ui.CharactersViewModel
import com.example.movilab7.characters.PersonajeScreen

import com.example.movilab7.characters.PersonajeID
import com.example.movilab7.components.LoadingScreen
import com.example.movilab7.core.AppDatabase
import com.example.movilab7.locations.ui.LocationDetailScreen
import com.example.movilab7.locations.ui.LocationsScreen
import com.example.movilab7.locations.ui.Locations
import com.example.movilab7.locations.ui.LocationID
import com.example.movilab7.locations.data.LocationDb
import com.example.movilab7.locations.ui.LocationDetailViewModel
import com.example.movilab7.locations.ui.LocationDetailViewModelFactory
import com.example.movilab7.locations.ui.LocationsViewModel
import com.example.movilab7.locations.ui.LocationsViewModelFactory

class MainActivity : ComponentActivity() {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "uvg.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Movilab7Theme {
                HomeScreen(database = database)
            }
        }
    }
}

val personajes = listOf<PersonajeDatos>(
    PersonajeDatos(
        1,
        "Rick Sanchez",
        "Humano",
        "Vivo",
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        "Masculino"
    ),
    PersonajeDatos(2,"Morty Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", "Masculino"),
    PersonajeDatos(3,"Summer Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", "Femenino"),
    PersonajeDatos(4,"Beth Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/4.jpeg", "Femenino"),
    PersonajeDatos(5,"Jerry Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/5.jpeg", "Masculino"),
    PersonajeDatos(6,"Abadango Cluster Princess", "Alien", "Vivo", "https://rickandmortyapi.com/api/character/avatar/6.jpeg", "Femenino")
)

@Serializable
data object CharactersGraph

@Serializable
data object LocationsGraph

var locationDb = LocationDb()

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    database: AppDatabase,
    sessionViewModel: SessionViewModel = viewModel()
) {
    val sessionState = sessionViewModel.uiState
        .collectAsStateWithLifecycle()
        .value

    if (sessionState.isLoading) {
        LoadingScreen(
            mensaje = "Iniciando...",
            onClick = {}
        )

        return
    }

    val navController = rememberNavController()

    val startRoute: Any =
        if (sessionState.data) {
            CharactersGraph
        } else {
            Login
        }

        val backStackEntry by navController.currentBackStackEntryAsState()
        val destinoActual = backStackEntry?.destination

    val mostrarBarra = destinoActual != null &&
            !destinoActual.hasRoute<Login>()

    // Cambia de pestaña conservando su estado.
    fun cambiarPestana(ruta: Any) {
        navController.navigate(ruta) {
            popUpTo<Characters> {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }

    val application =
        LocalContext.current.applicationContext as Application

    val appContext = LocalContext.current.applicationContext

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar {
                    NavigationBarItem(
                        selected = destinoActual?.hierarchy?.any {
                            it.hasRoute<CharactersGraph>()
                        } == true,
                        onClick = {
                            cambiarPestana(CharactersGraph)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.PeopleAlt,
                                contentDescription = null
                            )
                        },
                        label = { Text("Characters") }
                    )

                    NavigationBarItem(
                        selected = destinoActual?.hierarchy?.any {
                            it.hasRoute<LocationsGraph>()
                        } == true,
                        onClick = {
                            cambiarPestana(LocationsGraph)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null
                            )
                        },
                        label = { Text("Locations") }
                    )

                    NavigationBarItem(
                        selected = destinoActual?.hasRoute<Profile>() == true,
                        onClick = {
                            cambiarPestana(Profile)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute
        ) {
            composable<Login> {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(
                        application = application,
                        database = database
                    )
                )

                LoginScreen(
                    navController = navController,
                    viewModel = loginViewModel
                )
            }

            navigation<CharactersGraph>(
                startDestination = Characters
            ) {
                composable<Characters> {
                    val charactersViewModel: CharactersViewModel = viewModel(
                        factory = CharactersViewModelFactory(
                            database.characterDao()
                        )
                    )
                    CharactersScreen(navController,
                        charactersViewModel)
                }

                composable<PersonajeID> { entry ->
                    val ruta = entry.toRoute<PersonajeID>()

                    val detailViewModel: CharacterDetailViewModel = viewModel(
                        factory = CharacterDetailViewModelFactory(
                            characterDao = database.characterDao(),
                            id = ruta.id
                        )
                    )

                    CharacterDetailScreen(
                        navController = navController,
                        viewModel = detailViewModel
                    )
                }
            }

            // Subgrafo de Locations
            navigation<LocationsGraph>(
                startDestination = Locations
            ) {
                composable<Locations> {
                    val locationsViewModel: LocationsViewModel = viewModel(
                        factory = LocationsViewModelFactory(
                            database.locationDao()
                        )
                    )

                    LocationsScreen(navController,
                        locationsViewModel)
                }

                composable<LocationID> { entry ->
                    val ruta = entry.toRoute<LocationID>()

                    val detailViewModel: LocationDetailViewModel = viewModel(
                        factory = LocationDetailViewModelFactory(
                            locationDao = database.locationDao(),
                            id = ruta.id
                        )
                    )

                    LocationDetailScreen(
                        navController = navController,
                        viewModel = detailViewModel
                    )
                }

            }
            // Pantalla independiente
            composable<Profile> {
                val cerrarSesionViewModel: LogoutViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            LogoutViewModel(
                                context = appContext,
                                database = database
                            )
                        }
                    }
                )

                val cierre = cerrarSesionViewModel.uiState
                    .collectAsStateWithLifecycle()
                    .value

                LaunchedEffect(cierre.completado) {
                    if (cierre.completado) {
                        navController.navigate(Login) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }

                PerfilScreen(
                    navController = navController,
                    cerrandoSesion = cierre.isLoading,
                    errorCierre = cierre.error,
                    onCerrarSesion = cerrarSesionViewModel::cerrarSesion
                )
            }
        }
    }
}