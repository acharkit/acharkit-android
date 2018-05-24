package ir.acharkit.android.demo.test;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ir.acharkit.android.app.AbstractActivity;
import ir.acharkit.android.component.BottomSheet;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.util.Colour;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    5/17/18
 * Email:   alirezat775@gmail.com
 */
public class TestBottomSheet extends AbstractActivity {

    private static final String TAG = TestBottomSheet.class.getSimpleName();
    private boolean isCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bottom_sheet);
        final BottomSheet bottomSheet = new BottomSheet(this, R.id.bottom_sheet);
        bottomSheet.setHeight(0.7f);
        bottomSheet.setBackgroundColor(Colour.DKGRAY);
        bottomSheet.setStateChangeListener(new BottomSheet.Callback() {
            @Override
            public void collapse() {
                Log.d(TAG, "collapse");
            }

            @Override
            public void show() {
                Log.d(TAG, "show");
            }
        });

        ListView listView = findViewById(R.id.list);
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

        findViewById(R.id.show_collapse_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollapsed) {
                    isCollapsed = false;
                    bottomSheet.show();
                } else {
                    isCollapsed = true;
                    bottomSheet.collapse();
                }
            }
        });

    }
}
