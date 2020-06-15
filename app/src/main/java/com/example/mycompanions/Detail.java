package com.example.mycompanions;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {
    public ArrayList imgs;
    LinearLayout sliderDotPanel;
    String[] images = {};
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_scroll);
        String name = getIntent().getExtras().getString("name");
        String gender = getIntent().getExtras().getString("gender");
        String breed = getIntent().getExtras().getString("breeds");
        images = getIntent().getExtras().getStringArray("mediumArray");
//        imgs = getIntent().getExtras().getStringArrayList("small");
        String IMG = getIntent().getExtras().getString("medium");
        String description = getIntent().getExtras().getString("description");
        String organization_name = getIntent().getExtras().getString("organization_name");
        String email = getIntent().getExtras().getString("email");
        String phone = getIntent().getExtras().getString("phone");
        String website = getIntent().getExtras().getString("website");
        String address = getIntent().getExtras().getString("address");
        String spayed_neutered = getIntent().getExtras().getString("spayed_neutered");
        Log.i("Returning", spayed_neutered);
        TextView tv_name = findViewById(R.id.textView_detail_name);
        TextView tv_gender = findViewById(R.id.textView_detail_gender);
        TextView tv_breed = findViewById(R.id.textView_breeds);
        TextView tv_neutured_result = findViewById(R.id.netured_result);
        TextView tv_description = findViewById(R.id.tv_comment_detail);
        TextView tv_organization_name = findViewById(R.id.tv_organization_name);
        TextView tv_email = findViewById(R.id.tv_organization_email);
        TextView tv_phone = findViewById(R.id.tv_organization_phone_number);
        TextView tv_website = findViewById(R.id.tv_organization_website);
        TextView tv_address = findViewById(R.id.tv_organization_address);
        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_breed.setText(breed);
        tv_description.setText(description);
        tv_organization_name.setText(organization_name);
        tv_email.setText(email);
        tv_phone.setText(phone);
        tv_website.setText(website);
        tv_address.setText(address);
        tv_neutured_result.setText(spayed_neutered);
        ViewPager viewPager = findViewById(R.id.viewPager);
        sliderDotPanel = findViewById(R.id.sliderDots);
        if (images != null) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, images);
            viewPager.setAdapter(viewPagerAdapter);
            dotsCount = viewPagerAdapter.getCount();
            dots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.non_active_dot));
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                sliderDotPanel.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.active_dot));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(
                                getApplicationContext(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }
}
