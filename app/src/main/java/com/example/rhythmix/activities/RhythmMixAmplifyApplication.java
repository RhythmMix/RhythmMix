package com.example.rhythmix.activities;
import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class RhythmMixAmplifyApplication extends Application{
    public static final String TAG = "rhythmMixPlugins";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
        }catch (AmplifyException amplifyException){
            Log.e(TAG, "Error Initializing Amplify " + amplifyException.getMessage(), amplifyException);
        }
    }
}
