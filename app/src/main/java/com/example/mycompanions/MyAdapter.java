package com.example.mycompanions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    private List<Friend> petlist;
    private Context context;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<Friend> petlist) {
        this.context = context;
        this.petlist = petlist;
    }

//    private static String convert(Object[] o, String de) {
//        StringBuilder sb = new StringBuilder();
//        for (Object onj : o)
//            sb.append(onj.toString()).append(de);
//        return sb.substring(0, sb.length() - 1);
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.recyclerview_item,
                parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Friend pets = petlist.get(position);
        String url = pets.getSmall();
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        //setting Glide.  pets.getSmall()
        Glide.get(context).clearMemory();
        Glide.with(context).load(url).apply(requestOptions)
                .thumbnail(0.5f).circleCrop()
                .placeholder(R.mipmap.noimagemaru_round)
                .error(R.mipmap.noimagemaru_round)
                .into(((ItemHolder) holder).imageView);
//        GlideApp.with(context).
//                load(pets.getSmall()).
//                placeholder(R.drawable.no_image).
//                into(((ItemHolder) holder).imageView);
        //setting on Picasso
//        Picasso.get().load(pets.getSmall()).placeholder(R.drawable.no_image)
//                .error(R.drawable.no_image).into(((ItemHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return petlist.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ItemHolder(View itemview) {
            super(itemview);
            imageView = itemview.findViewById(R.id.image_recycler);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Detail.class);
                      i.putExtra("id", petlist.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }
    }
}