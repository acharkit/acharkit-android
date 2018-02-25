package ir.acharkit.android.component.carousel.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.carousel.model.CarouselModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public abstract class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    private List<CarouselModel> items = new ArrayList<>();

    /**
     * @return list items
     */
    public List<CarouselModel> getItems() {
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
     * @param item      instance CarouselModel
     * @param operation action add or remove
     */
    public synchronized void operation(CarouselModel item, int operation) {
        switch (operation) {
            case ADD:
                add(item);
                break;
            case REMOVE:
                remove(item);
                break;
        }
    }

    /**
     * add item to list and notifyDataSetChanged adapter
     * @param item instance CarouselModel
     */
    private void add(CarouselModel item) {
        getItems().add(item);
        notifyDataSetChanged();
    }

    /**
     * remove item from list and notifyDataSetChanged adapter
     * @param item instance CarouselModel
     */
    private void remove(CarouselModel item) {
        getItems().remove(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
