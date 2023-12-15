package com.example.rhythmix.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String USER_EMAIL_EXTRA = "USER_EMAIL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(ProfileActivity.this, LibraryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                return true;
            } else return false;
        });
        // Set the default item to be the "Search" item
        bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(true);





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
