package com.mahfuj.newsviews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahfuj.newsviews.JsonToJava.Article;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList<Article> articleArrayList;
    private Context context;
    WindowManager.LayoutParams lp;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Article> articleArrayList, WindowManager.LayoutParams lp) {
        this.mInflater = LayoutInflater.from(context);
        this.articleArrayList = articleArrayList;
        this.context = context;
        this.lp = lp;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_card_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.textViewSource.setText(articleArrayList.get(position).getSource().getName());
        String dtStart = articleArrayList.get(position).getPublishedAt();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try {
            Date date = format.parse(dtStart);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yy");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
            holder.textViewDate.setText(dateFormat1.format(date));
            holder.textViewTime.setText(dateFormat2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewTitle.setText(articleArrayList.get(position).getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.textViewTitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        holder.textViewAuthor.setText(articleArrayList.get(position).getAuthor());
        /*Picasso.with(context).load(articleArrayList.get(position).getUrlToImage())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imageViewSourceImage);*/
        Picasso.with(context).load(articleArrayList.get(position).getUrlToImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageViewSourceImage.setMaxHeight(40000);
                holder.imageViewSourceImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                holder.imageViewSourceImage.setImageBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
        /*Picasso.with(context)
                .load(articleArrayList.get(position).getUrlToImage())
                .centerInside()
                .placeholder(R.drawable.default_001)
                .error(R.drawable.default_001)
                .fit()
                .into(holder.imageViewSourceImage);
        holder.textViewAuthor.setText(articleArrayList.get(position).getAuthor());*/
        holder.textViewTitle.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getRootView().getContext());

                LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
                @SuppressLint("InflateParams")
                View convertView = inflater.inflate(R.layout.news_total, null);
                alertDialog.setView(convertView);
                alertDialog.setCancelable(true);

                TextView textViewSource = convertView.findViewById(R.id.textViewSource);
                TextView textViewTime = convertView.findViewById(R.id.textViewTime);
                TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
                final ImageView imageViewSourceImage = convertView.findViewById(R.id.imageViewSourceImage);
                TextView textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
                TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
                TextView textViewDescriptionMain = convertView.findViewById(R.id.textViewDescriptionMain);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    textViewSource.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    textViewTime.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    textViewTitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    textViewAuthor.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    textViewDescription.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    textViewDescriptionMain.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                }
                textViewSource.setText(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getSource().getName());

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
                try {
                    Date date = format.parse(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getPublishedAt());
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yy HH:mm");
                    textViewTime.setText(dateFormat1.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Picasso.with(context).load(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrlToImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageViewSourceImage.setMaxHeight(40000);
                        imageViewSourceImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageViewSourceImage.setImageBitmap(bitmap);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });


                textViewTitle.setText(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getTitle());
                textViewAuthor.setText(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getAuthor());
                textViewDescription.setText(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getDescription());
                if(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent()!=null && articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent().split("\\[")[0]!=null)
                {
                    textViewDescriptionMain.setText(String.format("%s%s", articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent().split("\\[")[0], articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrl()));
                }
                else if(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrl()!=null && articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent()!=null)
                {
                    textViewDescriptionMain.setText(String.format("%s%s", articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent(), articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrl()));
                }
                else if(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent()!=null)
                {
                    textViewDescriptionMain.setText(String.format("%s", articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getContent()));
                }
                else if(articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrl()!=null)
                {
                    textViewDescriptionMain.setText(String.format("%s", articleArrayList.get(Integer.parseInt(holder.textViewTitle.getTag().toString())).getUrl()));
                }


                Linkify.addLinks(textViewDescriptionMain, Linkify.WEB_URLS);


                AlertDialog dialogInside = alertDialog.create();
                Objects.requireNonNull(dialogInside.getWindow()).setAttributes(lp);
                dialogInside.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialogInside.show();



            }
        });


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


            itemView.setOnClickListener(this);
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