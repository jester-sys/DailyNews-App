
package com.medi.dailynews.adapter;//package com.medi.dailynews.AdapterClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;


import androidx.annotation.Nullable;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


import com.google.android.material.color.utilities.ColorUtils;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.NewsItemListBinding;
import com.medi.dailynews.db.viewmodels.SavedViewModels;
import com.medi.dailynews.utils.Utils;
import com.medi.dailynews.models.Article;


public class News_List_Adapter extends PagedListAdapter<Article, News_List_Adapter.ViewHolder> {

    private final Context mContext;
    private final SavedViewModels savedViewModels;
    private Boolean isFavourite = false;

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


    public News_List_Adapter(Context context) {
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
        return new ViewHolder(NewsItemListBinding.inflate(LayoutInflater.from(mContext), parent, false));

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
                        holder.progressBarInImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBarInImage.setVisibility(View.GONE);

                        if (resource instanceof BitmapDrawable) {
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            int dominantColor = getDominantColor(bitmap);

                            // Set the background color
                            holder.relativeLayout.setBackgroundColor(dominantColor);
                        } else if (resource instanceof GifDrawable) {
                            // Handle GifDrawable here if needed
                        }

                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.titleImage);

        holder.ShareBtn.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Article article = getItem(adapterPosition);
                if (article != null) {
                    shareArticle(article);
                }
            }
        });
        holder.BookMarkBtn.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Article article = getItem(adapterPosition);
                if (isFavourite && article != null) {
                    isFavourite = false;
                    holder.BookMarkBtn.setImageResource(R.drawable.book_mark_red);
                    savedViewModels.insertArticle(article);
                    Toast.makeText(mContext, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else if (!isFavourite && article != null) {
                    isFavourite = true;
                    holder.BookMarkBtn.setImageResource(R.drawable.baseline_bookmark_24);
                    savedViewModels.deleteArticle(article);
                    Toast.makeText(mContext, "Article Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.textViewTitle.setText(newsItem.getTitle());
        holder.textViewDescription.setText(newsItem.getDescription());
        holder.textViewSource.setText(newsItem.getSource().getName());
        holder.textViewTime.setText(" \u2022 " + Utils.DateToTimeFormat(newsItem.getPublishedAt()));
        holder.textViewPublishedAt.setText(Utils.DateFormat(newsItem.getPublishedAt()));
        holder.textViewAuthor.setText(newsItem.getAuthor());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        private final ImageView titleImage;
        private ImageView ShareBtn;
        private ImageView BookMarkBtn;
        private final TextView textViewAuthor;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewSource;
        private final TextView textViewTime;
        private final TextView textViewPublishedAt;
        private final ProgressBar progressBarInImage;
        RelativeLayout relativeLayout;
        CardView cardView;

        public ViewHolder(@NonNull NewsItemListBinding binding) {
            super(binding.getRoot());
            this.titleImage = binding.imageViewTitle;
            this.textViewAuthor = binding.textViewSourceAuthorTime;
            this.textViewTitle = binding.textViewNewsTitle;
            this.textViewSource = binding.textViewSource;
            this.textViewTime = binding.textViewTime;
            this.textViewPublishedAt = binding.textViewPublishedAt;
            this.textViewDescription = binding.textViewNewsDescription;
            this.progressBarInImage = binding.progressBarImage;
            this.relativeLayout = binding.UiBackground;
            this.cardView = binding.root;
            this.ShareBtn = binding.ShareBtn;
            this.BookMarkBtn = binding.BookMarkBtn;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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
            intent.putExtra("Source",article.getSource().getName());

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) mContext, titleImage, ViewCompat.getTransitionName(titleImage));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Add to Favorites"); //groupId, itemId, order, title
            MenuItem addToFavoritesMenuItem = menu.getItem(0);
            addToFavoritesMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = getAdapterPosition();
                    Article newsItem = getItem(position);
                    savedViewModels.insertArticle(newsItem);
                    Toast.makeText(mContext, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            menu.add(0, v.getId(), 1, "Share"); //groupId, itemId, order, title
            MenuItem shareMenuItem = menu.getItem(1);
            shareMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = getAdapterPosition();
                    Article newsItem = getItem(position);
                    if (newsItem != null) {
                        shareArticle(newsItem);
                    }
                    return true;
                }
            });
        }
    }

    // Utility method to get the dominant color from a bitmap
    @SuppressLint("RestrictedApi")
    public static int getDominantColor(Bitmap bitmap) {
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

    private void shareArticle(Article article) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, article.getSource().getName());
        String body = article.getTitle() + "\n" + article.getUrl() + "\n" + "Via News App." + "\n";
        intent.putExtra(Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(intent, "Share with:"));
    }

}
