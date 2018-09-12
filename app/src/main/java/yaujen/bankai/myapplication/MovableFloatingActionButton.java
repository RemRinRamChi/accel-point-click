package yaujen.bankai.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {
    // Reference: Author=ban-geoengineering Source=https://stackoverflow.com/questions/46370836/android-movable-draggable-floating-action-button-fab
    private final static double CLICK_DRAG_TOLERANCE = 1000; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    private float downRawX, downRawY;
    private float dX, dY;

    private final static int DEFAULT_BUTTON_SIZE = 1000;        // Default button Size
    private final static int DEFAULT_BUTTON_COLOR = Color.RED;  // Default button Color

    public MovableFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public MovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
        this.setButtonSize(DEFAULT_BUTTON_SIZE);
        this.setButtonColor(DEFAULT_BUTTON_COLOR);
    }

    public void setButtonColor(int color){
        this.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setButtonSize(int size){
        this.setCustomSize(size);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;

            return true; // Consumed

        }
        else if (action == MotionEvent.ACTION_MOVE) {

            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            View viewParent = (View)view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            int buttonWidthHalf = this.getWidth()/2;
            int buttonHeightHalf = this.getHeight()/2;

            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(0-buttonWidthHalf, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth + buttonWidthHalf, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(0 - buttonHeightHalf, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight + buttonHeightHalf, newY); // Don't allow the FAB past the bottom of the parent

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true; // Consumed

        }
        else if (action == MotionEvent.ACTION_UP) {

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                return performClick();
            }
            else { // A drag
                return true; // Consumed
            }
        }
        else {
            return super.onTouchEvent(motionEvent);
        }

    }

}
