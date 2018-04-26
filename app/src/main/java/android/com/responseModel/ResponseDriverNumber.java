package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDriverNumber {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;


    @SerializedName("message")
    @Expose
    public String message;


    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}