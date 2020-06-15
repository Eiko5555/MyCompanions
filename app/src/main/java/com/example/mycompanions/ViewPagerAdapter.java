package com.example.mycompanions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    String[] imges;
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context,
                            String[] imges) {
//                            String imges){
        this.context = context;
        this.imges = imges;
    }

    @Override
    public int getCount() {
        return imges.length;
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
        ImageView imageView = new ImageView(context);
        if (imges == null) {
            imageView.setImageResource(R.drawable.no_image);
        }
        Picasso.get()
//                .load(imges)
                .load(imges[position])
//                .centerInside()
//                .fit()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
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
