package android.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfflineDataModel {

    @SerializedName("lat")
    @Expose
    private String latitude;

    @SerializedName("lng")
    @Expose
    private String longitude;

    @SerializedName("orderid")
    @Expose
    private String orderID;


    @SerializedName("time")
    @Expose
    private String currentTime;


    @SerializedName("address")
    @Expose
    private String address;


    public OfflineDataModel(String orderID, String latitude, String longitude, String address, String currentTime) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.orderID = orderID;
        this.currentTime = currentTime;
        this.address = address;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOrderID() {
        return orderID;
    }


    public String getCurrentTime() {
        return currentTime;
    }

    public String getAddress() {
        return address;
    }


}
