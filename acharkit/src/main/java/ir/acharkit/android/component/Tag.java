package ir.acharkit.android.component;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.tag.TagView;
import ir.acharkit.android.component.tag.model.TagModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    3/28/18
 * Email:   alirezat775@gmail.com
 */
public class Tag {


    private static final int ADD = 0;
    private static final int REMOVE = 1;
    private static TagView tagView;
    private static List<TagModel> tagList = new ArrayList<>();
    private AppCompatActivity activity;
    private TagAdapter<ViewHolder> tagAdapter;

    /**
     * @param activity
     * @param tagViewId
     * @param tagAdapter
     */
    public Tag(@NonNull AppCompatActivity activity, @IdRes int tagViewId, TagAdapter<ViewHolder> tagAdapter) {
        View view = activity.getWindow().getDecorView();
        init(activity, view, tagViewId, tagAdapter);

    }

    /**
     * @param activity
     * @param view
     * @param tagViewId
     * @param tagAdapter
     */
    public Tag(@NonNull AppCompatActivity activity, View view, @IdRes int tagViewId, TagAdapter<ViewHolder> tagAdapter) {
        init(activity, view, tagViewId, tagAdapter);

    }

    /**
     * @return
     */
    public static List<TagModel> getTagList() {
        return tagList;
    }

    /**
     * @param activity
     * @param view
     * @param tagViewId
     * @param tagAdapter
     */
    private void init(AppCompatActivity activity, View view, int tagViewId, TagAdapter<ViewHolder> tagAdapter) {
        this.activity = activity;
        this.tagAdapter = tagAdapter;
        tagView = view.findViewById(tagViewId);
    }

    /**
     * @return
     */
    public int getItemCount() {
        return getTagList().size();
    }

    /**
     * @param items
     */
    public void addAll(@NonNull ArrayList items) {
        tagView.removeAllViews();
        getTagList().clear();
        tagList = items;
        for (int i = 0; i < items.size(); i++) {
            tagView.addView(tagAdapter.itemRootView());
            tagAdapter.bindView();
        }
        notifyData();
    }

    /**
     * @param items
     */
    public void updateAll(@NonNull ArrayList items) {
        getTagList().clear();
        addAll(items);
        tagAdapter.bindView();
        notifyData();
    }

    /**
     * @param items
     */
    public void add(@NonNull TagModel items) {
        getTagList().add(items);
        tagView.addView(tagAdapter.itemRootView());
        tagAdapter.bindView();
        notifyData();
    }

    /**
     * @param items
     */
    public void remove(@NonNull TagModel items) {
        if (getItemCount() < 1)
            return;
        getTagList().remove(items);
        tagView.removeView(tagAdapter.itemRootView());
        notifyData();
    }

    /**
     * @param index
     */
    public void remove(int index) {
        if (getItemCount() < 1)
            return;
        tagView.removeView(tagView.getChildAt(index));
        getTagList().remove(index);
        notifyData();
    }

    /**
     *
     */
    private void notifyData() {
        if (tagView == null)
            throw new NullPointerException("TagView is null !!!");
        tagView.requestLayout();
        tagView.invalidate();
        getItemCount();
    }

    /**
     *
     */
    public void notifyDataSetChanged() {
        if (tagView == null)
            throw new NullPointerException("TagView is null !!!");
        tagView.requestLayout();
        tagView.invalidate();
        getItemCount();
    }

    public static abstract class ViewHolder {

        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    public static abstract class TagAdapter<VH extends ViewHolder> {

        private ViewHolder viewHolder;

        public abstract ViewHolder onCrateTagViewItem(ViewGroup parent);

        public abstract void onBindTagViewItem(ViewHolder holder, int position);

        public List getTagList() {
            return Tag.getTagList();
        }

        View itemRootView() {
            if (onCrateTagViewItem(tagView) == null)
                throw new NullPointerException("First must be create tag item");
            setViewHolder(onCrateTagViewItem(tagView));
            return getViewHolder().itemView;
        }

        void bindView() {
            View view = getViewHolder().itemView;
            if (view == null)
                return;
            int position = tagView.indexOfChild(view);
            if (position == -1)
                return;
            onBindTagViewItem(getViewHolder(), position);
        }

        private ViewHolder getViewHolder() {
            return viewHolder;
        }

        private void setViewHolder(ViewHolder holder) {
            this.viewHolder = holder;
        }
    }
}
