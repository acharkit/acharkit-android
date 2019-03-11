package ir.acharkit.android.component.roster.adapter;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;
import ir.acharkit.android.component.roster.model.RosterModel;
import ir.acharkit.android.util.helper.ViewHelper;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public abstract class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.ViewHolder> {

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    private boolean enableSlider = false;
    private List<RosterModel> items = new ArrayList<>();


    private void imageOption(View view) {
        view.getLayoutParams().width = Math.round(ViewHelper.getScreenWidth());
        view.getLayoutParams().height = (int) Math.round(view.getLayoutParams().width * 0.70);
        view.requestLayout();
    }

    /**
     * @return list items
     */
    public List<RosterModel> getItems() {
        return items;
    }

    /**
     * @return count of items in the list
     */
    @CallSuper
    @Override
    public int getItemCount() {
        return getItems().size();
    }

    /**
     * @param item      instance RosterModel
     * @param operation action add or remove
     */
    public synchronized void operation(RosterModel item, int operation) {
        switch (operation) {
            case ADD:
                add(item);
                break;
            case REMOVE:
                remove(item);
                break;
        }
    }

    public void addAll(List items) {
        this.items = items;
    }

    /**
     * add item to list and notifyDataSetChanged adapter
     *
     * @param item instance RosterModel
     */
    private void add(RosterModel item) {
        getItems().add(item);
        notifyDataSetChanged();
    }

    /**
     * remove item from list and notifyDataSetChanged adapter
     *
     * @param item instance RosterModel
     */
    private void remove(RosterModel item) {
        getItems().remove(item);
        notifyDataSetChanged();
    }

    private boolean isEnableSlider() {
        return enableSlider;
    }

    public void setEnableSlider(boolean enableSlider) {
        this.enableSlider = enableSlider;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            if (isEnableSlider()) imageOption(itemView);
        }
    }
}
