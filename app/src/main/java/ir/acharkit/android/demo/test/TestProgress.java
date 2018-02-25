package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.Progress;
import ir.acharkit.android.component.progress.AbstractProgress;
import ir.acharkit.android.demo.R;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/8/17
 * Email:   alirezat775@gmail.com
 */
public class TestProgress extends AbstractActivity {

    private static final String TAG = TestProgress.class.getSimpleName();
    private AbstractProgress loadingIndicatorProgress;
    private AbstractProgress lineIndicatorProgress;
    private AbstractProgress fadeProgress;
    private AbstractProgress translationIndicatorProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_progress);

        loadingIndicatorProgress = findViewById(R.id.loadingIndicatorProgress);
        lineIndicatorProgress = findViewById(R.id.lineIndicatorProgress);
        fadeProgress = findViewById(R.id.fadeProgress);
        translationIndicatorProgress = findViewById(R.id.translationIndicatorProgress);

        //loading progress
        final Progress loading = new Progress(this)
                .setProgress(loadingIndicatorProgress)
                .setColor(Progress.DEFAULT_COLOR);
        loading.load();

        //line progress
        final Progress line = new Progress(this)
                .setProgress(lineIndicatorProgress)
                .setColor(Progress.DEFAULT_COLOR);
        line.load();

        // fade progress
        final Progress fade = new Progress(this)
                .setProgress(fadeProgress)
                .setColor(Progress.DEFAULT_COLOR);
        fade.load();

        // translation progress
        final Progress translation = new Progress(this)
                .setProgress(translationIndicatorProgress)
                .setColor(Progress.DEFAULT_COLOR);
        translation.load();

        loadingIndicatorProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.hide();
            }
        });

        lineIndicatorProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.hide();
            }
        });
    }
}
