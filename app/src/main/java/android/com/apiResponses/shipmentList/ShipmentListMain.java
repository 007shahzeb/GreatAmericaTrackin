package android.com.apiResponses.shipmentList;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipmentListMain {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("Shipment_List")
    @Expose
    public List<ShipmentList> shipmentList = null;

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

    public List<ShipmentList> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(List<ShipmentList> shipmentList) {
        this.shipmentList = shipmentList;
    }


}