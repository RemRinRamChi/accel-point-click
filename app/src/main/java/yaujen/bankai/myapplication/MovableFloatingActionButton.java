package yaujen.bankai.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {
    // Reference: Author=ban-geoengineering Source=https://stackoverflow.com/questions/46370836/android-movable-draggable-floating-action-button-fab
    private final static double CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    private float downRawX, downRawY;
    private float dX, dY;

    private int buttonSize = 200;        // Default button Size
    private int buttonColor = Color.CYAN; // Default button Color
    private float buttonOpacity = 0.1f;

    public MovableFloatingActionButton(Context context) {
        super(context);
        init();
    }
    public MovableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MovableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
        this.setButtonSize(buttonSize);
        this.setButtonColor(buttonColor);
        this.setButtonOpacity(buttonOpacity);
    }

    public void setButtonColor(int color){
        buttonColor = color;
        this.setBackgroundColor(buttonColor);
        //this.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        // this.setBackgroundTintList(ColorStateList.valueOf(Color.argb(100,0,0,0)));
        //this.setBackgroundColor(ColorStateList.valueOf(Color.CYAN));
    }

    public void setButtonSize(int size){
        buttonSize = size;
        this.setCustomSize(buttonSize);
    }

    public void setButtonOpacity(float opacity){
        buttonOpacity = opacity;
        this.setAlpha(buttonOpacity);
    }

//    public void enableDisableViewBehaviour(CoordinatorLayout.Behavior<View> behavior, boolean enable){
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) this.getLayoutParams();
//        params.setBehavior(behavior);
//        this.requestLayout();
//        this.setVisibility((enable ? View.VISIBLE : View.GONE));
//    }

    public void enableOrDisableButton(boolean enable){
        if (enable) {
            this.show();
            //this.getBackground().setAlpha(1);
        } else {
            this.hide();
        }

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
