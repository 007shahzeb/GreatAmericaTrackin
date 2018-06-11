package android.com.adapters;

import android.com.activity.UploadImageActivity;
import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.com.responseModel.ShipmentDetail;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.sdsmdg.tastytoast.TastyToast;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.com.activity.ShipmenActivity.fastAdapter;


public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.ViewHolder> {


    private Shipment shipment;
    private static long lastClickTime = 0;
    List<ShipmentDetail> shipmentDetailsReference = new ArrayList<>();


    public Shipment getShipment() {
        return shipment;
    }


    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    private boolean isAccepted;


    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }


    public SimpleItem() {
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

        private View accept, ontheway, reched, upcoming;
        public TextView tv_Accept, tv_ShipmentNumber, tv_Date;
        public TextView upload_file;
        public Button information, reject;


        int aStatusID;
        String orderId;
        String rEceiverId;
        String receiverlatitude;
        String receiverLongitude;
        Integer status;
        Context context;


        public ViewHolder(View view) {
            super(view);

            tv_Accept = view.findViewById(R.id.tv_Accept);
            tv_ShipmentNumber = view.findViewById(R.id.tv_ShipmentNumber);
            tv_Date = view.findViewById(R.id.tv_Date);
            accept = view.findViewById(R.id.accept);
            ontheway = view.findViewById(R.id.ontheway);
            reched = view.findViewById(R.id.reched);
            upcoming = view.findViewById(R.id.upcoming);
            upload_file = view.findViewById(R.id.upload_file);
            information = view.findViewById(R.id.information);
            reject = view.findViewById(R.id.reject);
            context = view.getContext();


        }

        @Override
        public void bindView(@NonNull final SimpleItem item, @NonNull List<Object> payloads) {

            Context ctx = itemView.getContext();

            tv_Date.setText(item.shipment.getDate());
            tv_ShipmentNumber.setText(item.shipment.getShipMentNo());

//            if (item.isAccepted) {
//                reject.setVisibility(View.GONE);
//
//            } else {
//                reject.setVisibility(View.VISIBLE);
//            }
//
//
//
//
//            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_upcoming));
//
//            tv_Accept.setText(item.getShipment().getTextViewState().getTextLable());
//
//            view_forAll.setBackgroundColor(ContextCompat.getColor(view_forAll.getContext(), item.shipment.getTextViewState().getColor()));
//
//
//
//            if (tv_Accept.getText().toString().equalsIgnoreCase("ACCEPT")) {
//                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_accept));
//
//            } else if (tv_Accept.getText().toString().equalsIgnoreCase("ON THE WAY")) {
//                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_ontheway));
//
//            } else if (tv_Accept.getText().toString().equalsIgnoreCase("REACHED")) {
//                tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_reached));
//            }
//
//            if (tv_Accept.getText().toString().equalsIgnoreCase("REACHED")) {
//                upload_file.setVisibility(View.VISIBLE);
//            }

            // Maintaining the state of the shipment
            try {
                switch (item.getShipment().getStatusId()) {

                    case 0:
                        System.out.println("ViewHolder.bindView - - CASE 0  " + item.getShipment().getStatusId());
                        break;

                    case 1:   // active just coming

                        System.out.println("ViewHolder.bindView -CASE 1 - " + item.getShipment().getStatusId());
                        aStatusID = item.getShipment().getStatusId();
                        orderId = item.getShipment().getShipMentNo();


                        tv_Accept.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);

                        // ACCEPT CLCIK GOES HERE
                        tv_Accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                HttpModule.provideRepositoryService().getCheckedStatusAPI(orderId).enqueue(new Callback<ResponseCheckedStatus>() {
                                    @Override
                                    public void onResponse(Call<ResponseCheckedStatus> call, Response<ResponseCheckedStatus> response) {

                                        if (response.body().isSuccess) {

                                            item.getShipment().setStatusId(response.body().status);  //1 2 3
                                            receiverlatitude = response.body().destination.lat;
                                            receiverLongitude = response.body().destination.lng;
                                            rEceiverId = response.body().destination.id.toString();
                                            fastAdapter.notifyAdapterDataSetChanged();
                                        } else {
                                            TastyToast.makeText(context, "Error Occurred", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseCheckedStatus> call, Throwable t) {
                                        System.out.println("ViewHolder.onFailure - - " + t);
                                    }
                                });

                            }
                        });


                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                                    return;
                                }
                                lastClickTime = SystemClock.elapsedRealtime();

//                            new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
//                                    .setTopColorRes(R.color.colorAccent)
//                                    .setButtonsColorRes(R.color.colorPrimaryDark)
//                                    .setIcon(R.drawable.app_icon)
//                                    .setTitle(R.string.donate_title)
//                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                            rejectingOrder(item);
//
//                                        }
//                                    })
//                                    .setNegativeButton(android.R.string.no, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            TastyToast.makeText(context, "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                                        }
//                                    })
//                                    .show();

                            }
                        });

                        break;

//              ON THE WAY CLICK GOES HERE
                    case 2:   // on the way

                        System.out.println("ViewHolder.bindView - CASE 2- " + item.getShipment().getStatusId());


                        reject.setVisibility(View.GONE);
                        tv_Accept.setText("ON THE WAY");
                        tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_ontheway));
                        accept.setVisibility(View.GONE);
                        ontheway.setVisibility(View.VISIBLE);


                        tv_Accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                HttpModule.provideRepositoryService().getRealTimeLocationCheckedStatusAPI(orderId, rEceiverId, receiverlatitude, receiverLongitude).enqueue(new Callback<ResponseRealTimeLocationCheckedStatus>() {
                                    @Override
                                    public void onResponse(Call<ResponseRealTimeLocationCheckedStatus> call, Response<ResponseRealTimeLocationCheckedStatus> response) {
                                        if (response.body().isSuccess) {

                                            item.getShipment().setStatusId(response.body().status);
                                            fastAdapter.notifyAdapterDataSetChanged();

                                            tv_Accept.setText("REACHED");
                                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_reached));
                                            accept.setVisibility(View.GONE);
                                            ontheway.setVisibility(View.GONE);
                                            reched.setVisibility(View.VISIBLE);
                                            upload_file.setVisibility(View.VISIBLE);

                                            upload_file.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Intent intent = new Intent(context, UploadImageActivity.class);
                                                    context.startActivity(intent);
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseRealTimeLocationCheckedStatus> call, Throwable t) {
                                        System.out.println("ShipmenActivity.onFailure - - -" + t);
                                    }
                                });


                            }
                        });


                        break;


                    case 3:  // reached
                        System.out.println("ViewHolder.bindView - CASE 3- " + item.getShipment().getStatusId());
                        break;

                    case 4: //rejected


                        break;
                }


            } catch (Exception e) {
                System.out.println("ViewHolder.bindView - - " + e);
            }
        }

        private void rejectingOrder(final SimpleItem item) {

            HttpModule.provideRepositoryService().getOrderRejectedAPI(orderId).enqueue(new Callback<ResponseGetOrderRejected>() {
                @Override
                public void onResponse(Call<ResponseGetOrderRejected> call, Response<ResponseGetOrderRejected> response) {

                    if (response.body().isSuccess) {
                        TastyToast.makeText(context, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        item.getShipment().setStatusId(response.body().status);
                        fastAdapter.notifyAdapterDataSetChanged();
                    } else {

                        TastyToast.makeText(context, "Getting False Value", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetOrderRejected> call, Throwable t) {

                    System.out.println("ShipmenActivity.onFailure - - " + t);

                }
            });
        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {

        }


    }


}
