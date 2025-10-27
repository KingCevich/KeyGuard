package com.example.keyguard.security

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

private const val TAG = "KeyGuard/Bio"

sealed class BioStatus {
    data object Success : BioStatus()
    data object NoHardware : BioStatus()
    data object HWUnavailable : BioStatus()
    data object NoneEnrolled : BioStatus()
    data object Unknown : BioStatus()
}

/** Estado de capacidad biométrica detallado. */
fun biometricsStatus(activity: FragmentActivity): BioStatus {
    val bm = BiometricManager.from(activity)
    val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    return when (bm.canAuthenticate(authenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> BioStatus.Success
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BioStatus.NoHardware
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BioStatus.HWUnavailable
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BioStatus.NoneEnrolled
        else -> BioStatus.Unknown
    }
}

/** Atajo “booleano” si solo quieres saber si se puede autenticar. */
fun canUseBiometrics(activity: FragmentActivity): Boolean =
    biometricsStatus(activity) == BioStatus.Success

/** Abre pantalla del sistema para enrolar huella/PIN si falta. */
fun launchEnroll(activity: Activity) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }
            activity.startActivity(intent)
        } else {
            // En APIs < 30 no hay intent específico; abrimos seguridad general
            activity.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
        }
    } catch (t: Throwable) {
        Log.e(TAG, "No se pudo abrir enrolamiento: ${t.message}", t)
    }
}

/** Muestra el BiometricPrompt con defensas (try/catch). */
fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onFail: (msg: String) -> Unit
) {
    try {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onFail("Error: $errString")
            }
            override fun onAuthenticationFailed() {
                // huella no reconocida; el prompt sigue
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)

        // ⚠️ NO mezclar negativeButton con DEVICE_CREDENTIAL.
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Desbloquear KeyGuard")
            .setSubtitle("Usa huella o PIN/patrón")
            .setConfirmationRequired(true)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        prompt.authenticate(promptInfo)
    } catch (t: Throwable) {
        Log.e(TAG, "Fallo al abrir prompt: ${t.message}", t)
        onFail("No se pudo abrir el prompt biométrico.")
    }
}

/** Helper para obtener FragmentActivity desde un Context (Compose: LocalContext). */
fun Context.findActivity(): FragmentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
