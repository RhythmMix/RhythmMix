package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "homeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpLoginAndLogoutButton();

        Button go =findViewById(R.id.move);
        go.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this,ListsActivity.class);
            startActivity(intent);
        });
    }

    private void setUpLoginAndLogoutButton() {
        Button loginButton = findViewById(R.id.loginButton);
        if (Amplify.Auth.getCurrentUser() != null) {
            loginButton.setText("Logout");
        } else {
            loginButton.setText("Login");
        }
        loginButton.setOnClickListener(v -> {
            if (Amplify.Auth.getCurrentUser() != null) {
                Amplify.Auth.signOut(
                        () -> {
                            Log.i(TAG, "Logout succeeded");
                            runOnUiThread(() -> {
                                ((TextView) findViewById(R.id.usernameTextView)).setText("");
                                ((TextView) findViewById(R.id.emailTextView)).setText("");
                            });
                            loginButton.setText("Login");
                            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(goToLogInIntent);
                        },
                        failure -> {
                            Log.i(TAG, "Logout failed");
                            runOnUiThread(() ->
                                    Toast.makeText(MainActivity.this, "Logout failed", Toast.LENGTH_LONG).show());
                        }
                );
            } else {
                Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(goToLogInIntent);
            }
        });
    }
}
