package android.com.responseModel;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCheckedStatus {


    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("Status")
    @Expose
    private Integer status;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }





}