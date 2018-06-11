package android.com.responseModel;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class  ResponseCheckedStatus {
    Context context;

    public ResponseCheckedStatus(Context context) {

        this.context = context;
    }

//    @SerializedName("isSuccess")
//    @Expose
//
//    public Boolean isSuccess;
//
//    @SerializedName("message")
//    @Expose
//    public String message;


//    @SerializedName("isSuccess")
//    @Expose
//    public Boolean isSuccess;
//    @SerializedName("message")
//    @Expose
//    public String message;
//
//    @SerializedName("Status")
//    @Expose
//    public String status;


    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("Status")
    @Expose
    public Integer status;

    @SerializedName("Destination")
    @Expose
    public Destination destination = null;


}