package android.com.sharedPrefrence;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {


    // https://github.com/nickescobedo/Android-Shared-Preferences-Helper/blob/master/SharedPreferenceHelper.java


    private final static String PREF_FILE = "PREFERENCE";
    private Context context;
    SharedPreferences sharedPreferences;


    public PreferencesManager(Context context) {

        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }


    public static void setLogin(boolean login, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first", login);
        editor.clear();
        editor.apply();

    }


    public static boolean getLogin(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("first", false);
    }


}
