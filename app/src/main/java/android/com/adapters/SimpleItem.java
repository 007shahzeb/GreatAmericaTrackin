package android.com.adapters;

import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;


public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.ViewHolder> {


    private Shipment shipment;

    public Shipment getShipment() {
        return shipment;
    }
    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public SimpleItem(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_sample_item_id;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.shipmen_item_layoutconstraint_test;
    }

    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends FastAdapter.ViewHolder<SimpleItem> {
        protected View view;

        private View view_forAll;
        public TextView tv_Accept, tv_ShipmentNumber, tv_Date;
        public TextView upload_file;

        public ViewHolder(View view) {
            super(view);
            tv_Accept = view.findViewById(R.id.tv_Accept);
            tv_ShipmentNumber = view.findViewById(R.id.tv_ShipmentNumber);
            tv_Date = view.findViewById(R.id.tv_Date);
            view_forAll = view.findViewById(R.id.view_forAll);
            upload_file = view.findViewById(R.id.upload_file);

        }

        @Override
        public void bindView(@NonNull SimpleItem item, @NonNull List<Object> payloads) {
            //get the context
            Context ctx = itemView.getContext();

            tv_Date.setText(item.shipment.getDate());
            tv_ShipmentNumber.setText(item.shipment.getShipMentNo());


            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_upcoming));
            tv_Accept.setText(item.getShipment().getTextViewState().getTextLable());
            view_forAll.setBackgroundColor(ContextCompat.getColor(view_forAll.getContext(), item.shipment.getTextViewState().getColor()));

            if (tv_Accept.getText().toString().equalsIgnoreCase("ACCEPT")) {
                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_accept));
            }else if (tv_Accept.getText().toString().equalsIgnoreCase("ON THE WAY"))
            {
                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_ontheway));
            }else if (tv_Accept.getText().toString().equalsIgnoreCase("REACHED"))
            {
                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_reached));
            }

            if (tv_Accept.getText().toString().equalsIgnoreCase("REACHED")) {
                upload_file.setVisibility(View.VISIBLE);
            }
//            view_forAll.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_upcoming));

        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {
          /*  name.setText(null);
            description.setText(null);*/
        }
    }
}
