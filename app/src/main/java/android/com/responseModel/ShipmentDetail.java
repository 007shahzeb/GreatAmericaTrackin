package android.com.responseModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShipmentDetail {

    @SerializedName("orderid")
    @Expose
    public Integer orderid;

    @SerializedName("statusid")
    @Expose
    public Integer statusid;

    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;


    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getStatusid() {
        return statusid;
    }

    public void setStatusid(Integer statusid) {
        this.statusid = statusid;
    }

    public String getDeliveryDate() {

//        // converting date into proper format
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date=null;
//        try {
//            Date newDate=simpleDateFormat.parse(deliveryDate);
//            date= outputFormat.format(newDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


}