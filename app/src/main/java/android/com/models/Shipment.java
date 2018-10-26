package android.com.models;


public class Shipment {


    private String deliveryDate;
    private String shipMentNo;
    private Integer statusId;
    private boolean isFirst;
    private String address;
    private String time;

    public String getTime() {
        return time;
    }


    private Integer RcrecieverId;
    private String Rclat;
    private String Rclang;

    public Shipment(String deliveryDate, String shipMentNo, Integer statusId, boolean isFirst, Integer rcrecieverId, String rclat, String rclang) {
        this.deliveryDate = deliveryDate;
        this.shipMentNo = shipMentNo;
        this.statusId = statusId;
        this.isFirst = isFirst;
        RcrecieverId = rcrecieverId;
        Rclat = rclat;
        Rclang = rclang;
    }


    public String getAddress() {
        return address;
    }


    public Integer getRcrecieverId() {
        return RcrecieverId;
    }


    public String getRclat() {
        return Rclat;
    }


    public String getRclang() {
        return Rclang;
    }


    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getShipMentNo() {
        return shipMentNo;
    }

    public Integer getStatusId() {
        return statusId;
    }


    public boolean isFirst() {
        return isFirst;
    }


}