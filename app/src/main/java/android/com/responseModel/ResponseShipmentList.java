package android.com.responseModel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}