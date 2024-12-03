package com.injiwallet.keys;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import javax.crypto.KeyGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class KeyManager {

    private static final String KEY_ALIAS = "InjiWalletKey";

    // Generate a new key pair
    public static void generateKeyPair(Context context, String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyGenerator.init(keyGenParameterSpec);
            keyGenerator.generateKey();

            Log.d("KeyManager", "Key pair generated successfully");
        } catch (Exception e) {
            Log.e("KeyManager", "Error generating key pair", e);
        }
    }

    // Get the public key for a given alias
    public static PublicKey getPublicKey(String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return keyStore.getCertificate(alias).getPublicKey();
    }

    // Sign data with the private key
    public static byte[] signData(String alias, byte[] data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);

        return signature.sign();
    }
}
