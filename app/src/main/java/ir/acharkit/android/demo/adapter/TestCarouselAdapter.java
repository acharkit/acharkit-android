package ir.acharkit.android.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ir.acharkit.android.component.carousel.adapter.CarouselAdapter;
import ir.acharkit.android.demo.R;
import ir.acharkit.android.demo.model.TestCarouselModel;
import ir.acharkit.android.imageLoader.ImageLoader;
import ir.acharkit.android.imageLoader.OnImageLoaderListener;
import ir.acharkit.android.util.Logger;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    12/7/17
 * Email:   alirezat775@gmail.com
 */
public class TestCarouselAdapter extends CarouselAdapter {

    private static final String TAG = TestCarouselAdapter.class.getSimpleName();
    private final Context context;
    private MyViewHolder vh;
    private ImageLoader.Builder imageLoader;


    public TestCarouselAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoader.Builder(context, null);
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
        final TestCarouselModel model = (TestCarouselModel) getItems().get(position);

        Logger.d(TAG, "ID : " + model.getId());
        Logger.d(TAG, "TITLE : " + model.getTitle());
        vh.id.setText("ID: " + model.getId());

        imageLoader.setPlaceHolder(R.mipmap.ic_launcher).setImageLoaderListener(new OnImageLoaderListener() {
            @Override
            public void onStart(ImageView imageView, String url) {
                Logger.d(TAG, "onStart:-- " + "imageView: " + imageView + "url: " + url);
            }

            @Override
            public void onCompleted(ImageView imageView, String url, Bitmap bitmap) {
                Logger.d(TAG, "onCompleted:-- " + "image: " + imageView.toString());
            }

            @Override
            public void onFailure(String reason) {
                Logger.d(TAG, "onFailure:-- " + reason);
            }
        }).load(((MyViewHolder) holder).imageView, model.getImageUri());
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
