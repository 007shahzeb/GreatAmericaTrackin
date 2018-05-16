
package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShipperList implements Serializable{

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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public String getLoadnumber() {
        return loadnumber;
    }

    public String getShippernote() {
        return shippernote;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Integer getOrderid() {
        return orderid;
    }


    public ShipperList(String ShipperName, String ShipperAddress, String ShipperPickUpDate, String ShipperLoadNumber, String ShipperNote, String ShipperPhoneNumber) {

        this.name = ShipperName;
        this.address = ShipperAddress;
        this.pickupDate = ShipperPickUpDate;
        this.loadnumber = ShipperLoadNumber;
        this.shippernote = ShipperNote;
        this.phonenumber = ShipperPhoneNumber;
    }


}
