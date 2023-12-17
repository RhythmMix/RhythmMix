package com.example.rhythmix.Activites;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreatePlaylistActivity extends AppCompatActivity {
    public static final String CREATE_PLAY_LIST_ACTIVITY_TAG = "CreatePlaylistActivity";
    public static final String TAG = "createPlaylist";
    private CompletableFuture<List<Playlist>> playlistFuture = null;
    private CompletableFuture<Playlist> playlistCompletableFuture = null;
    private String s3ImageKey = "";
    private EditText playlistTitle;
    private String playlistBackground;
    private Playlist playlistToEdit = null;

    ActivityResultLauncher<Intent>activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        playlistFuture = new CompletableFuture<>();
        playlistCompletableFuture = new CompletableFuture<>();

        playlistTitle = findViewById(R.id.playlistNameEditText);


        Button createPlaylistButton = findViewById(R.id.createPlaylistButton);
        createPlaylistButton.setOnClickListener(v ->
                createPlaylist()

        );


        activityResultLauncher=getImagePickActivityResultLauncher();
        setUpAddImage();
        setUpSaveButton();
    }

    private ActivityResultLauncher<Intent> getImagePickActivityResultLauncher()
    {
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                ImageButton addImageButton = findViewById(R.id.playlistBackground);
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
//                                            switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);

                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );

        return imagePickingActivityResultLauncher;
    }
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri) {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    // Update s3ImageKey
                    s3ImageKey = success.getKey();

                    ImageView productImageView = findViewById(R.id.editProductImageImageView);
                    ImageButton uploadSection = findViewById(R.id.playlistBackground);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                        uploadSection.setVisibility(View.INVISIBLE);
                        productImageView.setVisibility(View.VISIBLE);

                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure -> {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }


    private void createPlaylist() {
        AuthUser authUser = Amplify.Auth.getCurrentUser();

        if (authUser != null) {

            final String[] userEmail = {""};
            Amplify.Auth.fetchUserAttributes(
                    attributes -> {
                        for (AuthUserAttribute attribute : attributes) {
                            if ("email".equals(attribute.getKey().getKeyString())) {
                                userEmail[0] = attribute.getValue();
                            }
                        }
                        buildUserAndPlaylist(authUser, userEmail[0]);
                    },
                    error -> Log.e(CREATE_PLAY_LIST_ACTIVITY_TAG, "Error fetching user attributes", error)
            );
        } else {
            Log.e(CREATE_PLAY_LIST_ACTIVITY_TAG, "User not authenticated");
        }
    }

    private void buildUserAndPlaylist(AuthUser authUser, String userEmail) {
        EditText playlistNameEditText = findViewById(R.id.playlistNameEditText);

        User user = User.builder()
                .email(userEmail)
                .id(authUser.getUserId())
                .username(authUser.getUsername())
                .userImageS3Key("")
                .build();

        String playlistName = playlistNameEditText.getText().toString();
        Log.i(CREATE_PLAY_LIST_ACTIVITY_TAG, "Playlist Name: " + playlistName);


        playlistToEdit = Playlist.builder()
                .playlistName(playlistName)
                .user(user)
                .playlistBackground(s3ImageKey)
                .build();

        // Now you can call saveProduct with the initialized playlistToEdit
        saveProduct();
    }

    private void setUpAddImage()
    {
        ImageButton uploadBackground =findViewById(R.id.playlistBackground);
        uploadBackground.setOnClickListener(view -> {
            launchImageSelectionIntent();
        });
    }
    private void launchImageSelectionIntent()
    {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png","image/jpg"});
        activityResultLauncher.launch(imageFilePickingIntent);
    }

        private void setUpSaveButton()
    {
        Button saveButton = (Button)findViewById(R.id.createPlaylistButton);
        saveButton.setOnClickListener(v ->
        {
            saveProduct();
        });
    }
    private void saveProduct() {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        User user = User.builder()
                .email(authUser.getUsername())
                .id(authUser.getUserId())
                .username("")
                .userImageS3Key("")
                .build();

        Playlist productToSave = Playlist.builder()
                .playlistName(playlistTitle.getText().toString())
                .playlistBackground(s3ImageKey)
                .user(user)
                .build();

        try {
            Amplify.API.mutate(
                    ModelMutation.create(productToSave),
                    successResponse -> {
                        Log.i(TAG, "EditProductActivity.onCreate(): created a product successfully");
                        Snackbar.make(findViewById(R.id.playlistaccess), "Product saved!", Snackbar.LENGTH_SHORT).show();
                        Intent backToPlayList=new Intent(CreatePlaylistActivity.this, PlaylistsActivity.class);
                        startActivity(backToPlayList);
                    },
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        } catch (Exception e) {
            Log.e(TAG, "Exception during mutation: " + e.getMessage(), e);
        }
    }
}