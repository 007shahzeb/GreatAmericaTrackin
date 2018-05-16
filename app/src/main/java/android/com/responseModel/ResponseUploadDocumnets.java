package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUploadDocumnets {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;
    @SerializedName("message")
    @Expose
    public String message;

}