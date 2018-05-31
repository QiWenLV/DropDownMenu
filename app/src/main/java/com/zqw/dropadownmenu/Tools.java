package com.zqw.dropadownmenu;


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by 启文 on 2018/5/21.
 */
public class Tools {


    public static int dp2px(Context context, float value){

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm);

    }


}
