package com.example.mycompanions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    String[] stringArray;
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context,
                            String[] stringArray) {
//                            String imges){
        this.context = context;
        this.stringArray = stringArray;
    }

    @Override
    public int getCount() {
        return stringArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout_detal_img, null);
//        ImageView imageView1 = view.findViewById(R.id.imageView_detail);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        ImageView imageView = new ImageView(context);
        progressBar.setVisibility(View.VISIBLE);
        if (stringArray == null) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.heart);
        }
        progressBar.setVisibility(View.INVISIBLE);
        Picasso.get()
//                .load(imges)
                .load(stringArray[position])
//                .centerInside()
//                .fit()
//                .placeholder(R.drawable.heart)
                .error(R.drawable.heart)
                .into(imageView);
        container.addView(imageView);
//        imageView.setImageResource((Integer) listOfImages.get(position));
//        ViewPager pager = (ViewPager)container;
//        pager.addView(view, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
