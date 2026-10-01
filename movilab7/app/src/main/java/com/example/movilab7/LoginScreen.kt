package com.example.movilab7

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.toRoute

@Composable
fun LoginScreen(navController: NavController) {

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
            Button(onClick = {
                navController.navigate(CharactersGraph) {
                    popUpTo<Login> {
                        inclusive = true
                    }
                }
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