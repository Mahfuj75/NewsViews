package com.mahfuj.newsviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mahfuj.newsviews.JsonToJava.Article;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList<Article> articleArrayList;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Article> articleArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.articleArrayList = articleArrayList;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_card_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.textViewSource.setText(articleArrayList.get(position).getSource().getName());
        String dtStart = "2010-10-15T09:27:37Z";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try {
            Date date = format.parse(dtStart);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            holder.textViewDate.setText(dateFormat1.format(date));
            holder.textViewTime.setText(dateFormat2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewTitle.setText(articleArrayList.get(position).getTitle());
        holder.textViewAuthor.setText(articleArrayList.get(position).getAuthor());
        /*Picasso.with(context).load(articleArrayList.get(position).getUrlToImage())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imageViewSourceImage);*/
        Picasso.with(context).load(articleArrayList.get(position).getUrlToImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageViewSourceImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
        holder.textViewAuthor.setText(articleArrayList.get(position).getAuthor());


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewSource;
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewTitle;
        ImageView imageViewSourceImage;
        TextView textViewAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            textViewSource = itemView.findViewById(R.id.textViewSource);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewSourceImage = itemView.findViewById(R.id.imageViewSourceImage);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);


            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

/*    // convenience method for getting data at click position
    Article getItem(int id) {
        return articleArrayList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }*/

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }






}