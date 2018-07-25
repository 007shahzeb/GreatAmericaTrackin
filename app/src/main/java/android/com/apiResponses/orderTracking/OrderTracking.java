package android.com.apiResponses.orderTracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderTracking {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }


}