package yaujen.bankai.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static yaujen.bankai.myapplication.Utility.aLog;

public class Mouse {
    private Bitmap bitmap;
    private Context context;

    //coordinates
    private double x;
    private double y;

    // TODO delete these fields later, dev purposes only
    public double pitch;
    public double roll;
    public double dir;

    public Mouse(Context context, double initialX, double initialY){
        this.context = context;

        x = initialX;
        y = initialY;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cursor);
    }

    public void displace(double xVal, double yVal){
        update(x+xVal,y+yVal);
    }

    public void update(double xVal, double yVal){
        if(!Double.isNaN(xVal) && !Double.isNaN(yVal)){
            x = xVal;
            y = yVal;

            double wPx = context.getResources().getDisplayMetrics().widthPixels - bitmap.getWidth()/2;
            double hPx = context.getResources().getDisplayMetrics().heightPixels - bitmap.getHeight()/2;

            // bounding the pointer
            if(x > wPx){
                x = wPx;
            } else if (x<0){
                x = 0;
            }
            if(y > hPx){
                y = hPx;
            } else if (y<0){
                y = 0;
            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
