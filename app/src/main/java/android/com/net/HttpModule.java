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
        return new OkHttpClient.Builder().writeTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build();

    }


    public static Retrofit getRetroFitClient() {
        return new Retrofit.Builder()
                .baseUrl("http://greatamericatracking.com/driverapp/") // for testing  http://34.234.186.44:4000/driverapp/
//        http://greatamericatracking.com/driverapp/ValidateNumber
                // .baseUrl("http://18.217.234.39/EZE/public/api/addAttachmentImage")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Shahzeb added this to use RxJava
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkkHttpClient())
                .build();

    }

    public static RemoteRepositoryService provideRepositoryService() {
        return getRetroFitClient().create(RemoteRepositoryService.class);
    }


}


// .baseUrl("http://greatamericatracking.com/driverapp/") live server url