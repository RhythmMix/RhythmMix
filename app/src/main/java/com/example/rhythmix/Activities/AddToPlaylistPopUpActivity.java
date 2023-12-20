package com.example.rhythmix.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.Activities.PlaylistsActivity;
import com.example.rhythmix.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AddToPlaylistPopUpActivity extends AppCompatDialogFragment {
    public static final String CREATE_PLAY_LIST_ACTIVITY_TAG = "CreatePlaylistActivity";
    public static final String TAG = "createPlaylist";

    private CompletableFuture<List<Playlist>> playlistFuture = null;
    private CompletableFuture<Playlist> playlistCompletableFuture = null;
    private String s3ImageKey = "";
    private EditText playlistTitle;
    private String playlistBackground;
    private Playlist playlistToEdit = null;
    private View view; // Declare view variable

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_add_to_playlist_pop_up, container, false);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call the method to register for activity result
        activityResultLauncher = getImagePickActivityResultLauncher();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.activity_add_to_playlist_pop_up, null);
        builder.setView(view);
        builder
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle Cancel button click
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playlistFuture = new CompletableFuture<>();
                        playlistCompletableFuture = new CompletableFuture<>();
                        playlistTitle = view.findViewById(R.id.playlistNameEditText);
                        setUpAddImage();
                        setUpSaveButton();
                        Button createPlaylistButton = view.findViewById(R.id.createPlaylistButton);
                        createPlaylistButton.setOnClickListener(v ->
                                createPlaylist()
                        );
                        // Do not call createPlaylist() here, as it is called in the positive button click of the dialog
                    }
                });

        return builder.create();
    }

    private ActivityResultLauncher<Intent> getImagePickActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        ImageButton addImageButton = view.findViewById(R.id.playlistBackground);
                        if (result.getResultCode() == getActivity().RESULT_OK) {
                            if (result.getData() != null) {
                                Uri pickedImageFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getActivity().getContentResolver().openInputStream(pickedImageFileUri);
                                    String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file on the phone! Filename is: " + pickedImageFilename);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);

                                } catch (FileNotFoundException fnfe) {
                                    Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                }
                            }
                        } else {
                            Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                        }
                    }
                }
        );
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
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

                    ImageView productImageView = view.findViewById(R.id.editProductImageImageView);
                    ImageButton uploadSection = view.findViewById(R.id.playlistBackground);
                    InputStream pickedImageInputStreamCopy = null;
                    try {
                        pickedImageInputStreamCopy = getActivity().getContentResolver().openInputStream(pickedImageFileUri);
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
        EditText playlistNameEditText = view.findViewById(R.id.playlistNameEditText);

        User user = User.builder()
                .email(userEmail)
                .id(authUser.getUserId())
                .username(authUser.getUsername())
                .userImageS3Key("")
                .build();

        String playlistName = playlistNameEditText.getText().toString();
        Log.i(CREATE_PLAY_LIST_ACTIVITY_TAG, "Playlist Name: " + playlistName);

        Playlist playlistToEdit = Playlist.builder()
                .playlistName(playlistName)
                .user(user)
                .playlistBackground(s3ImageKey)
                .build();

        savePlaylist(playlistToEdit);
    }
    private void savePlaylist(Playlist playlistToSave) {
        Amplify.API.mutate(
                ModelMutation.create(playlistToSave),
                successResponse -> {
                    Log.i(TAG, "Playlist saved successfully");
                    Snackbar.make(view.findViewById(R.id.playlistaccess), "Playlist saved!", Snackbar.LENGTH_SHORT).show();
                    // Update this to navigate back to the PlaylistsActivity
                    Intent backToPlayList = new Intent(getActivity(), PlaylistsActivity.class);
                    startActivity(backToPlayList);
                },
                failureResponse -> Log.e(TAG, "Failed to save playlist: " + failureResponse)
        );
    }
    private void setUpAddImage() {
        ImageButton uploadBackground = view.findViewById(R.id.playlistBackground);
        uploadBackground.setOnClickListener(view -> {
            launchImageSelectionIntent();
        });
    }

    private void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg"});
        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private void setUpSaveButton() {
        Button saveButton = view.findViewById(R.id.createPlaylistButton);
        saveButton.setOnClickListener(v -> {
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
                        Snackbar.make(view.findViewById(R.id.playlistaccess), "Product saved!", Snackbar.LENGTH_SHORT).show();
                        // Update this to navigate back to the PlaylistsActivity
                        Intent backToPlayList = new Intent(getActivity(), PlaylistsActivity.class);
                        startActivity(backToPlayList);
                    },
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        } catch (Exception e) {
            Log.e(TAG, "Exception during mutation: " + e.getMessage(), e);
        }
    }
}
