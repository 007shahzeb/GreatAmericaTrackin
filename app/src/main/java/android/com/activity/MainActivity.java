package android.com.activity;

import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.net.RemoteRepositoryService;
import android.com.responseModel.ResponseDriverNumber;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sdsmdg.tastytoast.TastyToast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private CountryCodePicker ccp;
    private EditText edMobileNo;
    private TextView tv_OneTimePasswordtext;
    private ImageView im_Next;

    private long lastBackPressTime = 0;
    public static String enteredMobileNumber;

    private long lastClickTime = 0;
    private static long back_pressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findindIdsHere();
        listeners();

        // Hiding Action in a particular Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void listeners() {
        im_Next.setOnClickListener(this);
    }

    private void findindIdsHere() {

        ccp = findViewById(R.id.ccp);
        edMobileNo = findViewById(R.id.edMobileNo);
        tv_OneTimePasswordtext = findViewById(R.id.tv_OneTimePasswordtext);
        im_Next = findViewById(R.id.im_Next);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.im_Next: {


                performImageNextOperationHere();
            }
        }
    }

    private void performImageNextOperationHere() {

        if (edMobileNo.getText().toString().isEmpty() || edMobileNo.getText().toString().length() != 10) {
            edMobileNo.setError("Please Enter The 10 digit Mobile Number");
        } else {


            // Retrofit code

            HttpModule.provideRepositoryService().getDriverNumberAPI(edMobileNo.getText().toString()).enqueue(new Callback<ResponseDriverNumber>() {
                @Override
                public void onResponse(Call<ResponseDriverNumber> call, Response<ResponseDriverNumber> response) {

                    if (response.body().isSuccess) {

                        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                            return;
                        }
                        lastClickTime = SystemClock.elapsedRealtime();

                        enteredMobileNumber = edMobileNo.getText().toString();

                        Intent intent = new Intent(MainActivity.this, VeryficationCodeActivity.class);
                        startActivity(intent);


                    } else {
                        TastyToast.makeText(MainActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseDriverNumber> call, Throwable t) {
                    System.out.println("MainActivity.onFailure - - " + t);
                }
            });

        }

    }

//
//    @Override
//    public void onBackPressed() {
//
//        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
//            TastyToast.makeText(this, "Press back again to close this app !", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
//            this.lastBackPressTime = System.currentTimeMillis();
//        } else {
//        }
//        super.onBackPressed();
//    }


    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            TastyToast.makeText(this, "Press back again to close this app !", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}


