package it.example.applicazioneufficiale.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import it.example.applicazioneufficiale.LoginActivity;
import it.example.applicazioneufficiale.MainActivity;
import it.example.applicazioneufficiale.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final Intent intent = new Intent(this, LoginActivity.class);


        Thread timer = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();

    }

}



