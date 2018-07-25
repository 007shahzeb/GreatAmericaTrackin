package android.com.adapters;

import android.com.garytransportnew.R;
import android.com.interfaces.CustomItemClickListener;
import android.com.responseModel.ReceiverList;
import android.com.responseModel.ShipperList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShipmentOrderAdapter extends RecyclerView.Adapter<ShipmentOrderAdapter.InformationViewHolder> {

    CustomItemClickListener listener;

    List<ShipperList> orderListInfos = new ArrayList<>();
    List<ReceiverList> receiverLists = new ArrayList<>();


    public ShipmentOrderAdapter(List<ShipperList> shipmentOrderListInfos, List<ReceiverList> receiverListList, CustomItemClickListener testing_single_item_clicked) {
        System.out.println("ShipmentOrderAdapter.ShipmentOrderAdapter - - - Construvto");
        this.orderListInfos = shipmentOrderListInfos;
        this.listener = testing_single_item_clicked;
        this.receiverLists = receiverListList;
    }


    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        System.out.println("ShipmentOrderAdapter.onCreateViewHolder - - - OncreateViewGHolder ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shipment_order_list_information, parent, false);
        InformationViewHolder informationViewHolder = new InformationViewHolder(view);
        return informationViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {

        System.out.println("ShipmentOrderAdapter.onBindViewHolder");

        if (position >= 0) {
            // SHIPPER INFO
            holder.picker_name.setText(orderListInfos.get(position).name);
            holder.picker_address.setText(orderListInfos.get(position).address);
            holder.pickup_date.setText(orderListInfos.get(position).pickupDate);
            holder.load_number.setText(orderListInfos.get(position).loadnumber);
            holder.shipper_note.setText(orderListInfos.get(position).shippernote);
            holder.phone_number.setText(orderListInfos.get(position).phonenumber);

//            RECEIVER INFO

            holder.reciver_name.setText(receiverLists.get(position).name);
            holder.reciver_address.setText(receiverLists.get(position).address);
            holder.delivery_date.setText(receiverLists.get(position).deliveryDate);
            holder.reciver_pickupnumber.setText(receiverLists.get(position).pickupNumber);
            holder.reciver_note.setText(receiverLists.get(position).receiverNote);
            holder.receiver_phonenumber.setText(receiverLists.get(position).phonenumber);

        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        System.out.println("ShipmentOrderAdapter.getItemCount - - - " + orderListInfos.size());
        return receiverLists.size();
    }


    public static class InformationViewHolder extends RecyclerView.ViewHolder {


        TextView picker_name, picker_address, pickup_date, load_number, shipper_note,
                phone_number, reciver_name, reciver_address, delivery_date,
                reciver_pickupnumber, reciver_note, receiver_phonenumber;


        public InformationViewHolder(View itemView) {
            super(itemView);

            System.out.println("InformationViewHolder.InformationViewHolder - - - ");

            picker_name = itemView.findViewById(R.id.picker_name);
            picker_address = itemView.findViewById(R.id.picker_address);
            pickup_date = itemView.findViewById(R.id.pickup_date);
            load_number = itemView.findViewById(R.id.load_number);
            shipper_note = itemView.findViewById(R.id.shipper_note);
            phone_number = itemView.findViewById(R.id.phone_number);
            reciver_name = itemView.findViewById(R.id.reciver_name);
            reciver_address = itemView.findViewById(R.id.reciver_address);
            delivery_date = itemView.findViewById(R.id.delivery_date);
            reciver_pickupnumber = itemView.findViewById(R.id.reciver_pickupnumber);
            reciver_note = itemView.findViewById(R.id.reciver_note);
            receiver_phonenumber = itemView.findViewById(R.id.receiver_phonenumber);


        }
    }


}
