package com.example.work.simplecalcapp;

import android.content.Context;

/**
 * Created by work on 2018/01/05.
 */

public class Util {
    //pxをdpに変換
    //public static int convertPxToDp(Context context, int px) {
    public static int convertPxToDp(Context context, int px) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) ((px / d) + 0.5);
    }

    //dpをpxに変換
    public static int convertDpToPx(Context context, int dp) {
        float d = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * d) + 0.5);
    }
}