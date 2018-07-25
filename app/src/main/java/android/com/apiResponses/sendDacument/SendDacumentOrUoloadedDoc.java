package android.com.apiResponses.sendDacument;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendDacumentOrUoloadedDoc {

    @SerializedName("isSuccess")
    @Expose
    public Boolean isSuccess;

    @SerializedName("message")
    @Expose
    public String message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}