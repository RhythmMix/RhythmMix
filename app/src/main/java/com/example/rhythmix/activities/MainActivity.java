package com.example.rhythmix.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button press=findViewById(R.id.button3);

        press.setOnClickListener(view -> {
            Intent nn=new Intent(MainActivity.this, MainActivity2.class);
            startActivity(nn);
        });
        Button sheet=findViewById(R.id.Sheet);
        sheet.setOnClickListener(view -> {
            showBottomDialog();
        });

   
        Button pop=findViewById(R.id.openpopup);
        pop.setOnClickListener(view -> {
            openPopUP();
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);

            if (item.getItemId() == R.id.Home) {
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(MainActivity.this, LibraryActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                item.setChecked(true);
                return true;
            } else return false;
        });

        goToLogIn();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet);

        LinearLayout videoLayout = dialog.findViewById(R.id.click);
        FloatingActionButton fab = dialog.findViewById(R.id.fab);
//
//        videoLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"set here",Toast.LENGTH_SHORT).show();
//            }
//        });
        ImageView cancelButton = dialog.findViewById(R.id.cancel_button);
//

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUP();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void openPopUP() {
        PopUp popUp = new PopUp();
        popUp.show(getSupportFragmentManager(),"exp");
    }

    public void goToLogIn(){
        Button logIn = findViewById(R.id.button4);
        logIn.setOnClickListener(view -> {
            Intent goToAllSongs = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToAllSongs);
        });
    }
}

