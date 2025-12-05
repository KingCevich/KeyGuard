package com.example.keyguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.keyguard.navigation.AppNavigation
import com.example.keyguard.ui.PostScreen
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                AppNavigation()
            }
        }
    }
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            PostScreen()
//        }
//    }
//}




//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//        }
//
//        lifecycleScope.launch {
//            try {
//                val response = RetrofitInstance.api.getContrasenas()
//                if (response.isSuccessful) {
//                    val lista = response.body()
//                    Log.d("API_TEST", "Conexión exitosa. Total contraseñas: ${lista?.size}")
//                } else {
//                    Log.e("API_TEST", "Error HTTP: ${response.code()} - ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("API_TEST", "Error de conexión: ${e.localizedMessage}")
//            }
//        }
//    }
//}
