package yaujen.bankai.myapplication;

import android.util.Log;

import java.text.DecimalFormat;

public class Utility {

    public static void aLog(String tag, String msg){
        Log.println(Log.ASSERT,tag,msg);
    }

    public static String dF2(double dou){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(dou);
    }
}
