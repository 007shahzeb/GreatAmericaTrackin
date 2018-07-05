package android.com.test;

import android.com.responseModel.ShipmentDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseShipmentList {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("Shipment_List")
    @Expose
    public List<ShipmentDetail> shipmentDetail = null;



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

    public List<ShipmentDetail> getShipmentDetail() {
        return shipmentDetail;
    }

    public void setShipmentDetail(List<ShipmentDetail> shipmentDetail) {
        this.shipmentDetail = shipmentDetail;
    }



}