package android.com.models;

import android.com.garytransportnew.R;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

public class TextViewState {

    private String textLable;
    private int color;

    public TextViewState(int color, String textLable) {
        this.color = color;
        this.textLable = textLable;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTextLable() {
        return textLable;
    }

    public void setTextLable(String textLable) {
        this.textLable = textLable;
    }




  /*  int upcomingColor = ContextCompat.getColor(context, R.color.upcomingColorCode);
    int acceptColor = ContextCompat.getColor(context, R.color.acceptColorCode);
    int onthewayColor = ContextCompat.getColor(context, R.color.onthewatColorCode);
    int reachedColor = ContextCompat.getColor(context, R.color.reachedColorCode);*/


}
