package android.com.net;

import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseDriverNumber;
import android.com.responseModel.ResponseOtpNumber;
import android.com.responseModel.ResponseShipmentList;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RemoteRepositoryService {


    @FormUrlEncoded
    @POST("validateNumber/")
    Call<ResponseDriverNumber> getDriverNumberAPI(@Field("driverno") String user);


    @FormUrlEncoded
    @POST("validateOTP/")
    Call<ResponseOtpNumber> getOtpNumberAPI(@Field("otp") String user);

    @FormUrlEncoded
    @POST("ShipmentList/")
    Call<ResponseShipmentList> getShipmentListAPI(@Field("driverno") String driverno);


    @FormUrlEncoded
    @POST("CheckStatus/")
    Call<ResponseCheckedStatus> getCheckedStatusAPI(@Field("id") String idno);


}
