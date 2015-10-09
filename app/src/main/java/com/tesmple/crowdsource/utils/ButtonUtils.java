package com.tesmple.crowdsource.utils;

import android.content.Context;

import com.gc.materialdesign.views.ButtonFlat;

/**
 * Created by ESIR on 2015/10/8.
 */
public class ButtonUtils {
    private Context context;

    public void ButtonUtils(Context context){
        this.context = context;
    }
    /**
     * 这个方法用来修改ButtonFlat的textcolor
     * @param buttonFlat
     * @param textColor
     */
    public static void setBtnFlatTextColor(ButtonFlat buttonFlat,int textColor){
        buttonFlat.setBackgroundColor(textColor);
    }
}
