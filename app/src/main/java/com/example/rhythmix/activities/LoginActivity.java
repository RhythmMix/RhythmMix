package com.example.rhythmix.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amplifyframework.core.Amplify;
import com.example.rhythmix.R;
import com.google.android.material.textfield.TextInputEditText;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);



        TextView clickableTextView = findViewById(R.id.textView5);
        Button gradientButton = findViewById(R.id.button5);
        clickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    Intent callingIntent= getIntent();
        String email = callingIntent.getStringExtra(SignUpActivity.VERIFY_ACCOUNT_EMAIL_TAG);
        TextInputEditText usernameEditText =  findViewById(R.id.usernameEditText);
        usernameEditText.setText(email);

        Button loginButton = (Button) findViewById(R.id.button5);
        loginButton.setOnClickListener(v ->
        {
            String username = usernameEditText.getText().toString();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

            Amplify.Auth.signIn(username,
                    password,
                    success ->
                    {
                        Log.i(TAG, "Login succeeded: "+success.toString());
                        Intent goToProductListIntent= new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(goToProductListIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Login failed: "+failure.toString());
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        });

                    }
            );
        });

        Button signUpButton = (Button) findViewById(R.id.button5);
        signUpButton.setOnClickListener(v ->
        {
            Intent goToSignUpIntent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(goToSignUpIntent);
        });
    }
}