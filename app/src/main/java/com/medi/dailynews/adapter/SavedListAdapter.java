package com.medi.dailynews.adapter;




import static com.medi.dailynews.adapter.News_List_Adapter.getDominantColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
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

import com.medi.dailynews.databinding.NewsItemListBinding;
import com.medi.dailynews.db.viewmodels.SavedViewModels;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.utils.Utils;

public class SavedListAdapter extends ListAdapter<Article, SavedListAdapter.ViewHolder> {

    private final Context mContext;
    private final SavedViewModels mFavoritesViewModel;

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

    public SavedListAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
        mFavoritesViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SavedViewModels.class);
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
        private final TextView textViewAuthor;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewSource;
        private final TextView textViewTime;
        private final TextView textViewPublishedAt;
        private final ProgressBar progressBarInImage;
       RelativeLayout relativeLayout;



        public ViewHolder(@NonNull  NewsItemListBinding binding) {
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
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @SuppressLint("ObsoleteSdkInt")
        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Article article = getItem(position);

            Intent intent = new Intent(mContext, NewsDetailsActivity.class);
            intent.putExtra("source", article.getSource().getName());
            intent.putExtra("selected_article", article);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation
                    ((Activity) mContext, titleImage, ViewCompat.getTransitionName(titleImage));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else {
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Add to Favorites"); //groupId, itemId, order, title
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = getAdapterPosition();
                    Article newsItem = getItem(position);
                    mFavoritesViewModel.insertArticle(newsItem);
                    Toast.makeText(mContext, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }
}
