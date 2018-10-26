package android.com.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.com.DonationOption;
import android.com.adapters.SimpleItem;
import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.com.net.HttpModule;
import android.com.netStatus.AppStatus;
import android.com.receivers.NetworkChangeReceiver;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseReachedCheckStatus;
import android.com.responseModel.ResponseShipmentInformation;
import android.com.responseModel.ResponseShipmentList;
import android.com.responseModel.ShipmentDetail;
import android.com.responseModel.nearByCheckAPI.NearByCheck;
import android.com.service.LocationService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Location mLocation;
    TextView latLng;
    GoogleApiClient mGoogleApiClient = null;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    private RecyclerView recycler_view;
    public static FastItemAdapter<SimpleItem> fastAdapter;
    String message;
    String orderId;
    String rEceiverId;
    String receiverlatitude;
    String receiverLongitude;

    FlipProgressDialog fpd;
    KProgressHUD hud;
    private Context context;

    private final String TAG = "ShipmenActivity";
    private Location mLastLocation;
    private LocationCallback mLocationCallback;

    public Boolean reachedStatus = false;
    private SpotsDialog dialog = null;
    private SimpleItem itemChange;
    private long lastClickTime = 0;
    int pos;

    TextView tvLogout;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    TextView tvNo, tvYes;

    TextView tvNoInternet;
    ImageView imageOops;


    ArrayList<ShipmentDetail> shipmentDetails = new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecive(String data) {    //123,123,12,4

        String arr[] = data.split(",");
        orderId = arr[0];
        rEceiverId = arr[1];

        receiverlatitude = arr[2];
        receiverLongitude = arr[3];

        if (mGoogleApiClient == null) {


            buildGoogleApiClient();
            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        if (AppStatus.getInstance(this).isOnline()) {

            TastyToast.makeText(getApplicationContext(), "You are connected now !!!", 1, TastyToast.SUCCESS).show();
            tvNoInternet.setVisibility(View.GONE);
            imageOops.setVisibility(View.GONE);
        } else {

            TastyToast.makeText(getApplicationContext(), "Internet Connection is required !!!", 1, TastyToast.WARNING).show();
            tvNoInternet.setVisibility(View.VISIBLE);
            imageOops.setVisibility(View.VISIBLE);
        }

        performingOperationsHere();


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), intentFilter);
    }

    private List<DonationOption> loadDonationOptions() {
        List<DonationOption> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.donations);
        for (String op : raw) {
            String[] info = op.split("%");
            result.add(new DonationOption(info[1], info[0]));
        }
        return result;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipmen_layout);


        Hawk.init(this).build();

        Hawk.put("isFirst", true);

        flipProgress();
        findingIdsHere();


        try {
            // Hiding Action in a particular Activity
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } finally {

        }
        hud = new KProgressHUD(this);

        context = this;


    }


    private void findingIdsHere() {

        recycler_view = findViewById(R.id.recycler_view);
        tvLogout = findViewById(R.id.tvLogout);

        tvNoInternet = findViewById(R.id.tvNoInternet);
        imageOops = findViewById(R.id.imageOops);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logoutAlertDialog();


            }
        });
    }

    private void logoutAlertDialog() {

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_logout, null);


        findingLogoutDialodIdsHere(dialogView);


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private void findingLogoutDialodIdsHere(View dialogView) {

        tvNo = dialogView.findViewById(R.id.tvNo);
        tvYes = dialogView.findViewById(R.id.tvYes);

        takingClicksOfLogout();


    }

    private void takingClicksOfLogout() {


        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutTheDriver();


            }
        });


    }

    private void logoutTheDriver() {

        Hawk.put("isFirst", false);

        Intent intent = new Intent(ShipmenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void performingOperationsHere() {


        String FULLNUMBER = Hawk.get("FULL_NUMBER");

        HttpModule.provideRepositoryService().getShipmentListAPI(FULLNUMBER).enqueue(new Callback<ResponseShipmentList>() {
            @Override
            public void onResponse(Call<ResponseShipmentList> call, Response<ResponseShipmentList> response) {
                fpd.dismiss();

                if (response.body() != null) {

                    if (response.body().isSuccess) {

                        if (response.body().shipmentDetail.size() > 0) {


                            for (int i = 0; i < response.body().shipmentDetail.size(); i++) {

                                if (i == 0 && response.body().shipmentDetail.get(i).getStatusid() != 6) {

                                    Hawk.put("orderId", response.body().shipmentDetail.get(i).getOrderid());

                                    fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).getOrderid() + "", response.body().shipmentDetail.get(i).getStatusid(), true, response.body().shipmentDetail.get(i).getRcrecieverId(), response.body().shipmentDetail.get(i).getRclat(), response.body().shipmentDetail.get(i).getRclang())));
                                } else {
                                    fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).getOrderid() + "", response.body().shipmentDetail.get(i).getStatusid(), false, response.body().shipmentDetail.get(i).getRcrecieverId(), response.body().shipmentDetail.get(i).getRclat(), response.body().shipmentDetail.get(i).getRclang())));
                                }
                                Log.i(TAG, "onResponse: ");
                            }

                        }
                    } else {
                        TastyToast.makeText(ShipmenActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseShipmentList> call, Throwable t) {
                fpd.dismiss();
                t.printStackTrace();
            }


        });

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        fastAdapter = new FastItemAdapter<>();
        recycler_view.setAdapter(fastAdapter);

        fastAdapter.withSelectable(true);

        fastAdapter.withEventHook(new ClickEventHook<SimpleItem>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(final View v, final int position, final FastAdapter<SimpleItem> fastAdapter1, final SimpleItem item) {


                itemChange = item;
                pos = position;
                orderId = item.getShipment().getShipMentNo();
                switch (v.getId()) {


                    case R.id.upload_file:
                        break;

                    case R.id.tv_Accept:

                        // SHahzeb testing

//                        if (item.getShipment().getTextViewState().getTextLable().equalsIgnoreCase("ON THE WAY")) {
//
//
//                            // Location prompt starts
//
//                            final String REQUEST_CHECK_SETTINGS = "1";
//
//                            if (mGoogleApiClient == null) {
//                                mGoogleApiClient = new GoogleApiClient.Builder(context)
//                                        .addApi(LocationServices.API).build();
//
//                                mGoogleApiClient.connect();
//
//                            } else {
//                                try {
//                                    LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//
//                            LocationRequest locationRequest = LocationRequest.create();
//                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                            locationRequest.setInterval(10000);
//                            locationRequest.setFastestInterval(10000 / 2);
//
//                            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//                            builder.setAlwaysShow(true);
//
//                            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//                            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                                @Override
//                                public void onResult(LocationSettingsResult result) {
//                                    final Status status = result.getStatus();
//                                    switch (status.getStatusCode()) {
//                                        case LocationSettingsStatusCodes.SUCCESS:
//                                            Log.i(TAG, "All location settings are satisfied.");
//                                            break;
//                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
//
//                                            try {
//                                                // Show the dialog by calling startResolutionForResult(), and check the result
//                                                // in onActivityResult().
//                                                status.startResolutionForResult(ShipmenActivity.this, Integer.parseInt(REQUEST_CHECK_SETTINGS));
//                                            } catch (IntentSender.SendIntentException e) {
//                                                Log.i(TAG, "PendingIntent unable to execute request.");
//                                            }
//                                            break;
//                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                                            break;
//                                    }
//                                }
//                            });
//
//                            // Location prompt end here
//


                        // Shahzeb test


                    case R.id.reject:

                        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                                .setTopColorRes(R.color.white)
                                .setButtonsColorRes(R.color.black)
                                .setIcon(R.drawable.dialog_icon)
                                .setIconTintColor(R.color.reachedColorCode)
                                .setMessageGravity(Gravity.CENTER_HORIZONTAL)
//                                .setTopTitle("Great America Tracking")
                                .setTitle(R.string.donate_title)
                                .setTitleGravity(Gravity.CENTER_HORIZONTAL)


                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        rejectingOrder();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TastyToast.makeText(context, "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                    }
                                })
                                .show();


                        break;

                    case R.id.information:


                        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                            return;
                        }
                        lastClickTime = SystemClock.elapsedRealtime();

                        flipProgress();
                        HttpModule.provideRepositoryService().getShipperReciverListAPI(orderId).enqueue(new Callback<ResponseShipmentInformation>() {

                            @Override
                            public void onResponse(Call<ResponseShipmentInformation> call, Response<ResponseShipmentInformation> response) {

                                fpd.dismiss();
                                if (response.body() != null && response.body().isSuccess) {


                                    if (!response.body().shipperList.equals(null) && !response.body().shipperList.equals("")) {

                                        try {
                                            Intent intent1 = new Intent(ShipmenActivity.this, ShipmentOrderListInformation.class);
                                            intent1.putExtra("shipmemntlist", (Serializable) response.body().shipperList);
                                            intent1.putExtra("recivierlist", (Serializable) response.body().receiverList);
                                            startActivity(intent1);

                                        } catch (Exception e) {
                                            System.out.println("ShipmenActivity.onResponse - -EXCEPTION IS  -" + e);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseShipmentInformation> call, Throwable t) {
                                System.out.println("ShipmenActivity.onFailure - - -" + t);
                                fpd.dismiss();
                            }
                        });
                        break;
                }
            }

            @Nullable
            @Override
            public List<View> onBindMany(RecyclerView.ViewHolder viewHolder) {
                List<View> views = new ArrayList<>();
                views.add(((SimpleItem.ViewHolder) viewHolder).information);
                views.add(((SimpleItem.ViewHolder) viewHolder).reject);
                return views;
            }
        });

    }

    private void rejectingOrder() {

        HttpModule.provideRepositoryService().getOrderRejectedAPI(orderId).enqueue(new Callback<ResponseGetOrderRejected>() {
            @Override
            public void onResponse(Call<ResponseGetOrderRejected> call, Response<ResponseGetOrderRejected> response) {

                if (response.body().isSuccess) {


                    TastyToast.makeText(context, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    performingOperationsHere();

                } else {

                    TastyToast.makeText(context, "Getting False Value", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetOrderRejected> call, Throwable t) {

                System.out.println("ShipmenActivity.onFailure - - " + t);

            }
        });


    }


    private void flipProgress() {

        // Set imageList
        List<Integer> imageList = new ArrayList<Integer>();
        imageList.add(R.drawable.foodorderfill);
        fpd = new FlipProgressDialog();

        fpd.setImageList(imageList);
        fpd.setCanceledOnTouchOutside(true);
        fpd.setDimAmount(0.0f);

        // About dialog shape, color
        fpd.setBackgroundColor(Color.parseColor("#e0e0e0"));
        fpd.setBackgroundAlpha(0.2f);
        fpd.setBorderStroke(0);
        fpd.setBorderColor(-1);
        fpd.setCornerRadius(16);

        // About image
        fpd.setImageSize(200);
        fpd.setImageMargin(10);

        // About rotation
        fpd.setOrientation("rotationY");
        fpd.setDuration(600);
        fpd.setStartAngle(0.0f);
        fpd.setEndAngle(180.0f);
        fpd.setMinAlpha(0.0f);
        fpd.setMaxAlpha(1.0f);


        fpd.show(getFragmentManager(), "");

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationPromptHere();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0); // 5000, // 5 seconds
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setSmallestDisplacement(10);


        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationCallback = new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {

                    if (locationResult != null) {


//                        if (AppStatus.getInstance(ShipmenActivity.this).isOnline()) {
//
//
//                            System.out.println("ShipmenActivity.onLocationResult   ONLINE ");
//
//
//                        } else {
//
//                            System.out.println("ShipmenActivity.onLocationResult  OFFLINE ");
//                        }


                        Hawk.put("locationResult", locationResult);



//                        sendDataToServer(locationResult, orderId, rEceiverId);

                        Location reciverLoc = new Location("reciverLocation");
                        reciverLoc.setLatitude(Double.valueOf(receiverlatitude));
                        reciverLoc.setLongitude(Double.valueOf(receiverLongitude));


                        float distanceInMeters = reciverLoc.distanceTo(locationResult.getLastLocation());


                        if (distanceInMeters <= 5000) {
                            if (distanceInMeters <= 100) {


                                TastyToast.makeText(getApplicationContext(), "Distance less than 100 meter", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                sendDataToServerReached(orderId, rEceiverId, locationResult);
                                stopLocationUpdates();

                            } else {

                                TastyToast.makeText(getApplicationContext(), "Distance more than 100 meter", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                nearByCheckAPI(orderId);
                            }
                        }


                    }

                }
            };


            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }


    private void locationPromptHere() {

        final String REQUEST_CHECK_SETTINGS = "1";
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();

            mGoogleApiClient.connect();

        } else {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ShipmenActivity.this, Integer.parseInt(REQUEST_CHECK_SETTINGS));
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });


    }

    private void nearByCheckAPI(String orderId) {

        HttpModule.provideRepositoryService().nearByCheck(orderId).enqueue(new Callback<NearByCheck>() {
            @Override
            public void onResponse(Call<NearByCheck> call, Response<NearByCheck> response) {


                if (response.body().getIsSuccess()) {

                    try {

                        fastAdapter.getAdapterItem(0).getShipment().setStatusId(response.body().getStatus());
                        fastAdapter.notifyAdapterItemChanged(pos);

                    } catch (Exception e) {

                        System.out.println("ShipmenActivity.onResponse " + e);
                    }


                } else {
                    TastyToast.makeText(ShipmenActivity.this, response.message(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                }


            }

            @Override
            public void onFailure(Call<NearByCheck> call, Throwable t) {

//                TastyToast.makeText(ShipmenActivity.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                System.out.println("ShipmenActivity.onFailure " + t.toString());


            }
        });


    }


    private void sendDataToServer(LocationResult locationResult, String orderId, String rEceiverId) {

        ArrayList<String> listType;

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = null;
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        }


        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        String current_Date_Time = df.format(cal.getTime());


        Hawk.put("orderId", orderId);
        Hawk.put("latitude", locationResult.getLastLocation().getLatitude());
        Hawk.put("longitude", locationResult.getLastLocation().getLongitude());
        Hawk.put("address", address);
        Hawk.put("current_Date_Time", current_Date_Time);

        listType = new ArrayList<>();

        listType.add(String.valueOf(Hawk.get("orderId")));
        listType.add(String.valueOf(Hawk.get("latitude")));
        listType.add(String.valueOf(Hawk.get("longitude")));
        listType.add(String.valueOf(Hawk.get("address")));
        listType.add(String.valueOf(Hawk.get("current_Date_Time")));

        System.out.println("ShipmenActivity.sendDataToServer " + listType);


        HttpModule.provideRepositoryService().getRealTimeLocationAPI(String.valueOf(Hawk.get("orderId")), Hawk.get(String.valueOf(Hawk.get("latitude"))) + "", Hawk.get("longitude") + "", Hawk.get("address") + "", String.valueOf(Hawk.get("current_Date_Time"))).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("ShipmenActivity.onResponse");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                System.out.println("ShipmenActivity.onFailure");

            }
        });


    }

    private void stopLocationUpdates() {

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(mLocationCallback);

            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;

        }


    }

    private void sendDataToServerReached(String orderId, String rEceiverId, LocationResult locationResult) {

        HttpModule.provideRepositoryService().getReachedCheckStatusAPI(orderId).enqueue(new Callback<ResponseReachedCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseReachedCheckStatus> call, Response<ResponseReachedCheckStatus> response) {

                if (response.body().isSuccess) {
                    Hawk.delete("orderId");
                    Hawk.delete("arraylist");
                    fastAdapter.getAdapterItem(0).getShipment().setStatusId(response.body().status);
                    fastAdapter.notifyAdapterItemChanged(pos);
                }
            }

            @Override
            public void onFailure(Call<ResponseReachedCheckStatus> call, Throwable t) {
                System.out.println("ShipmenActivity.onFailure");
//                TastyToast.makeText(ShipmenActivity.this, t.toString(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}