package android.com.activity;

import android.com.adapters.TestAdapter;
import android.com.garytransportnew.R;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestMainShipmentList extends AppCompatActivity {

    private RecyclerView recyclerViewRefrence;
    List<ShipmentDetail> shipmentDetails = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipmen_layout);
        context = this;

        findingViewsIDsHere();
        adapterInitializationHere();
    }


    private void findingViewsIDsHere() {
        recyclerViewRefrence = findViewById(R.id.recycler_view);


    }

    private void adapterInitializationHere() {

        if (shipmentDetails != null) {
            if (shipmentDetails.size() > 0) {

                TestAdapter testAdapter = new TestAdapter(context, shipmentDetails);
                recyclerViewRefrence.setHasFixedSize(true);
                recyclerViewRefrence.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRefrence.setAdapter(testAdapter);
            }
        }


    }
}
