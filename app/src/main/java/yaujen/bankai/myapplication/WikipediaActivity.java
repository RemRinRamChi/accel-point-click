package yaujen.bankai.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

import static yaujen.bankai.pointandclick.Utility.aLog;

public class WikipediaActivity extends AppCompatActivity {
    private MouseView mouseView;
    private ConstraintLayout constraintLayout;
    private TextView counter;
    private TextView bodyText;

    private MovableFloatingActionButton movableButtonView;

    private int correctClicks;
    private int totalClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikipedia);
        constraintLayout = findViewById(R.id.layout);


        // How to use mouse view
        mouseView = new MouseView(this);
        constraintLayout.addView(mouseView, -1, MouseView.getFullScreenConstraintLayoutParams());
        mouseView.setClickingTargetView(findViewById(R.id.clickableLayout));

        // How to add fab clicking
        movableButtonView = new MovableFloatingActionButton(this);
        constraintLayout.addView(movableButtonView, constraintLayout.getChildCount(),MouseView.getFabConstraintLayoutParams(100,0));
        mouseView.setMovableFloatingActionButton(movableButtonView);


        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        String controlMethod = extras.getString(DemoActivity.KEY_NAME_CONTROL_METHOD);
        String clickingMethod = extras.getString(DemoActivity.KEY_NAME_CLICKING_METHOD);
        String tiltGain = extras.getString(DemoActivity.KEY_NAME_TILT_GAIN);

        aLog("Wikipedia", controlMethod);
        aLog("Wikipedia", clickingMethod);
        aLog("Wikipedia", tiltGain);

        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));

        counter = findViewById(R.id.counter);

        bodyText = findViewById(R.id.bodyText);
        bodyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalClicks++;
                updateText();
            }
        });

        InputStream is = getResources().openRawResource(R.raw.wikipedia_text);
        String text = "hello";
        try {
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Code adapted from:
        // https://stackoverflow.com/questions/12418279/android-textview-with-clickable-links-how-to-capture-clicks/19989677#19989677
        Spanned spanned = Html.fromHtml(text);
        final Spannable spannable = new SpannableStringBuilder(spanned);

        URLSpan[] urlSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);

        for (final URLSpan urlSpan: urlSpans) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    correctClicks++;
                    updateText();
                    aLog("Wikipedia", "Clicked " + urlSpan.getURL());
                }
            };

            spannable.setSpan(clickableSpan,
                    spannable.getSpanStart(urlSpan),
                    spannable.getSpanEnd(urlSpan),
                    spannable.getSpanFlags(urlSpan));
            spannable.removeSpan(urlSpan);
        }

        bodyText.setText(spannable);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());
    }



    private void updateText() {
        counter.setText((Math.round((double) correctClicks / totalClicks * 100)) + "% (" + correctClicks + "/" + totalClicks + ")");
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        mouseView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        mouseView.resume();
    }
}
