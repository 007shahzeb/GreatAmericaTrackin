package android.com.activity;


import android.com.InternetConnection;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseDriverNumber;
import android.com.responseModel.ResponseOtpNumber;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.sdsmdg.tastytoast.TastyToast;
import com.taishi.flipprogressdialog.FlipProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VeryficationCodeActivity extends AppCompatActivity {

    private EditText mEd1,
            mEd2,
            mEd3,
            mEd4;

    private TextView tv_OtpCode;
    private RelativeLayout rlone, rltwo, rlthree, rlfour, rlfive, rlsix, rlseven, rleight, rlnine, rldone, rlzero, rldelete;

    int userId;
    private long lastClickTime = 0;
    FlipProgressDialog fpd;
    KProgressHUD hud;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.edittextlinecolor));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.verification_code_layout);


        if (getIntent() != null) {
            if (getIntent().hasExtra("drivernumber")) {
                tv_OtpCode = findViewById(R.id.tv_OtpCode);
                tv_OtpCode.setText(getIntent().getStringExtra("drivernumber"));
            }
            if (getIntent().hasExtra("id")) {
                userId = (Integer) getIntent().getIntExtra("id", 0);
            }

        }

        hud = new KProgressHUD(this);

        findingIdsHere();

        // Hiding Action in a particular Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mEd1.setInputType(InputType.TYPE_NULL);
        mEd2.setInputType(InputType.TYPE_NULL);
        mEd3.setInputType(InputType.TYPE_NULL);
        mEd4.setInputType(InputType.TYPE_NULL);

        context = this;


    }

    private void findingIdsHere() {



        rlone = findViewById(R.id.rlone);
        rltwo = findViewById(R.id.rltwo);
        rlthree = findViewById(R.id.rlthree);
        rlfour = findViewById(R.id.rlfour);
        rlfive = findViewById(R.id.rlfive);
        rlsix = findViewById(R.id.rlsix);
        rlseven = findViewById(R.id.rlseven);
        rleight = findViewById(R.id.rleight);
        rlnine = findViewById(R.id.rlnine);
        rldone = findViewById(R.id.rldone);
        rlzero = findViewById(R.id.rlzero);
        rldelete = findViewById(R.id.rldelete);


        mEd1 = findViewById(R.id.ed1);
        mEd2 = findViewById(R.id.ed2);
        mEd3 = findViewById(R.id.ed3);
        mEd4 = findViewById(R.id.ed4);

    }


    public void Keybutton(View view) {

        switch (view.getId()) {

            case R.id.rlone:

                if (mEd1.getText().toString().length() == 0) {
                    mEd1.setText(getString(R.string.one));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }

                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.one));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }

                } else if (mEd3.getText().toString().length() == 0) {
                    mEd3.setText(getString(R.string.one));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.one));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();

                    }
                }

                break;


            case R.id.rltwo:
                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.two));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.two));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }


                } else if (mEd3.getText().toString().length() == 0) {
                    mEd3.setText(getString(R.string.two));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }


                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.two));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));


                    }
                }

                break;


            case R.id.rlthree:

                if (mEd1.getText().toString().length() == 0) {
                    mEd1.setText(getString(R.string.three));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.three));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }


                } else if (mEd3.getText().toString().length() == 0) {
                    mEd3.setText(getString(R.string.three));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }


                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.three));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();


                    }
                }
                break;


            case R.id.rlfour:
                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.four));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }

                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.four));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }

                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.four));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {
                    mEd4.setText(getString(R.string.four));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();


                    }
                }

                break;


            case R.id.rlfive:

                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.five));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.five));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }


                } else if (mEd3.getText().toString().length() == 0) {
                    mEd3.setText(getString(R.string.five));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {
                    mEd4.setText(getString(R.string.five));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();

                    }
                }
                break;


            case R.id.rlsix:

                if (mEd1.getText().toString().length() == 0) {
                    mEd1.setText(getString(R.string.sixe));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }

                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.sixe));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }


                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.sixe));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.sixe));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();


                    }
                }
                break;


            case R.id.rlseven:

                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.seven));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.seven));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }

                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.seven));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.seven));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();


                    }
                }
                break;


            case R.id.rleight:

                if (mEd1.getText().toString().length() == 0) {
                    mEd1.setText(getString(R.string.eight));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {

                    mEd2.setText(getString(R.string.eight));
                    mEd3.requestFocus();
                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }

                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.eight));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.eight));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();

                    }
                }
                break;


            case R.id.rlnine:
                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.nine));
                    mEd2.requestFocus();


                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.nine));
                    mEd3.requestFocus();
                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();
                    }

                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.nine));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }

                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.nine));
                    mEd4.requestFocus();

                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }
                }
                break;


            case R.id.rlzero:
                if (mEd1.getText().toString().length() == 0) {

                    mEd1.setText(getString(R.string.zero));
                    mEd2.requestFocus();

                    if (mEd1.getText().toString().length() == 1) {
                        mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd2.requestFocus();
                    }
                } else if (mEd2.getText().toString().length() == 0) {
                    mEd2.setText(getString(R.string.zero));
                    mEd3.requestFocus();

                    if (mEd2.getText().toString().length() == 1) {
                        mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd3.requestFocus();

                    }

                } else if (mEd3.getText().toString().length() == 0) {

                    mEd3.setText(getString(R.string.zero));
                    mEd4.requestFocus();

                    if (mEd3.getText().toString().length() == 1) {
                        mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();
                    }


                } else if (mEd4.getText().toString().length() == 0) {

                    mEd4.setText(getString(R.string.zero));

                    mEd4.requestFocus();
                    if (mEd4.getText().toString().length() == 1) {
                        mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_upcoming));
                        mEd4.requestFocus();

                    }
                }

                break;


            case R.id.rldelete:


                if (mEd4.getText().toString().length() > 0) {

                    mEd4.setText("");
                    mEd3.requestFocus();
                    mEd4.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_white));
//                    mEd4.setError(null);

                } else if (mEd3.getText().toString().length() > 0) {
                    mEd3.setText("");
                    mEd2.requestFocus();
                    mEd3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_white));
                    mEd3.requestFocus();
//                    mEd3.setError(null);

                } else if (mEd2.getText().toString().length() > 0) {

                    mEd2.setText("");
                    mEd1.requestFocus();
                    mEd2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_white));
                    mEd1.requestFocus();
//                    mEd2.setError(null);

                } else if (mEd1.getText().toString().length() > 0) {
                    mEd1.setText("");
                    mEd1.requestFocus();
                    mEd1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_button_white));
                    mEd1.requestFocus();
//                    mEd1.setError(null);

                }

                break;


            case R.id.rldone:


                if (mEd1.getText().toString().length() == 0 && mEd2.getText().toString().length() == 0 && mEd3.getText().toString().length() == 0 && mEd4.getText().toString().length() == 0) {
                    mEd1.setError("Enter otp");
                    mEd2.setError("Enter otp");
                    mEd3.setError("Enter otp");
                    mEd4.setError("Enter otp");


                } else if (mEd1.getText().toString().length() == 1 && mEd2.getText().toString().length() == 0 && mEd3.getText().toString().length() == 0 && mEd4.getText().toString().length() == 0) {
                    mEd1.setError(null);
                    mEd2.setError("Enter otp");
                    mEd3.setError("Enter otp");
                    mEd4.setError("Enter otp");
                } else if (mEd1.getText().toString().length() == 1 && mEd2.getText().toString().length() == 1 && mEd3.getText().toString().length() == 0 && mEd4.getText().toString().length() == 0) {

                    mEd1.setError(null);
                    mEd2.setError(null);
                    mEd3.setError("Enter otp");
                    mEd4.setError("Enter otp");
                } else if (mEd1.getText().toString().length() == 1 && mEd2.getText().toString().length() == 1 && mEd3.getText().toString().length() == 1 && mEd4.getText().toString().length() == 0) {

                    mEd1.setError(null);
                    mEd2.setError(null);
                    mEd3.setError(null);
                    mEd4.setError("Enter otp");

                } else if (mEd1.getText().toString().length() == 1 && mEd2.getText().toString().length() == 1 && mEd3.getText().toString().length() == 1 && mEd4.getText().toString().length() == 1) {
                    mEd1.setError(null);
                    mEd2.setError(null);
                    mEd3.setError(null);
                    mEd4.setError(null);


//                     Retrofit code
                    networkConnection();
                    flipProgress();
//                    pleaseWaitDialog();


                    HttpModule.provideRepositoryService().getOtpNumberAPI(mEd1.getText().toString() + mEd2.getText().toString() + mEd3.getText().toString() + mEd4.getText().toString()).enqueue(new Callback<ResponseOtpNumber>() {
                        @Override
                        public void onResponse(Call<ResponseOtpNumber> call, Response<ResponseOtpNumber> response) {
                            fpd.dismiss();
//                            hud.dismiss();
                            if (response.body() != null) {

                                if (response.body().isSuccess) {
                                    fpd.dismiss();
//                                    hud.dismiss();
                                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                                        return;
                                    }
                                    lastClickTime = SystemClock.elapsedRealtime();

                                    System.out.println("VeryficationCodeActivity.onResponse - - - Testing heer ");
                                    Intent intent = new Intent(VeryficationCodeActivity.this, ShipmenActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TastyToast.makeText(VeryficationCodeActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseOtpNumber> call, Throwable t) {
                            System.out.println("VeryficationCodeActivity.onFailure - - " + t);
                            t.printStackTrace();
                            fpd.dismiss();
//                            hud.dismiss();
                        }
                    });

                }
                break;

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
//            TastyToast.makeText(getApplicationContext() , "Internet Available" , TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//        } else {
//            // Internet Not Available...
//            TastyToast.makeText(getApplicationContext() , "No Internet Available" , TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//        }

    }


    private void pleaseWaitDialog() {


        hud = KProgressHUD.create(VeryficationCodeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

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
        super.onBackPressed();

    }
}




