package ir.acharkit.android.demo.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import ir.acharkit.android.imageLoader.ImageLoader;
import ir.acharkit.android.imageLoader.OnImageLoaderListener;
import ir.acharkit.android.imageLoader.cache.OnCacheListener;
import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.util.Logger;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/8/17
 * Email:   alirezat775@gmail.com
 */
public class TestImageLoader extends AbstractActivity {

    private static final String TAG = TestImageLoader.class.getSimpleName();
    private String url0 = "https://api.androidhive.info/images/glide/large/bourne.jpg";
    private String url1 = "https://api.androidhive.info/images/glide/large/deadpool.jpg";
    private String url2 = "https://api.androidhive.info/images/glide/large/bvs.jpg";
    private String url3 = "https://api.androidhive.info/images/glide/large/cacw.jpg";
    private String url4 = "https://api.androidhive.info/images/glide/large/squad.jpg";

    private ArrayList<String> list = new ArrayList<>();
    private ImageLoader.Builder imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_loader);

        list.add(url0);
        list.add(url1);
        list.add(url2);
        list.add(url3);
        list.add(url4);

        final ImageView image = findViewById(R.id.image_view);
        imageLoader = new ImageLoader.Builder(this, null);

        Button random = findViewById(R.id.random);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int index = random.nextInt(list.size());
                imageLoader.setImageLoaderListener(new OnImageLoaderListener() {
                    @Override
                    public void onStart(ImageView imageView, String url) {
                        Logger.d(TAG, "onStart:-- " + "imageView: " + imageView + "url: " + url);
                    }

                    @Override
                    public void onCompleted(ImageView imageView, String url, Bitmap bitmap) {
                        Logger.d(TAG, "onCompleted:-- " + "image: " + image.toString() + "response: " + imageView.toString());
                    }

                    @Override
                    public void onFailure(String reason) {
                        Logger.d(TAG, "onFailure:-- " + reason);
                    }
                }).load(image, list.get(index));
            }
        });

        random.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imageLoader.clearCache(new OnCacheListener() {
                    @Override
                    public void onCompleted() {
                        Logger.d(TAG, "onCompleted: -- clearCache");
                    }
                });
                return false;
            }
        });
    }
}
