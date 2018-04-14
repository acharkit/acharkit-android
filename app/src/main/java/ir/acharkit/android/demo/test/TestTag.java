package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.Tag;
import ir.acharkit.android.component.tag.TagView;
import ir.acharkit.android.component.tag.model.TagModel;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.adapter.TestTagAdapter;
import ir.acharkit.android.demo.model.TestTagModel;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    10/19/2017
 * Email:   alirezat775@gmail.com
 */

public class TestTag extends AbstractActivity {

    private static final String TAG = TestTag.class.getSimpleName();
    private ArrayList<TagModel> arrayList = new ArrayList<>();
    private int j;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tag);

        TagView tagView = findViewById(R.id.tag_view);
        tagView.setGravity(Gravity.RIGHT);

        TestTagAdapter testTagAdapter = new TestTagAdapter(this);
        final Tag tag = new Tag(this, R.id.tag_view, testTagAdapter);
        for (int i = 0; i < 15; i++) {
            j = i;
            TestTagModel tagModel = new TestTagModel();
            tagModel.setId(i);
            tagModel.setTitle("# " + i);
            arrayList.add(tagModel);
        }

        tag.addAll(arrayList);

        Log.d(TAG, "tagList : " + tag.getTagList());
        Log.d(TAG, "tagList size : " + tag.getItemCount());

        findViewById(R.id.add_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestTagModel tagModel = new TestTagModel();
                j = j + 1;
                tagModel.setId(j);
                tagModel.setTitle("# " + j);
                tag.add(tagModel);
            }
        });

        findViewById(R.id.remove_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag.remove(tag.getItemCount() - 1);
            }
        });
    }
}