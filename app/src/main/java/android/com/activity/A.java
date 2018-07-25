package android.com.activity;

import android.com.activity.MyCallBack.IresultCallback;
import android.com.adapters.AdapterMade;
import android.com.apiResponses.shipmentList.ShipmentList;
import android.com.apiResponses.shipmentList.ShipmentListMain;
import android.com.garytransportnew.R;
import android.com.interfaces.ItemClickListener;
import android.com.net.HttpModule;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class A extends AppCompatActivity implements IresultCallback { // ItemClickListener


    RecyclerView recylerId;

    private AdapterMade adapterMade;
    ArrayList<ShipmentList> eventSuccessArrayList = new ArrayList<>();

    private ItemClickListener itemClickListener;

    Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act);

        context = this;

        hidingActionBar();
        findingIdsHere();
        initializingAdpterHere();


    }

    private void hidingActionBar() {
        // Hiding Action in a particular Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void initializingAdpterHere() {

        System.out.println("A.initializingAdpterHere");
        compositeDisposable.add(HttpModule.provideRepositoryService().getShipmentListTest(MainActivity.enteredMobileNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShipmentListMain>() {
                    @Override
                    public void accept(ShipmentListMain eventSuccess) throws Exception {


                        if (eventSuccess.isSuccess) {

                            System.out.println("A.accept");

                            eventSuccessArrayList = new ArrayList<>(eventSuccess.shipmentList);


                            TastyToast.makeText(getApplicationContext(), eventSuccess.message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                          adapterMade = new AdapterMade(A.this,eventSuccessArrayList);







                            recylerId.setAdapter(adapterMade);


                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        compositeDisposable.dispose();
                        throw new RuntimeException("I'm a cool exception and I crashed the main thread!");


                    }
                }));


    }

    private void findingIdsHere() {
        recylerId = findViewById(R.id.recylerId);
        recylerId.setHasFixedSize(true);
        recylerId.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void getResult(AdapterMade.MadeViewHolder madeViewHolder, int position) {
        Log.i("", "getResult: "+position);

        madeViewHolder.itemView.getId();


    }
}
