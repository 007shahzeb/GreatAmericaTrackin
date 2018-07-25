package android.com.apiResponses.shipperReceiverList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipperList {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("lat")
    @Expose
    public String lat;

    @SerializedName("lng")
    @Expose
    public String lng;

    @SerializedName("pickup_date")
    @Expose
    public String pickupDate;


    @SerializedName("loadnumber")
    @Expose
    public String loadnumber;

    @SerializedName("shippernote")
    @Expose
    public String shippernote;

    @SerializedName("phonenumber")
    @Expose
    public String phonenumber;

    @SerializedName("orderid")
    @Expose
    public Integer orderid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getLoadnumber() {
        return loadnumber;
    }

    public void setLoadnumber(String loadnumber) {
        this.loadnumber = loadnumber;
    }

    public String getShippernote() {
        return shippernote;
    }

    public void setShippernote(String shippernote) {
        this.shippernote = shippernote;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }


}