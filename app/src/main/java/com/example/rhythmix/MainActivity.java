package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.rhythmix.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rplacefragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
int d=item.getItemId();
//        switch (item.getItemId()){

if(d==R.id.Library){
    rplacefragment(new LibraryFragment());
} else if (d==R.id.Profile) {
    rplacefragment(new ProfileFragment());
}
else if (d==R.id.Search) {
    rplacefragment(new SearchFragment());
}
//            case R.id.Lib:
//                rplacefragment(new HomeFragment());
//                break;
//            case R.id.Library:
//                rplacefragment(new LibraryFragment());
//                break;
//            case R.id.Search:
//                rplacefragment(new SearchFragment());
//                break;
//            case R.id.Profile:
//                rplacefragment(new ProfileFragment());
//                break;



        return true;
    });

    }
    private void rplacefragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.frameLayout,fragment);
    fragmentTransaction.commit();
}

}