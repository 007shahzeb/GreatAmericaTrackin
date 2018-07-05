package android.com.adapters;

import android.Manifest;
import android.app.Activity;
import android.com.activity.ShipmenActivity;
import android.com.activity.UploadImageActivity;
import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseReachedCheckStatus;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.mikepenz.fastadapter.items.AbstractItem;
import com.sdsmdg.tastytoast.TastyToast;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.com.activity.ShipmenActivity.fastAdapter;
import static android.content.ContentValues.TAG;


public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.ViewHolder> {
//        implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {

    public String orderId;
    //    public String rEceiverId;
//    public String receiverlatitude;
//    public String receiverLongitude;
    public int aStatus = 10;

    private Shipment shipment;
    private static long lastClickTime = 0;
    List<ShipmentDetail> shipmentDetailsReference = new ArrayList<>();


    public static GoogleApiClient mGoogleApiClient = null;
    public static LocationRequest mLocationRequest;
    public static LocationCallback mLocationCallback;
    public static Location mLastLocation;
    public static Context context;


    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }


    public SimpleItem() {
    }

    public SimpleItem(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_sample_item_id;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.shipmen_item_layoutconstraint_test;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    public class ViewHolder extends FastAdapter.ViewHolder<SimpleItem> {
        protected View view;

        private View accept, ontheway, reched, upcoming;
        public TextView tv_Accept, tv_ShipmentNumber, tv_Date;
        public TextView upload_file;
        public Button information, reject;


        public ViewHolder(View view) {
            super(view);

            tv_Accept = view.findViewById(R.id.tv_Accept);
            tv_ShipmentNumber = view.findViewById(R.id.tv_ShipmentNumber);
            tv_Date = view.findViewById(R.id.tv_Date);
            accept = view.findViewById(R.id.accept);
            ontheway = view.findViewById(R.id.ontheway);
            reched = view.findViewById(R.id.reched);
            upcoming = view.findViewById(R.id.upcoming);
            upload_file = view.findViewById(R.id.upload_file);
            information = view.findViewById(R.id.information);
            reject = view.findViewById(R.id.reject);
            context = view.getContext();


        }

        @Override
        public void bindView(@NonNull final SimpleItem item, @NonNull List<Object> payloads) {

            Context ctx = itemView.getContext();

            tv_Date.setText(item.shipment.getDate());
            tv_ShipmentNumber.setText(item.shipment.getShipMentNo());


            // Maintaining the state of the shipment
            try {
                if (item.getShipment().isFirst) {

                    System.out.println("ViewHolder.bindView - - - here what isFirst " + item.getShipment().isFirst);
                    switch (item.getShipment().getStatusId()) {

                        case 0:
                            break;

                        case 1:   // active just coming


                            System.out.println("ViewHolder.bindView - Case1- -");
                            Toast.makeText(context, "ACCEPT CLCIK", Toast.LENGTH_SHORT).show();
                            item.orderId = item.getShipment().getShipMentNo();

                            tv_Accept.setVisibility(View.VISIBLE);
                            reject.setVisibility(View.VISIBLE);
                            ontheway.setVisibility(View.GONE);
                            reched.setVisibility(View.GONE);
                            upcoming.setVisibility(View.GONE);
                            accept.setVisibility(View.GONE);

                            // ACCEPT CLCIK GOES HERE
                            tv_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    HttpModule.provideRepositoryService().getCheckedStatusAPI(item.orderId).enqueue(new Callback<ResponseCheckedStatus>() {
                                        @Override
                                        public void onResponse(Call<ResponseCheckedStatus> call, Response<ResponseCheckedStatus> response) {

                                            if (response.body().isSuccess) {

                                                // setting the values into an item
                                                item.getShipment().setStatusId(response.body().status);  //1 2 3
                                                item.getShipment().setOrderId(item.orderId);
                                                item.getShipment().setReciverId(response.body().destination.id);
                                                item.getShipment().setRcLat(response.body().destination.lat);
                                                item.getShipment().setRcLang(response.body().destination.lng);
                                                fastAdapter.notifyAdapterDataSetChanged();

                                            } else {
                                                TastyToast.makeText(context, "Error Occurred", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseCheckedStatus> call, Throwable t) {
                                            System.out.println("ViewHolder.onFailure - - " + t);
                                        }
                                    });

                                }
                            });
                            break;
                        //              ON THE WAY CLICK GOES HERE
                        case 2:   // on the way

                            System.out.println("ViewHolder.bindView - Case2- -");
                            Toast.makeText(context, "ON THE WAY CLCIK", Toast.LENGTH_SHORT).show();

                            reject.setVisibility(View.GONE);
                            tv_Accept.setVisibility(View.VISIBLE);
                            tv_Accept.setText("ON THE WAY");
                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_ontheway));
                            accept.setVisibility(View.GONE);
                            upcoming.setVisibility(View.GONE);
                            ontheway.setVisibility(View.VISIBLE);


                            // Location prompt starts

//                            final String REQUEST_CHECK_SETTINGS = "1";
//
//                            if (mGoogleApiClient == null) {
//                                mGoogleApiClient = new GoogleApiClient.Builder(context)
//                                        .addApi(LocationServices.API).build();
//                                mGoogleApiClient.connect();
//
//                            } else {
//                                try {
//
//                                    // Added the runtime permission
////                                    LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    ActivityCompat#requestPermissions
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for ActivityCompat#requestPermissions for more details.
//                                        return;
//                                    }
//                                    LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
////                                                status.startResolutionForResult(ShipmenActivity.this, Integer.parseInt(REQUEST_CHECK_SETTINGS));
//                                                status.startResolutionForResult((Activity) context, Integer.parseInt(REQUEST_CHECK_SETTINGS));
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

                            // Location prompt end here


                            tv_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    System.out.println("ViewHolder.onClick - -  ORDERID   " + item.getShipment().getOrderId());
                                    System.out.println("ViewHolder.onClick - -  RECIVERID    " + item.getShipment().getOrderId());
                                    System.out.println("ViewHolder.onClick - -  RECIVERLATTITUDE     " + item.getShipment().getRcLat());
                                    System.out.println("ViewHolder.onClick - -  RECIVERLONGITUDE    " + item.getShipment().getRcLang());

                                    HttpModule.provideRepositoryService().getRealTimeLocationCheckedStatusAPI(item.getShipment().getOrderId(), item.getShipment().getReciverId() + "", item.getShipment().getRcLat(), item.getShipment().getRcLang()).enqueue(new Callback<ResponseRealTimeLocationCheckedStatus>() {
                                        @Override
                                        public void onResponse(Call<ResponseRealTimeLocationCheckedStatus> call, Response<ResponseRealTimeLocationCheckedStatus> response) {
                                            if (response.body().isSuccess) {

//                                                item.getShipment().setStatusId(response.body().status);
                                                System.out.println("ViewHolder.onResponse - --  CheckedStatusAPI ");
                                                item.getShipment().setStatusId(aStatus);
                                                fastAdapter.notifyAdapterDataSetChanged();
//                                                buildGoogleApiClient();
//                                                mGoogleApiClient.connect();


                                            } else {
                                                System.out.println("ViewHolder.onResponse - - - you are not passing the proper value in request check your request.. ");
                                                TastyToast.makeText(context, response.body().message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseRealTimeLocationCheckedStatus> call, Throwable t) {
                                            System.out.println("ShipmenActivity.onFailure - - -" + t);
                                        }
                                    });
                                }
                            });
                            break;

                        case 3:  // reached
//                            System.out.println("ViewHolder.bindView - CASE 3- " + item.getShipment().getStatusId());
//                            Toast.makeText(context, "REACHED CLCIK", Toast.LENGTH_SHORT).show();
//                            ontheway.setVisibility(View.GONE);
//                            reched.setVisibility(View.VISIBLE);
//                            upload_file.setVisibility(View.VISIBLE);
//
//                            upload_file.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    Intent intent = new Intent(context, UploadImageActivity.class);
//                                    context.startActivity(intent);
//                                    removeAt(getAdapterPosition());
//                                }
//                            });

                            break;

                        case 4: //rejected
                            // REJECT CLICK GOES HERE
                            System.out.println("ViewHolder.bindView - Case3- -");
                            Toast.makeText(context, "REJECT CLCIK", Toast.LENGTH_SHORT).show();
                            reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                                        return;
                                    }
                                    lastClickTime = SystemClock.elapsedRealtime();

                                    new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                            .setTopColorRes(R.color.colorAccent)
                                            .setButtonsColorRes(R.color.colorPrimaryDark)
                                            .setIcon(R.drawable.app_icon)
                                            .setTitle(R.string.donate_title)
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    rejectingOrder(item);
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    TastyToast.makeText(context, "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                                }
                                            })
                                            .show();
                                }
                            });

                            break;


                        case 10:

                            Toast.makeText(context, "aStatus Test ", Toast.LENGTH_SHORT).show();
                            System.out.println("ViewHolder.bindView - CASE 3- " + item.getShipment().getStatusId());
                            Toast.makeText(context, "REACHED CLCIK", Toast.LENGTH_SHORT).show();


                            tv_Accept.setText("REACHED");
                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_reached));
                            ontheway.setVisibility(View.GONE);
                            reched.setVisibility(View.VISIBLE);
                            upload_file.setVisibility(View.VISIBLE);
//                            removeAt(getAdapterPosition());

                            upload_file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(context, UploadImageActivity.class);
                                    context.startActivity(intent);

                                }
                            });

                            break;
                    }

                } else {

                    tv_Accept.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                    upload_file.setVisibility(View.GONE);
                    tv_Accept.setText("UPCOMING");
                    tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_upcoming));
                    upcoming.setVisibility(View.VISIBLE);
                    ontheway.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);
                    reched.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                System.out.println("ViewHolder.bindView - - " + e);
            }
        }

        private void rejectingOrder(final SimpleItem item) {

            HttpModule.provideRepositoryService().getOrderRejectedAPI(item.orderId).enqueue(new Callback<ResponseGetOrderRejected>() {
                @Override
                public void onResponse(Call<ResponseGetOrderRejected> call, Response<ResponseGetOrderRejected> response) {

                    if (response.body().isSuccess) {

                        TastyToast.makeText(context, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        removeAt(getAdapterPosition());

                        Intent intent = new Intent(context, ShipmenActivity.class);
                        context.startActivity(intent);


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

        private void removeAt(int position) {

            fastAdapter.remove(position);
            fastAdapter.notifyAdapterItemRemoved(position);
            fastAdapter.notifyAdapterItemChanged(position);
            fastAdapter.notifyAdapterDataSetChanged();

        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {

        }


    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000); // Update location every second
//
//        try {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
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
////                        sendDataToServer(locationResult, orderId, rEceiverId);
//                        sendDataToServer(locationResult, getShipment().getOrderId(), String.valueOf(getShipment().getReciverId()));
//
//
//                        Log.i(TAG, "onLocationResult: ");
//
//                        Location reciverLoc = new Location("reciverLocation");
//                        reciverLoc.setLatitude(Double.valueOf(getShipment().getRcLat()));
//                        reciverLoc.setLongitude(Double.valueOf(getShipment().getRcLang()));
//
//                        float distanceInMeters = reciverLoc.distanceTo(locationResult.getLastLocation());
//
////                        sendDataToServerReached(orderId, rEceiverId, locationResult);
//                        sendDataToServerReached(getShipment().getOrderId(), String.valueOf(getShipment().getReciverId()), locationResult);
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
//            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//
//    }

//    private void stopLocationUpdates() {
//
//
//        if (mGoogleApiClient != null) {
//            LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(mLocationCallback);
//          /*  mGoogleApiClient.disconnect();
//            mGoogleApiClient = null;*/
//
//        }
//    }

//    private void sendDataToServerReached(String orderId, String rEceiverId, LocationResult locationResult) {
//
//
//        HttpModule.provideRepositoryService().getReachedCheckStatusAPI(orderId).enqueue(new Callback<ResponseReachedCheckStatus>() {
//            @Override
//            public void onResponse(Call<ResponseReachedCheckStatus> call, Response<ResponseReachedCheckStatus> response) {
//
//                if (response.body().isSuccess) {
//
//                    System.out.println("SimpleItem.onResponse - - -Testing Caese");
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseReachedCheckStatus> call, Throwable t) {
//                System.out.println("ShipmenActivity.onFailure");
//
//            }
//        });
//
//
//    }

//    private void sendDataToServer(LocationResult locationResult, String orderId, String rEceiverId) {
//
//        HttpModule.provideRepositoryService().getRealTimeLocationAPI(orderId, locationResult.getLastLocation().getLatitude() + "", locationResult.getLastLocation().getLongitude() + "").enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                System.out.println("ShipmenActivity.onResponse sENDING DATA TO SERVER cHECK ");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                System.out.println("ShipmenActivity.onFailure " + t);
//
//            }
//        });
//
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(context) // getApplicationContext
//                .addConnectionCallbacks(this) // this
//                .addOnConnectionFailedListener(this) // this
//                .addApi(LocationServices.API)
//                .build();
//    }


}
