package android.com.service;

import android.Manifest;
import android.app.Service;
import android.com.activity.ShipmenActivity;
import android.com.models.OfflineDataModel;
import android.com.net.HttpModule;
import android.com.netStatus.AppStatus;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    List<OfflineDataModel> offlineDataModels;
    OfflineDataModel offlineDataModel;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        offlineDataModels = Hawk.get("arraylist");
        offlineDataModels = new ArrayList<>();


    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
        sendDataToServer(offlineDataModels);

    }


//     Hawk.put("orderId", orderId);
//                        Hawk.put("receiverId", rEceiverId);

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {


            Log.i("*****", "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {

                Calendar calendar = Calendar.getInstance();
                int orderId = Hawk.get("orderId");

//                Date time = calendar.getTime();
//                Calendar cal = Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                String current_Date_Time = df.format(calendar.getTime());


                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(LocationService.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = null;
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                }


                Hawk.put("latitude", loc.getLatitude());
                Hawk.put("longitude", loc.getLongitude());
                Hawk.put("currentDateTime", current_Date_Time);
                Hawk.put("address", address);

                offlineDataModel = new OfflineDataModel(loc.getLatitude() + "", loc.getLongitude() + "", orderId + "", current_Date_Time, address);

                offlineDataModels.add(offlineDataModel);

                System.out.println("MyLocationListener.onLocationChanged" + offlineDataModels.toString());


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {

            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendDataToServer(List<OfflineDataModel> offlineDataModels) {


        HttpModule.provideRepositoryService().addReview(offlineDataModels).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("LocationService.onResponse  RESPONSE ");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                System.out.println("LocationService.onFailure " + t);

            }
        });


    }

    @Override
    public boolean stopService(Intent name) {


        return super.stopService(name);

    }
}