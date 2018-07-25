package android.com.adapters;

import android.com.activity.A;
import android.com.activity.MyCallBack.IresultCallback;
import android.com.apiResponses.shipmentList.ShipmentList;
import android.com.apiResponses.shipmentList.ShipmentListMain;
import android.com.garytransportnew.R;
import android.com.interfaces.ItemClickListener;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class AdapterMade extends RecyclerView.Adapter<AdapterMade.MadeViewHolder> {


    ArrayList<ShipmentList> informationArrayList = new ArrayList<>();

    Context context;
    private ItemClickListener clickListener;
    public IresultCallback iresultCallback;
    int position=0;

//    private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    public AdapterMade(Context a, ArrayList<ShipmentList> informationArrayList) {

        this.informationArrayList = informationArrayList;
        iresultCallback = (IresultCallback) a;


    }


    @NonNull
    @Override
    public MadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_made, parent, false);

        return new MadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MadeViewHolder holder, final int position) {

        final ShipmentList eventPayload = informationArrayList.get(position);

        holder.tv_Date.setText(eventPayload.getDeliveryDate());


        holder.tv_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iresultCallback.getResult(holder,position);
            }
        });






//        holder.tv_ShipmentNumber.setText(eventPayload.getOrderid());


    }

    @Override
    public int getItemCount() {
        return informationArrayList.size();
    }







    public class MadeViewHolder extends RecyclerView.ViewHolder {

        public View accept, ontheway, reched, upcoming;
        public TextView tv_ShipmentNumber, tv_Accept, upload_file, tv_Date;
        public Button information, reject;


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

//    private class MyOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//
//
//
////            int itemPosition = mRecyclerView.getChildLayoutPosition(v);
////            String item = String.valueOf(informationArrayList.get(itemPosition));
//
//
//            TastyToast.makeText(context, "Click", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//
//
//        }
//    }
}
