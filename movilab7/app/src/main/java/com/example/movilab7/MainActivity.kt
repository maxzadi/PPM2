package com.example.movilab7

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import kotlinx.serialization.Serializable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.example.movilab7.ui.theme.Movilab7Theme

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





@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun HomeScreen(){
    Scaffold(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier
                .fillMaxSize()
        ){
            composable<Login>{
                backstackEntry->
                val destino: Login = backstackEntry.toRoute()
                val image = painterResource(R.drawable.rick_and_morty)
                Column(modifier=Modifier
                    .fillMaxSize()
                    , horizontalAlignment = Alignment.CenterHorizontally) {

                    //Imagen
                    Box(modifier = Modifier
                        .weight(5f)
                        , contentAlignment = Alignment.BottomCenter){
                        Image(contentDescription = "Logo",
                            painter = image
                        )
                    }
                    //Boton
                    Box(modifier = Modifier
                        .weight(4f)
                        , contentAlignment = Alignment.TopCenter) {
                        Button({navController.navigate(
                            route = Characters
                        ){popUpTo<Login>{inclusive = true}}
                               }, modifier = Modifier
                            .width(250.dp)
                            .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
                        ) { Text("Entrar") }
                    }

                    //Nombre
                    Box(modifier = Modifier
                        .weight(1f)
                        , contentAlignment = Alignment.TopCenter
                    ) {
                        Text("Matías Zamora #25760")
                    }
                }
            }

            composable<Characters>{
                val activity = LocalContext.current as? Activity

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Characters")
                            }, navigationIcon = {
                                IconButton(onClick = {
                                    if (!navController.popBackStack()) {
                                        activity?.finish()
                                    }
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
                ) {
                        innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = personajes,
                            key = { personaje -> personaje.id }
                        ) { personaje ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(PersonajeID( id = personaje.id))
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.surface
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
                                            "Imagen de ${personaje.nombre}",
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
                                            text = personaje.nombre,
                                            style =
                                                MaterialTheme.typography.titleMedium
                                        )

                                        Text(
                                            text =
                                                "${personaje.especie} - ${personaje.status}",
                                            style =
                                                MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            composable<PersonajeID>{backStackEntry ->
                val ruta = backStackEntry.toRoute<PersonajeID>()

                val personaje = personajes.firstOrNull {
                    it.id == ruta.id
                }

                if (personaje == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Personaje no encontrado")
                    }

                    return@composable
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
                            )
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
                                Text(personaje.especie)
                                Text(personaje.status)
                                Text(personaje.genero)
                            }
                        }
                        }
                    }

                }
            }
        }
    }

}