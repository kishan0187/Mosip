package com.injiwallet.faceauthentication;

import android.content.Context;
import android.hardware.biometrics.BiometricManager;
import android.util.Log;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import java.util.concurrent.Executor;

public class FaceAuthenticationManager {

    private static final String TAG = "FaceAuthManager";

    // Verify if face authentication is supported
    public static boolean isFaceAuthenticationSupported(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    // Authenticate the user using face recognition
    public static void authenticateUser(Context context, Executor executor, BiometricPrompt.AuthenticationCallback callback) {
        if (!isFaceAuthenticationSupported(context)) {
            Log.e(TAG, "Face authentication not supported.");
            return;
        }

        BiometricPrompt biometricPrompt = new BiometricPrompt((MainActivity) context, executor, callback);
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Face Authentication")
                .setSubtitle("Please authenticate using your face")
                .setDeviceCredentialAllowed(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
