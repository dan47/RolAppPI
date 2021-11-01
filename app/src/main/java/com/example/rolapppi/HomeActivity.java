package com.example.rolapppi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.example.rolapppi.ui.cattle.CattleModel;
import com.example.rolapppi.ui.cattle.CattleViewModel;
import com.example.rolapppi.ui.cropProtection.CropProtectionModel;
import com.example.rolapppi.ui.cropProtection.CropProtectionViewModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  Log.e("DD", String.valueOf(position));
//                final Dialog dialog = new Dialog(HomeActivity.this);
//                //We have added a title in the custom layout. So let's disable the default title.
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
//                dialog.setCancelable(true);
//                //Mention the name of the layout of your custom dialog.
//                dialog.setContentView(R.layout.custom_dialog);
//
//                //Initializing the views of the dialog.
//                final EditText nameEt = dialog.findViewById(R.id.name_et);
//                final EditText ageEt = dialog.findViewById(R.id.age_et);
//                final CheckBox termsCb = dialog.findViewById(R.id.terms_cb);
//                Button submitButton = dialog.findViewById(R.id.submit_button);
//
//
//                submitButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String name = nameEt.getText().toString();
//                        String age = ageEt.getText().toString();
//                        Boolean hasAccepted = termsCb.isChecked();R.o
//                        // populateInfoTv(name,age,hasAccepted);
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
//                Snackbar.make(view, "dd", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
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
                    cattleViewModel = new ViewModelProvider(this).get(CattleViewModel.class);
                    while (line != null) {
                        line = reader.readNext();
                        // nextLine[] is an array of values from the line
                        String[] lineN = line;
                        String[] dd = lineN[0].split(";");

                        if (lineN[0].contains("PARAMETRY")) {
                            return;
                        }
                        dd[3] = dd[3].replace("-", ".");
                        cattleViewModel.cattleAdd(new CattleModel(dd[2], dd[3], dd[5], dd[18]));
                    }
                } catch (Exception e) {
                    Log.e("DIPA", e.toString());
                }

            }
        }
    }
}