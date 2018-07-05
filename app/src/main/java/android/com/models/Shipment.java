package android.com.models;

import android.com.responseModel.ResponseShipmentList;

import java.util.List;

public class Shipment {


    private String deliveryDate;
    private String shipMentNo;
    private Integer statusId;
    private TextViewState upcoming;
    public boolean isFirst = false;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getReciverId() {
        return reciverId;
    }

    public void setReciverId(Integer reciverId) {
        this.reciverId = reciverId;
    }

    public String getRcLat() {
        return rcLat;
    }

    public void setRcLat(String rcLat) {
        this.rcLat = rcLat;
    }

    public String getRcLang() {
        return rcLang;
    }

    public void setRcLang(String rcLang) {
        this.rcLang = rcLang;
    }

    private String orderId ="0";
    private Integer reciverId = 0;
    private String rcLat ="0";
    private String rcLang = "0";







    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


    public Shipment(String date, String shipMentNo, Integer statusId, boolean isFirst) {
        this.deliveryDate = date;
        this.shipMentNo = shipMentNo;
        this.upcoming = upcoming;
        this.statusId = statusId;
        this.isFirst = isFirst;
    }

    public TextViewState getTextViewState() {
        return upcoming;
    }

    public void setTextViewState(TextViewState textViewState) {
        this.upcoming = textViewState;
    }


    public String getDate() {
        return deliveryDate;
    }

    public void setDate(String date) {
        this.deliveryDate = date;
    }

    public String getShipMentNo() {
        return shipMentNo;
    }

    public void setShipMentNo(String shipMentNo) {
        this.shipMentNo = shipMentNo;
    }

}