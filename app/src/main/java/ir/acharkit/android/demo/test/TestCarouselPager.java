package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.Carousel;
import ir.acharkit.android.component.carousel.CarouselListener;
import ir.acharkit.android.component.carousel.CarouselView;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.adapter.TestCarouselAdapter;
import ir.acharkit.android.demo.model.TestCarouselModel;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class TestCarouselPager extends AbstractActivity {

    private static final String TAG = TestCarouselPager.class.getSimpleName();
    private int count;

    private String url0 = "https://api.androidhive.info/images/glide/large/bourne.jpg";
    private String url1 = "https://api.androidhive.info/images/glide/large/deadpool.jpg";
    private String url2 = "https://api.androidhive.info/images/glide/large/bvs.jpg";
    private String url3 = "https://api.androidhive.info/images/glide/large/cacw.jpg";
    private String url4 = "https://api.androidhive.info/images/glide/large/squad.jpg";
    private String url5 = "https://www.w3schools.com/howto/img_fjords.jpg";
    private String url6 = "https://cloud.netlifyusercontent.com/assets/344dbf88-fdf9-42bb-adb4-46f01eedd629/68dd54ca-60cf-4ef7-898b-26d7cbe48ec7/10-dithering-opt.jpg";
    private String url7 = "https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg";
    private String url8 = "https://www.wonderplugin.com/videos/demo-image0.jpg";
    private String url9 = "https://www.aussiespecialist.com/content/asp/en_us/sales-resources/image-and-video-galleries/jcr:content/mainParsys/hero/image.adapt.1663.medium.jpg";
    private String url10 = "@drawable/linux";
    private String url11 = "@drawable/java";
    private String url12 = "@drawable/postman";
    private String url13 = "@drawable/ic_android";

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<TestCarouselModel> testCarouselModels = new ArrayList<>();
    private Carousel carousel;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!hasBackStack()) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (carousel != null) {
            carousel.setAutoScrollPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (carousel != null) {
            carousel.setAutoScrollResume();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_carousel_pager);

        list.add(url0);
        list.add(url1);
        list.add(url2);
        list.add(url3);
        list.add(url4);
        list.add(url5);
        list.add(url6);
        list.add(url7);
        list.add(url8);
        list.add(url9);
        list.add(url10);
        list.add(url11);
        list.add(url12);
        list.add(url13);

        Button add_carousel = findViewById(R.id.add_carousel);
        final TestCarouselAdapter carouselPagerAdapter = new TestCarouselAdapter(this);
        carousel = new Carousel(this, R.id.carousel, carouselPagerAdapter);
        carousel.setOrientation(CarouselView.HORIZONTAL, true);
        carousel.setAutoScroll(true, 5000, true);
        carousel.setScaleView(true);
        for (int i = 0; i < 14; i++) {
            count = i;
            TestCarouselModel model = new TestCarouselModel();
            model.setId(i);
            model.setTitle("# " + i);
            model.setImageUri(list.get(i));
            testCarouselModels.add(model);
        }

        carousel.addAll(testCarouselModels);

        carousel.setSnappingListener(new CarouselListener() {
            @Override
            public void onPositionChange(int position) {
                Log.d(TAG, "position: " + position);
            }

            @Override
            public void onScroll(int dx, int dy) {
                Log.d(TAG, "dx: " + dx + " dy: " + dy);
            }
        });

        add_carousel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Random random = new Random();
                int index = random.nextInt(list.size());
                TestCarouselModel model = new TestCarouselModel();
                model.setId(count);
                model.setTitle("# " + count);
                model.setImageUri(list.get(index));
                carousel.add(model);
            }
        });

        add_carousel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                carousel.remove(carouselPagerAdapter.getItems().get(1));
                return false;
            }
        });
    }
}