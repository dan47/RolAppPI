package com.example.rolapppi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.rolapppi.fragments.cattle.CattleModel;
import com.example.rolapppi.fragments.cattle.CattleViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rolapppi.databinding.ActivityHomeBinding;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private CattleViewModel cattleViewModel;
    private final int CHOOSE_FILE = 1001;
    private static final int REQUEST_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cattle, R.id.nav_cropProtection, R.id.nav_feed, R.id.nav_feedProduced, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (checkSelfPermission(Manifest.permission.CAMERA) + checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Uprawnienia uzyskano", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Uprawnień nie uzyskano", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.action_settings){
            Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigate(R.id.settingsFragment);
        }

        if (item.getItemId() == R.id.action_import) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            startActivityForResult(intent, CHOOSE_FILE);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FILE && resultCode == RESULT_OK) {
            if ((data != null)) {
                File file = new File(data.getData().getPath());
                String path = file.getAbsolutePath().replace("/document/raw:", "");

                try {
                    FileReader fileReader = new FileReader(path);
                    CSVReader reader = new CSVReader(fileReader);

                    String[] line = reader.readNext();
                    String[] lineA = line;

                    String[] check = lineA[0].split(";");
                    if (check[2].contains("Numer identyfikacyjny")&&check[3].contains("Data urodzenia")
                            &&check[18].contains("Identyfikator matki")){
                        cattleViewModel = new ViewModelProvider(this).get(CattleViewModel.class);
                        while (line != null) {
                            line = reader.readNext();

                            String[] lineN = line;
                            String[] cell = lineN[0].split(";");


                            if (lineN[0].contains("PARAMETRY")) {
                                return;
                            }

                            cell[3] = cell[3].replace("-", ".");
                            cattleViewModel.cattleAdd(new CattleModel(cell[2], cell[3], cell[5], cell[18]));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Problem z odczytem pliku", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (Exception e) {
                    Log.e("DIPA", e.toString());
                }

            }
        }
    }
}