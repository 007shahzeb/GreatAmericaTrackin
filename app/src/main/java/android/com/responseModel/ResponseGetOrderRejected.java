package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGetOrderRejected {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;


    public Integer getStatus() {
        return status;
    }

    @SerializedName("Status")
    @Expose
    public Integer status;





    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }


}