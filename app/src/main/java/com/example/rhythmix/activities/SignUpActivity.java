package com.example.rhythmix.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.rhythmix.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    public EditText passwordEditText;
    public static final String USER_EMAIL_EXTRA = "USER_EMAIL";

    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "Verify_Account_Email_Tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView textView5=findViewById(R.id.textView5);
        textView5.setOnClickListener(view -> {
            Intent moveToSignUp=new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(moveToSignUp);
        });

        Button verifyButton = findViewById(R.id.verify);
        EditText verifyCode = findViewById(R.id.codeEdit);
        Button signupSubmitButton = findViewById(R.id.signUp);
        passwordEditText = findViewById(R.id.passwordEditText);
        TextInputLayout textInputLayout=findViewById(R.id.textInputLayoutcode);

        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        verifyButton.setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
            String verificationCode = verifyCode.getText().toString();

            if (verificationCode.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter a verification code", Toast.LENGTH_LONG).show();
                return;
            }

            Amplify.Auth.confirmSignUp(username,
                    verificationCode,
                    god -> {
                        Log.i(TAG, "Verification succeeded: " + god.toString());
                        Intent goToLogin =new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(goToLogin);
                        fetchUserAttributesAndNavigate(username);
                    },
                    failure -> {
                        Log.i(TAG, "Verification failed: " + failure.toString());
                        runOnUiThread(() -> {
                            Toast.makeText(SignUpActivity.this, "Verify account failed!!", Toast.LENGTH_LONG).show();
                        });
                    }
            );
        });
        signupSubmitButton.setOnClickListener(v -> {
            verifyCode.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.VISIBLE);
            signupSubmitButton.setVisibility(View.INVISIBLE);
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
            String useyy = ((EditText) findViewById(R.id.userName)).getText().toString();

            Log.i("Amplify", "email: " + username);
            Log.i("Amplify", "username: " + useyy);
            Log.i("Amplify", "password: " + password);


            Amplify.Auth.signUp(username,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .userAttribute(AuthUserAttributeKey.nickname(), useyy)
                            .build(),
                    good -> {
                        Log.i(TAG, "Signup succeeded: " + good.toString());
                    },
                    bad -> {
                        Log.i(TAG, "Signup failed with username: " + username + " with this message: " + bad.toString());
                        runOnUiThread(() -> {
                            Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_LONG).show();
                        });
                    }
            );
        });
    }

    private void togglePasswordVisibility(View view) {
        int inputType = passwordEditText.getInputType();

        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void fetchUserAttributesAndNavigate(String username) {
        Log.i(TAG, "Fetching user attributes for username: " + username);

        Amplify.Auth.fetchUserAttributes(
                attributes -> {
                    Log.i(TAG, "User attributes fetched successfully");

                    String userEmail = "";
                    for (AuthUserAttribute attribute : attributes) {
                        if (attribute.getKey().equals(AuthUserAttributeKey.email())) {
                            userEmail = attribute.getValue();
                            break;
                        }
                    }

                    if (!userEmail.isEmpty()) {
                        Log.i(TAG, "User email: " + userEmail);

                        Intent goToProfileIntent = new Intent(SignUpActivity.this, ProfileActivity.class);
                        goToProfileIntent.putExtra(ProfileActivity.USER_EMAIL_EXTRA, userEmail);

                        startActivity(goToProfileIntent);

                        finish();
                    } else {
                        Log.e(TAG, "User email is empty");
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching user attributes: " + error.toString());
                }
                );
}


}