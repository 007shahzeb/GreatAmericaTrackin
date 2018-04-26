package android.com.activity;

import android.com.Constants;
import android.com.adapters.SimpleItem;
import android.com.garytransportnew.R;
import android.com.models.TextViewState;
import android.com.models.Shipment;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseDriverNumber;
import android.com.responseModel.ResponseShipmentList;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmenActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private FastItemAdapter<SimpleItem> fastAdapter;

    static final int RESULT_LOAD_IMAGE = 1;
    public static String dataTransmited;
    String message;
    Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipmen_layout);

        findingIdsHere();
        performingOperationsHere();

        // Hiding Action in a particular Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    private void findingIdsHere() {
        recycler_view = findViewById(R.id.recycler_view);
    }

    private void performingOperationsHere() {

        HttpModule.provideRepositoryService().getShipmentListAPI(MainActivity.enteredMobileNumber).enqueue(new Callback<ResponseShipmentList>() {
            @Override
            public void onResponse(Call<ResponseShipmentList> call, Response<ResponseShipmentList> response) {

                if (response.body().isSuccess) {

                    if (response.body().shipmentDetail.size() > 0) {

                        for (int i = 0; i < response.body().shipmentDetail.size(); i++) {

                            if (i == 0) {

                                System.out.println("ShipmenActivity.onResponse - value of - " + i);

                                fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).orderid + "", new TextViewState(R.color.acceptColorCode, "ACCEPT"))));
                            } else {
                                fastAdapter.add(new SimpleItem(new Shipment(response.body().shipmentDetail.get(i).getDeliveryDate(), response.body().shipmentDetail.get(i).orderid + "", new TextViewState(R.color.upcomingColorCode, "UPCOMING"))));
                            }
                        }
                    }

                } else {
                    TastyToast.makeText(ShipmenActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseShipmentList> call, Throwable t) {
                t.printStackTrace();
            }


        });


        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        fastAdapter = new FastItemAdapter<>();
        recycler_view.setAdapter(fastAdapter);


        fastAdapter.withSelectable(true);

        fastAdapter.withEventHook(new ClickEventHook<SimpleItem>() {
            @Override
            public void onClick(final View v, final int position, final FastAdapter<SimpleItem> fastAdapter, final SimpleItem item) {

                switch (v.getId()) {

                    case R.id.upload_file:

                        Intent intent = new Intent(ShipmenActivity.this, UploadImageActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.tv_Accept: {

                        int[] positionArray = new int[position];

                        if (positionArray.length >= 0) {

                            new CountDownTimer(2000, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    if (item.getShipment().getTextViewState().getTextLable().equalsIgnoreCase("UPCOMING")) {
                                        item.getShipment().getTextViewState().setColor(R.color.acceptColorCode);
                                        item.getShipment().getTextViewState().setTextLable("ACCEPT");
                                        fastAdapter.notifyAdapterItemChanged(position);

                                    } else if (item.getShipment().getTextViewState().getTextLable().equalsIgnoreCase("ACCEPT")) {


                                        HttpModule.provideRepositoryService().getCheckedStatusAPI(Constants.ACCEPT + "").enqueue(new Callback<ResponseCheckedStatus>() {
                                            @Override
                                            public void onResponse(Call<ResponseCheckedStatus> call, Response<ResponseCheckedStatus> response) {

                                                if (response.body().isSuccess) {

//                                            Toast.makeText(getApplication(), responseCheckedStatus.message, Toast.LENGTH_SHORT).show();
                                                    message = response.body().message;
                                                    System.out.println("ShipmenActivity.onResponse - -ID IS -" + message);

                                                } else {
                                                    TastyToast.makeText(ShipmenActivity.this, response.body().message, TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseCheckedStatus> call, Throwable t) {
                                                System.out.println("MainActivity.onFailure - - " + t);
                                            }
                                        });


                                        item.getShipment().getTextViewState().setColor(R.color.onthewatColorCode);
                                        item.getShipment().getTextViewState().setTextLable("ON THE WAY");
                                        fastAdapter.notifyAdapterItemChanged(position);
                                    } else if (item.getShipment().getTextViewState().getTextLable().equalsIgnoreCase("ON THE WAY")) {
                                        item.getShipment().getTextViewState().setColor(R.color.reachedColorCode);
                                        item.getShipment().getTextViewState().setTextLable("REACHED");
                                        fastAdapter.notifyAdapterItemChanged(position);
                                    }
                                }
                            }.start();

                        }

                        break;
                    }

                }
            }

            @Nullable
            @Override
            public List<View> onBindMany(RecyclerView.ViewHolder viewHolder) {
                List<View> views = new ArrayList<>();

                views.add(((SimpleItem.ViewHolder) viewHolder).tv_Accept);
                views.add(((SimpleItem.ViewHolder) viewHolder).upload_file);

                return views;
            }
        });

    }


}