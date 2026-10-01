package com.example.movilab7

import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PerfilScreen(navController: NavController) {
    val activity = LocalContext.current as? Activity

    Column(modifier=Modifier
        .fillMaxSize()
        , horizontalAlignment = Alignment.CenterHorizontally) {

        //Imagen
        Box(modifier = Modifier
            .size(300.dp)
            , contentAlignment = Alignment.BottomCenter
            ){
            Image(contentDescription = "Logo",painter = painterResource(R.drawable.ic_launcher_background))
        }

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
            Spacer(modifier = Modifier.padding(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Column(modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text("Nombre:")
                    Text("Carné:")
                }
                Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text("Matías Zamora")
                    Text("25760")
                }
            }
Spacer(modifier = Modifier.padding(10.dp))
        //Boton
        Box(modifier = Modifier
            .weight(4f)
            , contentAlignment = Alignment.TopCenter) {
            Button(onClick = {
                    activity?.finish()
            }, modifier = Modifier
                .width(250.dp)
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
            ) { Text("Cerrar Sesión") }
        }
    }}
}