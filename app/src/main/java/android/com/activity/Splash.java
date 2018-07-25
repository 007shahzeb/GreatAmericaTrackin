package android.com.activity;

import android.com.garytransportnew.R;
import android.com.sharedPrefrence.PreferencesManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    private volatile boolean isCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        hidingActionbar();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

//                if (!isCheck) {
//
//
//                    if (PreferencesManager.getLogin(Splash.this)) {
//
//                        Intent intent = new Intent(Splash.this, ActivityDashboard.class); // true
//                        startActivity(intent);
//                        finish();
//                        return;
//
//
//                    } else {
//
//                        Intent intent = new Intent(Splash.this, MainActivity.class); // false
//                        startActivity(intent);
//                        finish();
//                        return;
//
//                    }
//                }
//
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        }, SPLASH_TIME_OUT);
    }


    private void hidingActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isCheck = true;
    }


}
