
package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiverList implements Serializable{

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

    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;

    @SerializedName("pickup_number")
    @Expose
    public String pickupNumber;

    @SerializedName("receiver_note")
    @Expose
    public String receiverNote;

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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getPickupNumber() {
        return pickupNumber;
    }

    public String getReceiverNote() {
        return receiverNote;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Integer getOrderid() {
        return orderid;
    }


    public ReceiverList(String receiverName, String receiverAddress, String receiverDeliveryDate, String receiverPickUpNumber, String receiverNote, String receiverPhoneNumber) {

        this.name = receiverName;
        this.name = receiverAddress;
        this.deliveryDate = receiverDeliveryDate;
        this.pickupNumber = receiverPickUpNumber;
        this.receiverNote = receiverNote;
        this.phonenumber = receiverPhoneNumber;
    }


}
