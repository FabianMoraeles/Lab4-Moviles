package com.example.myapplicationlab4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplicationlab4.ui.theme.MyApplicationLab4Theme
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationLab4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecipeInputForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RecipeInputForm(modifier: Modifier = Modifier) {
    val recipeList = remember { mutableStateListOf<Pair<String, String>>() }
    val recipeName = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = recipeName.value,
            onValueChange = { recipeName.value = it },
            label = { Text("Nombre de la Receta") },  // Traducción del string
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = imageUrl.value,
            onValueChange = { imageUrl.value = it },
            label = { Text("URL de la Imagen") },  // Traducción del string
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                try {
                    if (recipeName.value.isNotBlank() && imageUrl.value.isNotBlank()) {
                        if (isValidUrl(imageUrl.value)) {
                            recipeList.add(Pair(recipeName.value, imageUrl.value))
                            recipeName.value = ""
                            imageUrl.value = ""
                        } else {
                            Log.e("RecipeInputForm", "URL inválida: ${imageUrl.value}")  // Traducción del string
                        }
                    } else {
                        Log.e("RecipeInputForm", "Campos vacíos: nombreReceta o urlImagen está en blanco")  // Traducción del string
                    }
                } catch (e: Exception) {
                    Log.e("RecipeInputForm", "Error al agregar receta", e)  // Traducción del string
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Agregar")  // Traducción del string
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Lista de Recetas:", style = MaterialTheme.typography.titleMedium)  // Traducción del string

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(recipeList) { recipe ->
                RecipeItem(recipeName = recipe.first, imageUrl = recipe.second)
            }
        }
    }
}

@Composable
fun RecipeItem(recipeName: String, imageUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .error(android.R.drawable.ic_dialog_alert)
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "Imagen de la Receta",  // Traducción del string
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = recipeName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .alignByBaseline()
                .padding(start = 8.dp)
        )
    }
    Divider(modifier = Modifier.padding(vertical = 8.dp))
}

fun isValidUrl(url: String): Boolean {
    return try {
        URL(url)
        true
    } catch (e: Exception) {
        false
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeInputFormPreview() {
    MyApplicationLab4Theme {
        RecipeInputForm()
    }
}