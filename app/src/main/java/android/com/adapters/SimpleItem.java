package android.com.adapters;

import android.com.activity.ShipmenActivity;
import android.com.activity.UploadImageActivity;
import android.com.garytransportnew.R;
import android.com.models.Shipment;
import android.com.net.HttpModule;
import android.com.responseModel.ResponseCheckedStatus;
import android.com.responseModel.ResponseGetOrderRejected;
import android.com.responseModel.ResponseRealTimeLocationCheckedStatus;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.sdsmdg.tastytoast.TastyToast;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.com.activity.ShipmenActivity.fastAdapter;


public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.ViewHolder> {


    private Context context;
    private Shipment shipment;


    public Shipment getShipment() {
        return shipment;
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




    public class ViewHolder extends FastAdapter.ViewHolder<SimpleItem> {
        protected View view;

        private View accept, ontheway, reched, upcoming;
        public TextView tv_Accept, tv_ShipmentNumber, tv_Date;
        public TextView upload_file;
        public Button information, reject;


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

            tv_Date.setText(item.shipment.getDeliveryDate());
            tv_ShipmentNumber.setText(item.shipment.getShipMentNo());

            // Maintaining the state of the shipment
            try {
                if (item.getShipment().isFirst()) {

                    switch (item.getShipment().getStatusId()) {

                        case 0:
                            break;

                        case 1:   // active just coming


                            tv_Accept.setVisibility(View.VISIBLE);

                            tv_Accept.setText("ACCEPT");
                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_accept));

                            reject.setVisibility(View.VISIBLE);
                            ontheway.setVisibility(View.GONE);
                            reched.setVisibility(View.GONE);
                            upcoming.setVisibility(View.GONE);
                            accept.setVisibility(View.VISIBLE);

                            // ACCEPT CLCIK GOES HERE
                            tv_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    HttpModule.provideRepositoryService().getCheckedStatusAPI(item.getShipment().getShipMentNo()).enqueue(new Callback<ResponseCheckedStatus>() {
                                        @Override
                                        public void onResponse(Call<ResponseCheckedStatus> call, Response<ResponseCheckedStatus> response) {

                                            if (response.body().getSuccess()) {
                                                item.getShipment().setStatusId(response.body().getStatus());
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
                            break;

                        case 2:   // on the way



                            reject.setVisibility(View.GONE);
                            tv_Accept.setVisibility(View.VISIBLE);
                            tv_Accept.setText("ON THE WAY");
                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_ontheway));
                            accept.setVisibility(View.GONE);
                            upcoming.setVisibility(View.GONE);
                            reched.setVisibility(View.GONE);
                            ontheway.setVisibility(View.VISIBLE);



                            tv_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    HttpModule.provideRepositoryService().getRealTimeLocationAPI(item.getShipment().getShipMentNo(),"" , "").enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                            if (response.body().i) {
                                            Log.i("", "onResponse: "+response.toString());
                                                item.getShipment().setStatusId(100);
                                               EventBus.getDefault().post(item.getShipment().getShipMentNo()+","+item.getShipment().getRcrecieverId()+","+item.getShipment().getRclat()+","+item.getShipment().getRclang());

                                          /*  } else {
                                                TastyToast.makeText(context, response.body().message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                                            }*/
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            System.out.println("ShipmenActivity.onFailure - - -" + t);
                                        }
                                    });
                                }
                            });
                            break;



                        case 4: //rejected
                            System.out.println("ViewHolder.bindView - Case3- -");
                            Toast.makeText(context, "REJECT CLCIK", Toast.LENGTH_SHORT).show();
//                            reject.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//                                    new LovelyStandardDialog(context, LovelyStandardDialog.ButtonLayout.VERTICAL)
//                                            .setTopColorRes(R.color.colorAccent)
//                                            .setButtonsColorRes(R.color.colorPrimaryDark)
//                                            .setIcon(R.drawable.app_icon)
//                                            .setTitle(R.string.donate_title)
//                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
////                                                    rejectingOrder(item);
//                                                }
//                                            })
//                                            .setNegativeButton(android.R.string.no, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    TastyToast.makeText(context, "Cancelled", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                                                }
//                                            })
//                                            .show();
//                                }
//                            });

                            break;


                        case 10:

                            Toast.makeText(context, "aStatus Test ", Toast.LENGTH_SHORT).show();


                            System.out.println("ViewHolder.bindView - CASE 3- " + item.getShipment().getStatusId());
                            Toast.makeText(context, "REACHED CLCIK", Toast.LENGTH_SHORT).show();


                            tv_Accept.setText("REACHED");
                            tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_reached));
                            ontheway.setVisibility(View.GONE);
                            reched.setVisibility(View.VISIBLE);
                            upload_file.setVisibility(View.VISIBLE);
                            reject.setVisibility(View.GONE);
                            upcoming.setVisibility(View.GONE);
                            accept.setVisibility(View.GONE);

                            upload_file.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(context, UploadImageActivity.class);
                                    intent.putExtra("orderId",item.getShipment().getShipMentNo()+"");
                                    context.startActivity(intent);

                                }
                            });

                            break;
                    }

                } else {
                    tv_Accept.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                    upload_file.setVisibility(View.GONE);
                    tv_Accept.setText("UPCOMING");
                    tv_Accept.setBackground(tv_Date.getContext().getResources().getDrawable(R.drawable.rounded_button_upcoming));
                    upcoming.setVisibility(View.VISIBLE);
                    ontheway.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);
                    reched.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                System.out.println("ViewHolder.bindView - - " + e);
            }
        }

        private void rejectingOrder(final SimpleItem item) {

            HttpModule.provideRepositoryService().getOrderRejectedAPI(item.getShipment().getShipMentNo()).enqueue(new Callback<ResponseGetOrderRejected>() {
                @Override
                public void onResponse(Call<ResponseGetOrderRejected> call, Response<ResponseGetOrderRejected> response) {

                    if (response.body().isSuccess) {

                        TastyToast.makeText(context, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        removeAt(getAdapterPosition());

                        Intent intent = new Intent(context, ShipmenActivity.class);
                        context.startActivity(intent);


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

        private void removeAt(int position) {

            fastAdapter.remove(position);
            fastAdapter.notifyAdapterItemRemoved(position);
            fastAdapter.notifyAdapterItemChanged(position);
            fastAdapter.notifyAdapterDataSetChanged();

        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {

        }


    }




}
