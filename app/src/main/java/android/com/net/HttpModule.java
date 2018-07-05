package android.com.net;

import android.com.garytransportnew.BuildConfig;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpModule {


    private static OkHttpClient getOkkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()/*.writeTimeout(5, TimeUnit.MINUTES)/*.addInterceptor(logging)*//*.addNetworkInterceptor(new StethoInterceptor())*/.readTimeout(180, TimeUnit.SECONDS).connectTimeout(180,TimeUnit.SECONDS).build();

    }


    public static Retrofit getRetroFitClient() {
        return new Retrofit.Builder()
                .baseUrl("http://34.234.186.44:4000/driverapp/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Shahzeb added this to use RxJava
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkkHttpClient())
                .build();

    }

    public static RemoteRepositoryService provideRepositoryService() {
        return getRetroFitClient().create(RemoteRepositoryService.class);
    }


}
