package android.com.models;

import android.com.responseModel.ResponseShipmentList;

import java.util.List;

public class Shipment {


    private String deliveryDate;
    private String shipMentNo;
    private Integer statusId;
    private TextViewState upcoming;



    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


    public Shipment(String date, String shipMentNo, Integer statusId) {
        this.deliveryDate = date;
        this.shipMentNo = shipMentNo;
        this.upcoming = upcoming;
        this.statusId = statusId;
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