package yaujen.bankai.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Mouse {
    private Bitmap bitmap;

    //coordinates
    private int initialX;
    private int initialY;
    private int x;
    private int y;

    // TODO delete these fields later, dev purposes only
    public int pitch;
    public int roll;
    public double dir;
    public int calibratePitch;

    //motion speed of the character
    private int speed = 0;

    public Mouse(Context context){
        initialX = context.getResources().getDisplayMetrics().widthPixels/2;
        initialY = (context.getResources().getDisplayMetrics().heightPixels/2);

        x = initialX;
        y = initialY;

        calibratePitch=0;

        speed = 1;

        //Getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cursor);
    }

    //Method to update coordinate of character
    public void update(){
        //updating x coordinate
        y+=3;
    }

    public void update(int xVal, int yVal){
        x = xVal;
        y = yVal;
    }

    /*
     * These are getters you can generate it autmaticallyl
     * right click on editor -> generate -> getters
     * */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

}
