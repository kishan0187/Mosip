package com.injiwallet.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import com.injiwallet.keys.KeyManager;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class ProfileManager {

    private static final String PROFILE_PREFS = "profile_prefs";
    private static final String PROFILE_ID_KEY = "profile_id";

    // Create a unique profile ID and cryptographic key pair for each user
    public static String createNewProfile(Context context) {
        try {
            String profileId = UUID.randomUUID().toString();
            String keyAlias = "profile_key_" + profileId;

            // Generate cryptographic keys
            KeyManager.generateKeyPair(context, keyAlias);

            // Save profile ID securely using EncryptedSharedPreferences
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    PROFILE_PREFS, 
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), 
                    context, 
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_GCM, 
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PROFILE_ID_KEY, profileId);
            editor.apply();

            return profileId;
        } catch (GeneralSecurityException | IOException e) {
            Log.e("ProfileManager", "Error creating new profile", e);
            return null;
        }
    }

    // Get the current profile ID
    public static String getCurrentProfileId(Context context) {
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                PROFILE_PREFS, 
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), 
                context, 
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_GCM, 
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        return sharedPreferences.getString(PROFILE_ID_KEY, null);
    }
}
