package com.mx.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class ScreenUtil {
    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    public static  int getScreenWidth(Context context){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
