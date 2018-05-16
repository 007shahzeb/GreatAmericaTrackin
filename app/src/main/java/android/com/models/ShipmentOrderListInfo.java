package android.com.models;

public class ShipmentOrderListInfo {

    String order_shipment;
    String time_shipment;
    String place_shipment;
    String dispatch_to_shipment;

    public String getOrder_shipment() {
        return order_shipment;
    }

    public String getTime_shipment() {
        return time_shipment;
    }

    public String getPlace_shipment() {
        return place_shipment;
    }

    public String getDispatch_to_shipment() {
        return dispatch_to_shipment;
    }

    public ShipmentOrderListInfo(String order, String time, String place, String dispatch_to) {
        this.order_shipment = order;
        this.time_shipment = time;
        this.place_shipment = place;
        this.dispatch_to_shipment = dispatch_to;
    }

}
