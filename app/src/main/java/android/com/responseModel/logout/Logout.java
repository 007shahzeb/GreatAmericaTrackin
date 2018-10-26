package android.com.responseModel.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Logout {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("message")
    @Expose
    private String message;


    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
