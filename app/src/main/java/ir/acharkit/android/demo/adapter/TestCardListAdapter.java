package ir.acharkit.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ir.acharkit.android.component.cardList.adapter.CardListAdapter;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.model.TestCardListModel;
import ir.acharkit.android.imageLoader.ImageLoader;
import ir.acharkit.android.imageLoader.OnImageLoadListener;
import ir.acharkit.android.util.Log;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public class TestCardListAdapter extends CardListAdapter {

    private static final String TAG = TestCardListAdapter.class.getSimpleName();
    private final Context context;
    private MyViewHolder vh;


    public TestCardListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.carousel_item, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        vh = (MyViewHolder) holder;
        final TestCardListModel model = (TestCardListModel) getItems().get(position);

        Log.d(TAG, "ID : " + model.getId());
        Log.d(TAG, "TITLE : " + model.getTitle());
        vh.id.setText("ID: " + model.getId());

        ImageLoader.Builder builder = new ImageLoader.Builder(context, vh.imageView).setPlaceHolder(R.mipmap.ic_launcher).setTrust(true).setTimeOut(60 * 2000).setOnImageLoadListener(new OnImageLoadListener() {
            @Override
            public void onCompleted(ImageView imageView) {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onFailure(String reason) {
                Log.d(TAG, "fail: " + reason);
            }
        });
        builder.load(model.getImageUri());

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class MyViewHolder extends ViewHolder {
        public TextView id, title;
        public ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.carousel_item_text);
            imageView = view.findViewById(R.id.carousel_item_content);
        }
    }
}
