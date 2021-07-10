package it.example.applicazioneufficiale;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import it.example.applicazioneufficiale.databinding.ActivityMainBinding;
import it.example.applicazioneufficiale.ui.charts.ChartsActivity;
import it.example.applicazioneufficiale.ui.film.FilmActivity;
import it.example.applicazioneufficiale.ui.logout.LogoutActivity;
import it.example.applicazioneufficiale.ui.series.SeriesActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_film_Activity, R.id.nav_series_Activity, R.id.nav_aboutUs, R.id.nav_logout_Activity, R.id.nav_charts_Activity)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //TODO implementare lo switch al posto dei singoli metodi

        navigationView.getMenu().findItem(R.id.nav_charts).setOnMenuItemClickListener(item -> {
            drawer.close();
            Intent i = new Intent(MainActivity.this, ChartsActivity.class);
            startActivity(i);
            return false;
        });


        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            drawer.close();
            Intent i = new Intent(MainActivity.this, LogoutActivity.class);
            startActivity(i);
            return false;
        });

        navigationView.getMenu().findItem(R.id.nav_film).setOnMenuItemClickListener(item -> {
            drawer.close();
            Intent i = new Intent(MainActivity.this, FilmActivity.class);
            startActivity(i);
            return false;
        });

        navigationView.getMenu().findItem(R.id.nav_series).setOnMenuItemClickListener(item -> {
            drawer.close();
            Intent i = new Intent(MainActivity.this, SeriesActivity.class);
            startActivity(i);
            return false;
        });
    }





    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



/*

    public static void redirectActivity(Activity activity, Class aClass) {
        //inizialize Intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }


     */

    }