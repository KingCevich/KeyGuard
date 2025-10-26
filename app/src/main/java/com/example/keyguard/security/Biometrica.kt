package com.example.keyguard.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

sealed class AuthResult {
    object Exito : AuthResult()
    object Fallo : AuthResult()
    data class Error(val mensaje: String) : AuthResult()
}

class Biometrica(private val activity: FragmentActivity) {

    fun autenticar(
        onResultado: (AuthResult) -> Unit
    ) {
        val biometricManager = BiometricManager.from(activity)

        // ↓↓↓ ESTA ES LA LÍNEA QUE ARREGLA EL CRASH ↓↓↓
        // Nos enfocamos solo en la biometría fuerte (huella/rostro) para evitar conflictos.
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onResultado(AuthResult.Error("Este dispositivo no tiene sensor biométrico."))
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onResultado(AuthResult.Error("El sensor biométrico no está disponible ahora."))
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Este es el error más común en emuladores.
                onResultado(AuthResult.Error("No tienes una huella o rostro registrado."))
                return
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Todo está en orden, podemos continuar.
            }
            else -> {
                // Otros posibles errores no fatales.
                onResultado(AuthResult.Error("El servicio biométrico no está disponible."))
                return
            }
        }

        // El resto de tu código estaba perfecto, no necesita cambios.
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onResultado(AuthResult.Exito)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onResultado(AuthResult.Fallo)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    onResultado(AuthResult.Error("Error: $errString"))
                }
                // No llamamos a onResultado si el usuario cancela, para no mostrar un Toast de error.
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Desbloquear KeyGuard")
            .setSubtitle("Usa tu huella para continuar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
