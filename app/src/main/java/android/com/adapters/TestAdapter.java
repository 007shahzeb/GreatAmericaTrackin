package android.com.adapters;

import android.com.garytransportnew.R;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.CheckViewHolder> {

    List<ShipmentDetail> shipmentDetails = new ArrayList<>();
    private Context context;

    public TestAdapter(Context context, List<ShipmentDetail> shipmentDetails) {


        this.shipmentDetails = shipmentDetails;
        this.context = context;
    }


    @NonNull
    @Override
    public TestAdapter.CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipmen_item_layoutconstraint_test, parent, false);
        TestAdapter.CheckViewHolder checkViewHolder = new TestAdapter.CheckViewHolder(view);
        return checkViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.CheckViewHolder holder, int position) {

        holder.tv_Date.setText(shipmentDetails.get(position).deliveryDate);
        holder.tv_ShipmentNumber.setText(shipmentDetails.get(position).orderid);
        int sStatusID = shipmentDetails.get(position).statusid;


    }

    @Override
    public int getItemCount() {
        return shipmentDetails.size();
    }

    public class CheckViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Date, tv_ShipmentNumber, tv_Accept, upload_file;
        private Button information, reject;


        public CheckViewHolder(View itemView) {
            super(itemView);

            tv_Date = itemView.findViewById(R.id.tv_Date);
            tv_ShipmentNumber = itemView.findViewById(R.id.tv_ShipmentNumber);
            tv_Accept = itemView.findViewById(R.id.tv_Accept);
            upload_file = itemView.findViewById(R.id.upload_file);
            information = itemView.findViewById(R.id.information);
            reject = itemView.findViewById(R.id.reject);


        }
    }
}
