package android.com.models;

import android.com.responseModel.ResponseShipmentList;

import java.util.List;

public class Shipment {


    private String deliveryDate, shipMentNo, statusId;
    private TextViewState upcoming;

    public Shipment(String date, String shipMentNo, TextViewState upcoming) {
        this.deliveryDate = date;
        this.shipMentNo = shipMentNo;
        this.upcoming = upcoming;
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