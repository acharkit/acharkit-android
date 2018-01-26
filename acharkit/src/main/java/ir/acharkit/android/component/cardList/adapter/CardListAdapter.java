package ir.acharkit.android.component.cardList.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ir.acharkit.android.component.cardList.model.CardListModel;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public abstract class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    public static final int ADD = 0;
    public static final int REMOVE = 1;
    private List<CardListModel> items = new ArrayList<>();

    public List<CardListModel> getItems() {
        return items;
    }

    @CallSuper
    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public void operation(CardListModel item, int operation) {
        switch (operation) {
            case ADD:
                add(item);
                break;
            case REMOVE:
                remove(item);
                break;
        }
    }

    private void add(CardListModel item) {
        getItems().add(item);
        notifyDataSetChanged();
    }

    private void remove(CardListModel item) {
        getItems().remove(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
