package android.com.responseModel;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCheckedStatus {
    Context context;

    public ResponseCheckedStatus(Context context) {

        this.context = context;
    }

    @SerializedName("isSuccess")
    @Expose

    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;


}