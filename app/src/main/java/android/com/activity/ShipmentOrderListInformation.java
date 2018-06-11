package android.com.activity;

//import android.com.adapters.ShipmentOrderListInformationAdapter;

import android.com.adapters.ShipmentOrderAdapter;
import android.com.garytransportnew.R;
import android.com.interfaces.CustomItemClickListener;
import android.com.models.ShipmentOrderListInfo;
import android.com.responseModel.ReceiverList;
import android.com.responseModel.ResponseShipmentInformation;
import android.com.responseModel.ShipperList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShipmentOrderListInformation extends AppCompatActivity {

    public RecyclerView recyclerView_shipment_order_list;
    private ImageView backarrow_info;


    List<ShipperList> shipmentOrderListInfos;
    List<ReceiverList> reciverListInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hidingStatusBar();
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {

            if (getIntent().hasExtra("shipmemntlist") && getIntent().hasExtra("recivierlist")) {

                shipmentOrderListInfos = (List<ShipperList>) getIntent().getSerializableExtra("shipmemntlist");
                reciverListInfos = (List<ReceiverList>) getIntent().getSerializableExtra("recivierlist");
                System.out.println("ShipmentOrderListInformation.onCreate 1- - " + shipmentOrderListInfos);
                System.out.println("ShipmentOrderListInformation.onCreate 2- - " + reciverListInfos);
            }
        }
        setContentView(R.layout.shipment_order_list_information);

        findingIdsHere();
        adapterInitializationHere();

        try {
            // Hiding Action in a particular Activity
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } finally {

        }


        backarrow_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void hidingStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void findingIdsHere() {
        System.out.println("ShipmentOrderListInformation.findingIdsHere");
        backarrow_info = findViewById(R.id.backarrow_info);

        recyclerView_shipment_order_list = findViewById(R.id.recyclerView_shipment_order_list);
        recyclerView_shipment_order_list.setHasFixedSize(true);
        recyclerView_shipment_order_list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void adapterInitializationHere() {


        System.out.println("ShipmentOrderListInformation.adapterInitializationHere - 123- -" + shipmentOrderListInfos.size());
        System.out.println("ShipmentOrderListInformation.adapterInitializationHere - - 321-" + reciverListInfos.size());

        if (shipmentOrderListInfos != null && reciverListInfos != null) {

            if (shipmentOrderListInfos.size() > 0 && reciverListInfos.size() > 0) {
                System.out.println("ShipmentOrderListInformation.adapterInitializationHere - - -12345 ");
                ShipmentOrderAdapter shipmentOrderListInformationAdapter = new ShipmentOrderAdapter(shipmentOrderListInfos, reciverListInfos, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

//                        Toast.makeText(getApplicationContext(), "Testing Single Item Clicked", Toast.LENGTH_SHORT).show();

                    }
                });

                System.out.println("ShipmentOrderListInformation.onCreate - - -");

                recyclerView_shipment_order_list.setAdapter(shipmentOrderListInformationAdapter);

            }

        }
    }

}
