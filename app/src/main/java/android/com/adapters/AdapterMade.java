package android.com.adapters;

import android.com.activity.A;
import android.com.garytransportnew.R;
import android.com.test.ShipmentInformation;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMade extends RecyclerView.Adapter<AdapterMade.MadeViewHolder> {


    ArrayList<ShipmentInformation> informationArrayList = new ArrayList<>();

    Context context;

    public AdapterMade(Context context, ArrayList<ShipmentInformation> informationArrayList) {


        this.context = context;
        this.informationArrayList = informationArrayList;


    }

    @NonNull
    @Override
    public MadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipmen_item_layoutconstraint_test, parent, false);
        MadeViewHolder madeViewHolder = new MadeViewHolder(view);
        return madeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MadeViewHolder holder, int position) {

        final ShipmentInformation eventPayload = informationArrayList.get(position);

        System.out.println("AdapterMade.onBindViewHolder   " + eventPayload.orderid);
        holder.tv_Date.setText(eventPayload.deliveryDate);
//        holder.tv_ShipmentNumber.setText(eventPayload.orderid);


    }

    @Override
    public int getItemCount() {
        return informationArrayList.size();
    }

    public class MadeViewHolder extends RecyclerView.ViewHolder {

        private View accept, ontheway, reched, upcoming;
        private TextView tv_ShipmentNumber, tv_Accept, upload_file, tv_Date;
        private Button information, reject;


        public MadeViewHolder(View itemView) {
            super(itemView);


            accept = itemView.findViewById(R.id.accept);
            ontheway = itemView.findViewById(R.id.ontheway);
            reched = itemView.findViewById(R.id.reched);
            upcoming = itemView.findViewById(R.id.upcoming);

            tv_ShipmentNumber = itemView.findViewById(R.id.tv_ShipmentNumber);

            tv_Accept = itemView.findViewById(R.id.tv_Accept);
            upload_file = itemView.findViewById(R.id.upload_file);
            information = itemView.findViewById(R.id.information);
            reject = itemView.findViewById(R.id.reject);
            tv_Date = itemView.findViewById(R.id.tv_Date);


        }
    }
}
