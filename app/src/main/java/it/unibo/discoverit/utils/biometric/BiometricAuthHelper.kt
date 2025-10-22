package it.unibo.discoverit.utils.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthHelper(private val context: Context) {

    private val biometricManager = BiometricManager.from(context)

    /**
     * Controlla se il dispositivo può autenticare tramite biometria.
     */
    fun isBiometricAvailable(): Boolean {
        return biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS
    }

    /**
     * Avvia il prompt biometrico.
     * @param activity l’Activity corrente (necessaria a BiometricPrompt)
     * @param title titolo mostrato nel dialog
     * @param subtitle sottotitolo
     * @param negativeText testo del pulsante negativo
     * @param onSuccess callback in caso di autenticazione riuscita
     * @param onError callback in caso di errore o fallimento
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        negativeText: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeText)
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    onError("Autenticazione fallita")
                }
            }
        )
        biometricPrompt.authenticate(promptInfo)
    }
}