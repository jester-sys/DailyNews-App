package com.medi.dailynews.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.medi.dailynews.NewsDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.color.utilities.ColorUtils;
import com.medi.dailynews.databinding.SearchNewsListBinding;
import com.medi.dailynews.db.viewmodels.SavedViewModels;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.utils.Utils;

public class SearchListAdapter extends PagedListAdapter<Article, SearchListAdapter.ViewHolder> {

    private final Context mContext;
    private final SavedViewModels savedViewModels;

    private static final DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(Article oldItem, Article newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(Article oldItem, @NonNull Article article) {
            return oldItem.equals(article);
        }
    };


    public SearchListAdapter (Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
        savedViewModels = new ViewModelProvider((ViewModelStoreOwner) context).get(SavedViewModels.class);
    }




    public Article getArticleAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SearchNewsListBinding.inflate(LayoutInflater.from(mContext), parent, false));

    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Article newsItem = getItem(position);

        RequestOptions requestOptions = new RequestOptions();

        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        assert newsItem != null;
        Glide.with(mContext)
                .load(newsItem.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        int dominantColor = getDominantColor(bitmap);

                        // Set the background color
                        holder.cardView.setBackgroundColor(dominantColor);

                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.titleImage);



        holder.textViewTitle.setText(newsItem.getTitle());
        holder.textViewSource.setText(newsItem.getSource().getName());
        holder.textViewTime.setText(" \u2022 " + Utils.DateToTimeFormat(newsItem.getPublishedAt()));
      ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ...

        private ImageView titleImage;
        private final TextView textViewTitle;
        private final TextView textViewSource;
        private final TextView textViewTime;
        private CardView cardView;

        public ViewHolder(SearchNewsListBinding binding) {
            super(binding.getRoot());
            this.textViewTitle = binding.textViewNewsTitle;
            this.textViewSource = binding.textViewSource;
            this.textViewTime = binding.textViewTime;
            this.titleImage = binding.titleImage;
            this.cardView = binding.mainLayout;

            itemView.setOnClickListener(this);
        }

        @SuppressLint("ObsoleteSdkInt")
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Article article = getItem(position);

            Intent intent = new Intent(mContext, NewsDetailsActivity.class);
            assert article != null;
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("selected_article", article);

            // Set the transition name for the titleImage ImageView
            ViewCompat.setTransitionName(titleImage, "titleImage_" + position);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) mContext, titleImage, ViewCompat.getTransitionName(titleImage));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        }
    }
        // Utility method to get the dominant color from a bitmap
        @SuppressLint("RestrictedApi")
        private int getDominantColor(Bitmap bitmap) {
            int defaultColor = 0xFF000000; // Black as default color

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int pixelCount = width * height;
            int[] pixels = new int[pixelCount];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            if (pixelCount > 0) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                for (int pixel : pixels) {
                    sumRed += ColorUtils.redFromArgb(pixel);
                    sumGreen += ColorUtils.greenFromArgb(pixel);
                    sumBlue += ColorUtils.blueFromArgb(pixel);
                }

                int averageRed = sumRed / pixelCount;
                int averageGreen = sumGreen / pixelCount;
                int averageBlue = sumBlue / pixelCount;

                return Color.rgb(averageRed, averageGreen, averageBlue);
            }

            return defaultColor;
        }

    }

