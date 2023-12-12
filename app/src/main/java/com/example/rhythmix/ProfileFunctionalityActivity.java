package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;

public class ProfileFunctionalityActivity extends AppCompatActivity {
    public static final String TAG = ProfileFunctionalityActivity.class.getSimpleName();
    public static final String USER_EMAIL_EXTRA = "USER_EMAIL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);

        final String[] username = {""};
        final String[] userEmail = {""};

        Amplify.Auth.fetchUserAttributes(
                success -> {
                    Log.i(TAG, "Fetch user attributes succeeded");

                    for (AuthUserAttribute userAttribute : success) {
                        String key = userAttribute.getKey().getKeyString();
                        String value = userAttribute.getValue();

                        if ("nickname".equals(key)) {
                            username[0] = value;
                        } else if ("email".equals(key)) {
                            userEmail[0] = value;
                        }
                    }

                    runOnUiThread(() -> {
                        usernameTextView.setText(username[0]);
                        emailTextView.setText(userEmail[0]);
                    });
                },
                failure -> {
                    Log.e(TAG, "Fetch user attributes failed: " + failure.toString());
                }
        );
    }
}
