package android.com.apiResponses.shipmentList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipmentList {

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
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


}