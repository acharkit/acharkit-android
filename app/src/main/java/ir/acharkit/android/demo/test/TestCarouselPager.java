package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.CardList;
import ir.acharkit.android.component.cardList.CardListSnappingListener;
import ir.acharkit.android.component.cardList.CardListView;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.adapter.TestCardListAdapter;
import ir.acharkit.android.demo.model.TestCardListModel;
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
    private ArrayList<String> list = new ArrayList<>();
    private CardList cardList;


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
        if (cardList != null) {
            cardList.setAutoScrollPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cardList != null) {
            cardList.setAutoScrollResume();
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

        Button add_carousel = findViewById(R.id.add_carousel);
        TestCardListAdapter carouselPagerAdapter = new TestCardListAdapter(this);
        cardList = new CardList(this, R.id.carousel, carouselPagerAdapter);
        cardList.setOrientation(CardListView.HORIZONTAL, true);
        cardList.setAutoScroll(true, 5000, true);
        cardList.setScaleView(true);
        for (int i = 0; i < 10; i++) {
            count = i;
            TestCardListModel model = new TestCardListModel();
            model.setId(i);
            model.setTitle("# " + i);
            model.setImageUri(list.get(i));
            cardList.add(model);
        }

        cardList.setSnappingListener(new CardListSnappingListener() {
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
                TestCardListModel model = new TestCardListModel();
                model.setId(count);
                model.setTitle("# " + count);
                model.setImageUri(list.get(index));
                cardList.add(model);
            }
        });
    }
}