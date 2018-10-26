package android.com.activity;

import android.com.garytransportnew.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;


public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Hawk.init(this).build();


        hidingActionbar();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (Hawk.get("isFirst", false)) {


                    Intent intent = new Intent(Splash.this, ShipmenActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }


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
    }


}
