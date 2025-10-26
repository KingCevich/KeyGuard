package com.example.keyguard.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.keyguard.security.AuthResult
import com.example.keyguard.security.Biometrica


class MainViewModel : ViewModel() {


    fun iniciarAutenticacion(activity: FragmentActivity, onResult: (AuthResult) -> Unit) {
        Log.d("MainViewModel", "Iniciando autenticación...")
        val biometrica = Biometrica(activity)
        biometrica.autenticar { resultado ->

            onResult(resultado)
        }
    }
}
