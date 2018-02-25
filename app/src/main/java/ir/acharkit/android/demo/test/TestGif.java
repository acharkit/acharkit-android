package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.GifView;
import ir.acharkit.android.demo.R;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/8/17
 * Email:   alirezat775@gmail.com
 */
public class TestGif extends AbstractActivity {

    private static final String TAG = TestGif.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gif);

        final GifView gifView = findViewById(R.id.gifView);
        gifView.loadResource(R.drawable.linux).setRepeatCount(GifView.INFINITE);
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gifView.isFullScreen()) gifView.setFullScreen(false);
                else gifView.setFullScreen(true);
            }
        });
    }
}
