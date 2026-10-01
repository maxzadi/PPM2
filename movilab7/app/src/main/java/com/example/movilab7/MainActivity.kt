package com.example.movilab7

import LocationDb
import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.example.movilab7.characters.CharacterScreen
import com.example.movilab7.characters.PersonajeScreen
import com.example.movilab7.locations.LocationDetailScreen
import com.example.movilab7.locations.LocationsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Movilab7Theme() {
                HomeScreen()
            }
        }
    }
}

//Rutas
@Serializable
data object Login

@Serializable
data object Characters

@Serializable
data class PersonajeID(
    val id: Int
)

data class PersonajeDatos(
    val id: Int,
    val nombre: String,
    val especie: String,
    val status: String,
    val imagen: String,
    val genero: String

)

val personajes = listOf<PersonajeDatos>(
    PersonajeDatos(1,"Rick Sanchez", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/1.jpeg", "Masculino"),
    PersonajeDatos(2,"Morty Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/2.jpeg", "Masculino"),
    PersonajeDatos(3,"Summer Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/3.jpeg", "Femenino"),
    PersonajeDatos(4,"Beth Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/4.jpeg", "Femenino"),
    PersonajeDatos(5,"Jerry Smith", "Humano", "Vivo", "https://rickandmortyapi.com/api/character/avatar/5.jpeg", "Masculino"),
    PersonajeDatos(6,"Abadango Cluster Princess", "Alien", "Vivo", "https://rickandmortyapi.com/api/character/avatar/6.jpeg", "Femenino")
)

//Lab8

@Serializable
data object CharactersGraph

@Serializable
data object LocationsGraph

@Serializable
data object Locations

@Serializable
data class LocationID(val id: Int)

@Serializable
data object Profile

val locationDb = LocationDb()




@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

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
            startDestination = Login,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<Login> {
                LoginScreen(navController)
            }

            // Subgrafo de Characters
            navigation<CharactersGraph>(
                startDestination = Characters
            ) {
                composable<Characters> {
                    CharacterScreen(navController)
                }

                composable<PersonajeID> { entry ->
                    val ruta = entry.toRoute<PersonajeID>()

                    val personaje = personajes.firstOrNull {
                        it.id == ruta.id
                    }

                    PersonajeScreen(personaje, navController)
                }
            }

            // Subgrafo de Locations
            navigation<LocationsGraph>(
                startDestination = Locations
            ) {
                composable<Locations> {
                    LocationsScreen(locationDb.getAllLocations(), navController)
                }

                composable<LocationID> { entry ->
                    val ruta = entry.toRoute<LocationID>()

                    val location = locationDb.getLocationById(ruta.id)

                    LocationDetailScreen(location, navController)

                }

                // Pantalla independiente
                composable<Profile> {
                    PerfilScreen(navController)
                }
            }
        }
    }
}