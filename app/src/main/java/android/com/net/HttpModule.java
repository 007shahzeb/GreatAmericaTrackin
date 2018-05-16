package android.com.net;

import android.com.garytransportnew.BuildConfig;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpModule {


    private static OkHttpClient getOkkHttpClient() {
        System.out.println("HttpModule.getOkkHttpClient - - - Shahzeb123 getOkkHttpClient ");
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()/*.addInterceptor(logging)*//*.addNetworkInterceptor(new StethoInterceptor())*/.readTimeout(180, TimeUnit.SECONDS).connectTimeout(180,TimeUnit.SECONDS).build();

    }


    public static Retrofit getRetroFitClient() {
        System.out.println("HttpModule.getRetroFitClient - - - Shahzeb123 getRetroFitClient ");
        return new Retrofit.Builder()
                .baseUrl("http://34.234.186.44:4000/driverapp/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkkHttpClient())
                .build();

    }

    public static RemoteRepositoryService provideRepositoryService() {
        System.out.println("HttpModule.provideRepositoryService - - - Shahzeb123 provideRepositoryService");
        return getRetroFitClient().create(RemoteRepositoryService.class);
    }


}
