package yaujen.bankai.pointandclick;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Mouse object to keep track of and update the coordinates of the on screen pointer
 */
public class Mouse {
    private Bitmap bitmap;
    private Context context;

    //coordinates
    private double x;
    private double y;

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

    public void setBitmap(Bitmap newBitmap) {
        bitmap = newBitmap;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Returns MapleStory mouse bitmap
     * @param activity
     * @return MapleStory mouse bitmap
     */
    public static Bitmap getMouseBitmap1(Activity activity) {
        return BitmapFactory.decodeResource(activity.getResources(), R.drawable.cursor);
    }

    /**
     * Returns umu mouse bitmap
     *
     * source: https://twitter.com/gzn_pp/status/855705455431110657
     *
     * @param activity
     * @return umu mouse bitmap
     */
    public static Bitmap getMouseBitmap2(Activity activity) {
        return BitmapFactory.decodeResource(activity.getResources(), R.drawable.seibacur);
    }
}
