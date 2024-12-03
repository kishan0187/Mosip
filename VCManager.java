package com.injiwallet.vcmanagement;

import android.content.Context;
import android.util.Log;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class VCManager {

    private static final String VC_PREFS = "vc_prefs";

    // Save VC (Verifiable Credential) for the current user profile
    public static boolean saveVC(Context context, String vcJson) {
        try {
            String profileId = ProfileManager.getCurrentProfileId(context);
            if (profileId == null) {
                Log.e("VCManager", "No active profile found");
                return false;
            }

            // Use EncryptedSharedPreferences to store the VC
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    VC_PREFS + profileId, 
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), 
                    context, 
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_GCM, 
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("vc", vcJson);
            editor.apply();
            return true;
        } catch (GeneralSecurityException | IOException e) {
            Log.e("VCManager", "Error saving VC", e);
            return false;
        }
    }

    // Retrieve VC for the current user profile
    public static String getVC(Context context) {
        String profileId = ProfileManager.getCurrentProfileId(context);
        if (profileId == null) {
            Log.e("VCManager", "No active profile found");
            return null;
        }

        try {
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    VC_PREFS + profileId, 
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), 
                    context, 
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_GCM, 
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            return sharedPreferences.getString("vc", null);
        } catch (GeneralSecurityException | IOException e) {
            Log.e("VCManager", "Error retrieving VC", e);
            return null;
        }
    }
}
