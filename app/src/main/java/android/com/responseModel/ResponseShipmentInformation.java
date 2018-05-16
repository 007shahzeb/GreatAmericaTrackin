
package android.com.responseModel;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseShipmentInformation implements Serializable {

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

    public String getMessage() {
        return message;
    }

    public List<ShipperList> getShipperList() {
        return shipperList;
    }

    public List<ReceiverList> getReceiverList() {
        return receiverList;
    }


}
