package ir.acharkit.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ir.acharkit.android.component.Tag;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.model.TestTagModel;


/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/29/18
 * Email:   alirezat775@gmail.com
 */
public class TestTagAdapter extends Tag.TagAdapter<Tag.ViewHolder> {

    private Context context;
    private MyViewHolder viewHolder;
    private ArrayList<TestTagModel> tagList;

    public TestTagAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Tag.ViewHolder onCrateTagViewItem(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.tag_item, parent, false);
        viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindTagViewItem(Tag.ViewHolder holder, int position) {
        viewHolder = (MyViewHolder) holder;
        final TestTagModel model = (TestTagModel) getTagList().get(position);
        viewHolder.textView.setText(model.getTitle());
    }

    private Context getContext() {
        return context;
    }


    private class MyViewHolder extends Tag.ViewHolder {

        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tag_view_item_title);
        }
    }
}
