package android.com.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.com.InternetConnection;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseDriverNumber;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sdsmdg.tastytoast.TastyToast;
import com.taishi.flipprogressdialog.FlipProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final int LOCATION_PERMISSION = 1;
    private CountryCodePicker ccp;
    private EditText edMobileNo;
    private TextView tv_OneTimePasswordtext;
    private ImageView im_Next;
    public static String enteredMobileNumber;
    private long lastClickTime = 0;
    private static long back_pressed;
    FlipProgressDialog fpd;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findindIdsHere();
        listeners();

        // Hiding Action in a particular Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context = this;

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }

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

            networkConnection();


        } else {

            // Retrofit code
            networkConnection();
            flipProgress();
            HttpModule.provideRepositoryService().getDriverNumberAPI(edMobileNo.getText().toString()).enqueue(new Callback<ResponseDriverNumber>() {
                @Override
                public void onResponse(Call<ResponseDriverNumber> call, Response<ResponseDriverNumber> response) {

                    fpd.dismiss();
                    if (response.body() != null && response.body().isSuccess) {


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
                    fpd.dismiss();
                }
            });

        }

    }

    private void networkConnection() {

        // 1st way of checking connectivity
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available
            } else {
                //No internet
                TastyToast.makeText(getApplicationContext(), "There is no internet Connection..", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        } else {
            //No internet
            TastyToast.makeText(getApplicationContext(), "There is no internet Connection..", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        }

        // 2nd way of checking connectivity

//        if (InternetConnection.checkConnection(context)) {
//            // Internet Available...
//            TastyToast.makeText(getApplicationContext(), "Internet Available", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//        } else {
//            // Internet Not Available...
//            TastyToast.makeText(getApplicationContext(), "No Internet Available", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//        }

    }

    private void flipProgress() {

        // Set imageList
        List<Integer> imageList = new ArrayList<Integer>();
        imageList.add(R.drawable.foodorderfill);
        fpd = new FlipProgressDialog();

        fpd.setImageList(imageList);                              // *Set a imageList* [Have to. Transparent background png recommended]
        fpd.setCanceledOnTouchOutside(true);                      // If true, the dialog will be dismissed when user touch outside of the dialog. If false, the dialog won't be dismissed.
        fpd.setDimAmount(0.0f);                                   // Set a dim (How much dark outside of dialog)

        // About dialog shape, color
        fpd.setBackgroundColor(Color.parseColor("#e0e0e0"));      // Set a background color of dialog
        fpd.setBackgroundAlpha(0.2f);                             // Set a alpha color of dialog
        fpd.setBorderStroke(0);                                   // Set a width of border stroke of dialog
        fpd.setBorderColor(-1);                                   // Set a border stroke color of dialog
        fpd.setCornerRadius(16);                                  // Set a corner radius

        // About image
        fpd.setImageSize(200);                                    // Set an image size
        fpd.setImageMargin(10);                                   // Set a margin of image

        // About rotation
        fpd.setOrientation("rotationY");                          // Set a flipping rotation
        fpd.setDuration(600);                                     // Set a duration time of flipping ratation
        fpd.setStartAngle(0.0f);                                  // Set an angle when flipping ratation start
        fpd.setEndAngle(180.0f);                                  // Set an angle when flipping ratation end
        fpd.setMinAlpha(0.0f);                                    // Set an alpha when flipping ratation start and end
        fpd.setMaxAlpha(1.0f);                                    // Set an alpha while image is flipping


        fpd.show(getFragmentManager(), "");                        // Show flip-progress-dialg


    }


    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            TastyToast.makeText(this, "Press back again to close this app !", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        TastyToast.makeText(getApplicationContext(), "Permission Granted", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    } else {
                        finish();
                        TastyToast.makeText(getApplicationContext(), "Please Allow Permission in Settings", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }

            }

        }

    }
}


