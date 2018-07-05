package android.com.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.com.DonationAdapter;
import android.com.DonationOption;
import android.com.adapters.SimpleItem;
import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.com.models.TextViewState;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseReachedCheckStatus;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.com.responseModel.ResponseShipmentInformation;
import android.com.responseModel.ResponseShipmentList;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
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
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.sdsmdg.tastytoast.TastyToast;
import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmenActivity extends AppCompatActivity {  // implements GoogleApiClient.ConnectionCallbacks,
//    GoogleApiClient.OnConnectionFailedListener

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
    private Context context;

    private final String TAG = "ShipmenActivity";
    private Location mLastLocation;
    private LocationCallback mLocationCallback;

    public Boolean reachedStatus = false;
    private SpotsDialog dialog = null;
    private SimpleItem itemChange;
    private long lastClickTime = 0;
    int pos;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        performingOperationsHere();
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
//        flipProgress();
        findingIdsHere();


        try {
            // Hiding Action in a particular Activity
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } finally {

        }


        context = this;
    }

    private void findingIdsHere() {
        recycler_view = findViewById(R.id.recycler_view);
    }

//    List<ShipmentDetail> tempListShipment = new ArrayList<>();

    private void performingOperationsHere() {




        HttpModule.provideRepositoryService().getShipmentListAPI(MainActivity.enteredMobileNumber).enqueue(new Callback<ResponseShipmentList>() {
            @Override
            public void onResponse(Call<ResponseShipmentList> call, Response<ResponseShipmentList> response) {
//                fpd.dismiss();

                if (response.body() != null) {

                    if (response.body().isSuccess) {

                        if (response.body().shipmentDetail.size() > 0) {

                            for (int i = 0; i < response.body().shipmentDetail.size(); i++) {

                                System.out.println("ShipmenActivity.onResponse - - -" + response.body().shipmentDetail.size());
//                                Shipment shipment = new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).orderid + "", response.body().shipmentDetail.get(i).statusid);
//                                SimpleItem simpleItem = new SimpleItem(shipment);
//                                fastAdapter.add(simpleItem);

                                if (i == 0) {

                                    fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).orderid + "", response.body().shipmentDetail.get(i).statusid, response.body().shipmentDetail.get(i).isFirst = true)));
                                } else {
                                    fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).orderid + "", response.body().shipmentDetail.get(i).statusid, response.body().shipmentDetail.get(i).isFirst = false)));
                                }

                                Log.i(TAG, "onResponse: ");
                            }
//
//
//                            for (int i = 0; i < response.body().shipmentDetail.size(); i++) {
//
//                                if (response.body().shipmentDetail.get(i).getStatusid() != 3)  // not done check
//                                {
//                                    tempListShipment.add(response.body().shipmentDetail.get(i));
//                                    continue;
//                                }
//
//
//                            }
//
//
//                            for (int i = 0; i < tempListShipment.size(); i++) {
//
//                                if (i == 0) {
//                                    fastAdapter.add(new SimpleItem(new Shipment(tempListShipment.get(i).getDeliveryDate(), tempListShipment.get(i).orderid + "", tempListShipment.get(i).statusid)));
//                                } else {
//                                    fastAdapter.add(new SimpleItem(new Shipment(tempListShipment.get(i).getDeliveryDate(), tempListShipment.get(i).orderid + "", tempListShipment.get(i).statusid)));
//                                }
//                            }
                        }
                    } else {
                        TastyToast.makeText(ShipmenActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseShipmentList> call, Throwable t) {
//                fpd.dismiss();
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

                System.out.println("ShipmenActivity.onClick - - - OnCLickkkkk ");

                itemChange = item;
                pos = position;
                orderId = item.getShipment().getShipMentNo();
                switch (v.getId()) {


                    case R.id.upload_file:


//                        Intent intent = new Intent(ShipmenActivity.this, UploadImageActivity.class);
//                        startActivity(intent);
//                        fastAdapter.remove(position); // Integer.parseInt(orderId)
                        //finish();

//                        Intent intent = new Intent(ShipmenActivity.this, UploadImageActivity.class);
//                        // set the new task and clear flags
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);

                        break;

                    case R.id.tv_Accept:


//                        if (!reachedStatus) {
//                            if (dialog == null) {
//                                dialog = new SpotsDialog(context);
//                                dialog.setCancelable(false);
//                                dialog.show();
//
//                            } else dialog.show();
//                        }
//
//                        if (item.getShipment().getTextViewState().getTextLable().equalsIgnoreCase("ACCEPT")) {
//                            item.setAccepted(true);
//                            fastAdapter.notifyAdapterDataSetChanged();
//
//                            HttpModule.provideRepositoryService().getCheckedStatusAPI(orderId).enqueue(new Callback<ResponseCheckedStatus>() {
//                                @Override
//                                public void onResponse(Call<ResponseCheckedStatus> call, Response<ResponseCheckedStatus> response) {
//                                    fpd.dismiss();
//                                    dialog.dismiss();
//                                    if (response.body() != null) {
//
//                                        if (response.body().isSuccess) {
//
//                                            String status = response.body().status;
//                                            receiverlatitude = response.body().destination.lat;
//                                            receiverLongitude = response.body().destination.lng;
//                                            rEceiverId = response.body().destination.id.toString();
//
//                                            if (response.body().status.equalsIgnoreCase(status)) {
//
//                                                item.getShipment().getTextViewState().setColor(R.color.onthewatColorCode);
//                                                item.getShipment().getTextViewState().setTextLable("ON THE WAY");
//                                                fastAdapter.notifyAdapterItemChanged(position);
//                                            }
//                                        } else {
//                                            TastyToast.makeText(ShipmenActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseCheckedStatus> call, Throwable t) {
//                                    System.out.println("ShipmenActivity.onFailure - - " + t);
//                                    fpd.dismiss();
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//

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
//                            if (!reachedStatus) {
//
//                                v.setClickable(false);
//
//                                HttpModule.provideRepositoryService().getRealTimeLocationCheckedStatusAPI(orderId, rEceiverId, receiverlatitude, receiverLongitude).enqueue(new Callback<ResponseRealTimeLocationCheckedStatus>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseRealTimeLocationCheckedStatus> call, Response<ResponseRealTimeLocationCheckedStatus> response) {
//                                        dialog.dismiss();
//                                        if (response.body() != null && response.body().isSuccess) {
//
//                                            buildGoogleApiClient();
//                                            mGoogleApiClient.connect();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseRealTimeLocationCheckedStatus> call, Throwable t) {
//                                        System.out.println("ShipmenActivity.onFailure - - -" + t);
//                                        dialog.dismiss();
//                                    }
//                                });
//                            }
//
//                        }
//                        break;


                        // Shahzeb test


                    case R.id.reject:
//
//                        Toast.makeText(getApplicationContext() , "Testting.." , Toast.LENGTH_SHORT).show();
//                        System.out.println("ShipmenActivity.onClick - -  rejecting...");
//
//                        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//                            return;
//                        }
//                        lastClickTime = SystemClock.elapsedRealtime();
//
//                        new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
//                                .setTopColorRes(R.color.colorAccent)
//                                .setButtonsColorRes(R.color.colorPrimaryDark)
//                                .setIcon(R.drawable.app_icon)
//                                .setTitle(R.string.donate_title)
//                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        rejectingOrder();
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        TastyToast.makeText(getApplicationContext(), "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                                    }
//                                })
//                                .show();
//
//                        break;

                    case R.id.information:

                        System.out.println("ShipmenActivity.onClick = = = information ");

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

//                views.add(((SimpleItem.ViewHolder) viewHolder).tv_Accept);
//                views.add(((SimpleItem.ViewHolder) viewHolder).upload_file);
                views.add(((SimpleItem.ViewHolder) viewHolder).information);
//                views.add(((SimpleItem.ViewHolder) viewHolder).reject);


                return views;
            }
        });

    }

//    private void rejectingOrder() {
//
//        HttpModule.provideRepositoryService().getOrderRejectedAPI(orderId).enqueue(new Callback<ResponseGetOrderRejected>() {
//            @Override
//            public void onResponse(Call<ResponseGetOrderRejected> call, Response<ResponseGetOrderRejected> response) {
//
//                if (response.body().isSuccess) {
//                    TastyToast.makeText(getApplicationContext(), response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                } else {
//
//                    TastyToast.makeText(getApplicationContext(), "Getting False Value", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseGetOrderRejected> call, Throwable t) {
//
//                System.out.println("ShipmenActivity.onFailure - - " + t);
//
//            }
//        });
//
//    }

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


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000); // Update location every second
//
//        try {
//            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mLocationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//
//                    if (locationResult != null) {
//
//
//                        sendDataToServer(locationResult, orderId, rEceiverId);
//
//
//                        Log.i(TAG, "onLocationResult: ");
//
//                        Location reciverLoc = new Location("reciverLocation");
//                        reciverLoc.setLatitude(Double.valueOf(receiverlatitude));
//                        reciverLoc.setLongitude(Double.valueOf(receiverLongitude));
//
//                        float distanceInMeters = reciverLoc.distanceTo(locationResult.getLastLocation());
//
//                        sendDataToServerReached(orderId, rEceiverId, locationResult);
//                        stopLocationUpdates();
//
////                        if (distanceInMeters < 10) {
////
////                            sendDataToServerReached(orderId, rEceiverId, locationResult);
////                            stopLocationUpdates();
////                        }
//                    }
//                }
//            };
//
//
//            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//    }

    private void sendDataToServer(LocationResult locationResult, String orderId, String rEceiverId) {


        HttpModule.provideRepositoryService().getRealTimeLocationAPI(orderId, locationResult.getLastLocation().getLatitude() + "", locationResult.getLastLocation().getLongitude() + "").enqueue(new Callback<ResponseBody>() {
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
          /*  mGoogleApiClient.disconnect();
            mGoogleApiClient = null;*/

        }


    }

    private void sendDataToServerReached(String orderId, String rEceiverId, LocationResult locationResult) {

        HttpModule.provideRepositoryService().getReachedCheckStatusAPI(orderId).enqueue(new Callback<ResponseReachedCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseReachedCheckStatus> call, Response<ResponseReachedCheckStatus> response) {

                if (response.body().isSuccess) {

                    itemChange.getShipment().getTextViewState().setColor(R.color.reachedColorCode);
                    itemChange.getShipment().getTextViewState().setTextLable("REACHED");
                    fastAdapter.notifyAdapterItemChanged(pos);
                    dialog.dismiss();
                    reachedStatus = response.body().isSuccess;
                    System.out.println("ShipmenActivity.onResponse - - " + reachedStatus);

                }
            }

            @Override
            public void onFailure(Call<ResponseReachedCheckStatus> call, Throwable t) {
                System.out.println("ShipmenActivity.onFailure");
                dialog.dismiss();

            }
        });
    }


//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }


//    synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
}