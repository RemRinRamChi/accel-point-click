package yaujen.bankai.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;
import static yaujen.bankai.pointandclick.Utility.aLog;

public class WikipediaActivity extends AppCompatActivity {
    private MouseView mouseView;
    private ConstraintLayout constraintLayout;
    private TextView linksLeft;
    private TextView bodyText;

    private MovableFloatingActionButton movableButtonView;
    private Button startButton;

    private List<String> links;
    private long startTime;
    private int correctClicks;
    private int totalClicks;
    private boolean hasStarted;

    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;

    private Intent resultsIntent;

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
        controlMethod = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));


        aLog("Wikipedia", controlMethod);
        aLog("Wikipedia", clickingMethod);
        aLog("Wikipedia", tiltGain + "");

        mouseView.enablePositionControl(controlMethod.equals(DemoActivity.CONTROL_METHODS[0]));
        mouseView.setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        mouseView.setPosTiltGain(tiltGain);
        mouseView.setVelTiltGain(tiltGain);
        mouseView.enableRecalibrationByVolumeUp(true);

        linksLeft = findViewById(R.id.links);

        // Keeps track of clicks made by the user
        bodyText = findViewById(R.id.bodyText);
        bodyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasStarted && !taskFinished()) {
                    totalClicks++;
                    aLog("Wikipedia", "Clicked");
                }
            }
        });

        createWikipediaText();

        hasStarted = false;
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasStarted) {
                    hasStarted = true;
                    startTime = System.currentTimeMillis();
                    updateText();
                } else if (taskFinished()){
                    goToResults();
                }
            }
        });
    }

    private void createWikipediaText() {
        // Converts sample wikipedia text file into a string
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

        links = new ArrayList<>();
        URLSpan[] urlSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);

        // Adds a click listener to every url
        for (final URLSpan urlSpan: urlSpans) {
            final String url = urlSpan.getURL();

            // Only add links we want to the task
            if (!url.equals("-1")) {
                links.add(url);
            }


            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (hasStarted && links.contains(url)) {
                        links.remove(url);
                        correctClicks++;
                        updateText();
                    }
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
        // Update list of links to click
        if (!taskFinished()) {
            Iterator<String> iterator = links.iterator();
            String linksRemaining = "<b>Click these links:</b><br>";
            while (iterator.hasNext()) {
                linksRemaining += iterator.next() + "<br>";
            }
            linksLeft.setText(Html.fromHtml(linksRemaining));
        }

        // Handle when task is finished
        else {
            totalClicks++; // increment because last link clicked doesn't increment total clicks
            long timeTaken = System.currentTimeMillis() - startTime;

            linksLeft.setText((Html.fromHtml("<b>Done!</b>")));
            startButton.setText("View results");

            resultsIntent = new Intent(this, ResultsActivity.class);
            resultsIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            resultsIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            resultsIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            resultsIntent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
            resultsIntent.putExtra(KEY_NAME_ERR_COUNT, totalClicks - correctClicks);
        }
    }

    private void goToResults() {
        aLog("Wikipedia", "Task finished: " + correctClicks + "/" + totalClicks);
        startActivity(resultsIntent);
    }

    private boolean taskFinished() {
        return links.size() == 0;
    }

    //pausing the mouse view when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        mouseView.pause();
    }

    //running the mouse view when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        mouseView.resume();
    }
}
