package android.com.apiResponses.shipperReceiverList;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipperReceiverList {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("ShipperList")
    @Expose
    public List<ShipperList> shipperList = null;

    @SerializedName("ReceiverList")
    @Expose
    public List<ReceiverList> receiverList = null;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ShipperList> getShipperList() {
        return shipperList;
    }

    public void setShipperList(List<ShipperList> shipperList) {
        this.shipperList = shipperList;
    }

    public List<ReceiverList> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<ReceiverList> receiverList) {
        this.receiverList = receiverList;
    }


}