package android.com.activity;

import android.com.adapters.AdapterMade;
import android.com.garytransportnew.R;
import android.com.net.HttpModule;
import android.com.test.ShipmentInformation;
import android.com.test.ShipmentListTest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;


import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class A extends AppCompatActivity {


    RecyclerView recylerId;

    private AdapterMade adapterMade;
    ArrayList<ShipmentInformation> eventSuccessArrayList = new ArrayList<>();

    Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act);

        context = this;

        findingIdsHere();
        initializingAdpterHere();


    }

    private void initializingAdpterHere() {

        System.out.println("A.initializingAdpterHere");
        compositeDisposable.add(HttpModule.provideRepositoryService().getShipmentListTest(MainActivity.enteredMobileNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShipmentListTest>() {
                    @Override
                    public void accept(ShipmentListTest eventSuccess) throws Exception {


                        if (eventSuccess.isSuccess) {

                            System.out.println("A.accept");

                            eventSuccessArrayList = new ArrayList<>(eventSuccess.informationList);


                            TastyToast.makeText(getApplicationContext(), eventSuccess.message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                            adapterMade = new AdapterMade(A.this, eventSuccessArrayList);
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
}
