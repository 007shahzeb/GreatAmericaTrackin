package android.com.responseModel.nearByCheckAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByCheck {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Status")
    @Expose
    private Integer status;


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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

