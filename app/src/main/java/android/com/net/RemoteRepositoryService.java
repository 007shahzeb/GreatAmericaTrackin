package android.com.net;

import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseDriverNumber;
import android.com.responseModel.ResponseOtpNumber;
import android.com.responseModel.ResponseReachedCheckStatus;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseShipmentInformation;
import android.com.responseModel.ResponseShipmentList;
import android.com.responseModel.ResponseUploadDocumnets;
import android.com.test.ShipmentListTest;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


//    @FormUrlEncoded
//    @POST("ShipmentList/")
//    Call<ShipmentListTest> getShipmentListTest(@Field("driverno") String driverno);


    @FormUrlEncoded
    @POST("CheckStatus/")
    Call<ResponseCheckedStatus> getCheckedStatusAPI(@Field("orderid") String idno);


    @FormUrlEncoded
    @POST("getShipperReceiverList/")
    Call<ResponseShipmentInformation> getShipperReciverListAPI(@Field("orderid") String orderId);


    // CLICK ON "ON THE WAY" THIS API WILL HIT
    @FormUrlEncoded
    @POST("orderTracking/")
    Call<ResponseRealTimeLocationCheckedStatus> getRealTimeLocationCheckedStatusAPI(@Field("orderid") String orderId,
                                                                                    @Field("receiverid") String receiverId,
                                                                                    @Field("lat") String lat,
                                                                                    @Field("lng") String lng);

    // REACHED ON THE DESTINATION "REACHED"
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
    @POST("RejectOrder/")
    Call<ResponseGetOrderRejected> getOrderRejectedAPI(@Field("orderid") String order_Id);


//    @Multipart
//    @POST("orderTracking/")
//    Call<ResponseUploadDocumnets> getUploadDocumnetsAPI (@Part("orderid") RequestBody fname1, @Part("myDocs\"; filename=\"pp.png\" ") RequestBody file);


    //------------------------1
    @FormUrlEncoded
    @POST("ShipmentList/")
    Observable<ShipmentListTest> getShipmentListTest(@Field("driverno") String driverno);

}
