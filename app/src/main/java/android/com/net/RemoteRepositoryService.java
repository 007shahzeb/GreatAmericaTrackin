package android.com.net;

import android.com.responseModel.LocationAPI;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseDriverNumber;
import android.com.responseModel.ResponseOtpNumber;
import android.com.responseModel.ResponseReachedCheckStatus;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.com.responseModel.ResponseRejectAPI;
import android.com.responseModel.ResponseShipmentInformation;
import android.com.responseModel.ResponseShipmentList;
import android.com.responseModel.ResponseUploadDocumnets;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<ResponseCheckedStatus> getCheckedStatusAPI(@Field("orderid") String idno);


    @FormUrlEncoded
    @POST("getShipperReceiverList/")
    Call<ResponseShipmentInformation> getShipperReciverListAPI(@Field("orderid") String orderId);


    @FormUrlEncoded
    @POST("orderTracking/")
    Call<ResponseRealTimeLocationCheckedStatus> getRealTimeLocationCheckedStatusAPI(@Field("orderid") String orderId,
                                                                                    @Field("receiverid") String receiverId,
                                                                                    @Field("lat") String lat,
                                                                                    @Field("lng") String lng);

    @FormUrlEncoded
    @POST("reachedCheck/")
    Call<ResponseReachedCheckStatus> getReachedCheckStatusAPI(@Field("orderid") String order_Id);


    @FormUrlEncoded
    @POST("orderTracking/")
    Call<ResponseBody> getRealTimeLocationAPI(@Field("orderid") String orderId,
                                              @Field("lat") String lat,
                                              @Field("lng") String lng);


    // Making the multipart request , its also available for Video , Audio..

    @Multipart
    @POST("orderTracking/")
    Call<ResponseUploadDocumnets> getUploadDocumnetsAPI(@Part("orderid") String orderId, @Part MultipartBody.Part image);



    @FormUrlEncoded
    @POST("reachedCheck/")
    Call<ResponseRejectAPI> getRejectAPI(@Field("orderid") String order_Id);


//    @Multipart
//    @POST("orderTracking/")
//    Call<ResponseUploadDocumnets> getUploadDocumnetsAPI (@Part("orderid") RequestBody fname1, @Part("myDocs\"; filename=\"pp.png\" ") RequestBody file);


}
