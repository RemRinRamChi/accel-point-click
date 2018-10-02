package yaujen.bankai.pointandclick;

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

import static yaujen.bankai.pointandclick.Utility.aLog;

/**
 * MovableFloatingActionButton extends a FloatingActionButton
 * It opens up 3 customization options for the FloatingActionButton: Color, Size and Opacity
 *
 * It setups listeners to make the button movable
 *
 */
public class MovableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {
    private final static double CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    private float downRawX, downRawY;
    private float dX, dY;

    private int buttonSize = 200;           // Default button Size
    private int buttonColor = Color.CYAN;   // Default button Color
    private float buttonOpacity = 0.1f;     // Default Opacity

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

    /**
     * Setup default customization of the button and listeners for dragging
     */
    private void init() {
        setOnTouchListener(this);
        this.setButtonOpacity(buttonOpacity);
        this.setButtonSize(buttonSize);
        this.setButtonColor(buttonColor);
    }

    public void setButtonColor(int color){
        buttonColor = color;
        this.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
    }

    public void setButtonSize(int size){
        buttonSize = size;
        this.setCustomSize(buttonSize);
    }

    public void setButtonOpacity(float opacity) {
        buttonOpacity = opacity;
        this.setAlpha(buttonOpacity);
    }

    public void setVisibilityButton(boolean visible){
        if (visible) {
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.GONE);
        }

    }

    /**
     * This onTouch methods listens and reads motion events to do dragging
     * Reference: Author=ban-geoengineering Source=https://stackoverflow.com/questions/46370836/android-movable-draggable-floating-action-button-fab
     * @param view
     * @param motionEvent
     * @return
     */

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        if(motionEvent.getSource() == 420){
            aLog("floating button","own source");
            return true; // consumed
        }
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
