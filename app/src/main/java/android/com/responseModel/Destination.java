package android.com.responseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("lat")
    @Expose
    public String lat;

    @SerializedName("lng")
    @Expose
    public String lng;

//    public Integer getId() {
//        return id;
//    }
//
//    public String getLat() {
//        return lat;
//    }
//
//    public String getLng() {
//        return lng;
//    }


}
