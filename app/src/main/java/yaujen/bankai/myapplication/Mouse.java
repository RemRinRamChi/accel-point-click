package yaujen.bankai.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Mouse {
    private Bitmap bitmap;
    private Context context;

    //coordinates
    private int x;
    private int y;

    // TODO delete these fields later, dev purposes only
    public int pitch;
    public int roll;
    public double dir;
    public int refPitch;

    public Mouse(Context context, int initialX, int initialY){
        this.context = context;

        x = initialX;
        y = initialY;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cursor);

        refPitch =0;
    }


    public void update(int xVal, int yVal){
        x = xVal;
        y = yVal;

        int wPx = context.getResources().getDisplayMetrics().widthPixels - bitmap.getWidth();
        int hPx = context.getResources().getDisplayMetrics().heightPixels - bitmap.getHeight();

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
