package com.injiwallet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.injiwallet.profile.ProfileManager;
import com.injiwallet.faceauthentication.FaceAuthenticationManager;
import com.injiwallet.vcmanagement.VCManager;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new profile and authenticate using face recognition
        String profileId = ProfileManager.createNewProfile(this);
        if (profileId != null) {
            Executor executor = Executors.newSingleThreadExecutor();
            FaceAuthenticationManager.authenticateUser(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    // Authentication succeeded, proceed with VC management
                    String vc = VCManager.getVC(MainActivity.this);
                    if (vc != null) {
                        // Do something with the VC
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });
        }
    }
}
