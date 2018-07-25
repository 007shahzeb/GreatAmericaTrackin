package android.com.responseModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipmentDetail {

    public Integer getOrderid() {
        return orderid;
    }

    public Integer getStatusid() {
        return statusid;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    @SerializedName("orderid")
    @Expose
    private Integer orderid;

    @SerializedName("statusid")
    @Expose
    private Integer statusid;

    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;


    public Integer getRcrecieverId() {
        return RcrecieverId;
    }

    public String getRclat() {
        return Rclat;
    }

    public String getRclang() {
        return Rclang;
    }

    @SerializedName("ReceiverId")
    @Expose
    private Integer RcrecieverId;

    @SerializedName("DestinationLat")
    @Expose
    private String Rclat;

    @SerializedName("DestinationLng")
    @Expose
    private String Rclang;








}