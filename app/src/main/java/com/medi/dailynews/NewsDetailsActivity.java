package com.medi.dailynews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.color.utilities.ColorUtils;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.ActivityNewsDetailsBinding;
import com.medi.dailynews.db.viewmodels.SavedViewModels;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.models.Source;
import com.medi.dailynews.utils.Utils;

import java.util.List;



public class NewsDetailsActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {
    private String mUrl,mUrlToImage,mTitle,mDate,mSource,mAuthor, mDescription;
    private boolean isHideToolbarView = false;
    private SavedViewModels mFavoritesViewModel;
    private Article article;
    ActivityNewsDetailsBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.collapsingToolbar.setTitle("");


        binding.layoutAppbar.addOnOffsetChangedListener(this);



        Intent intent = getIntent();
        article = intent.getParcelableExtra("selected_article");
        article.setSource(new Source(intent.getStringExtra("source")));
        mSource = intent.getStringExtra("Source");
        mUrl = article.getUrl();
        mUrlToImage = article.getUrlToImage();
        mTitle = article.getTitle();
        mDate = article.getPublishedAt();
        mAuthor = article.getAuthor();
        mDescription = article.getDescription();

        RequestOptions requestOptions = new RequestOptions();


//        Glide.with(this)
//                .load(mUrlToImage)
//                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(binding.imageViewTitle);

        Glide.with(this)
                .load(mUrlToImage)
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
                        binding.CardLayout.setBackgroundColor(dominantColor);



                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageViewTitle);


       binding.titleOnLayoutTitleAppbar.setText(mSource);
        binding.subtitleOnLayoutTitleAppbar.setText(mUrl);
        binding.textViewTitleNews.setText(mTitle);
        binding.textViewSourceAuthorTime.setText(mSource + appendAuthorWithBullet(mAuthor));
        binding.textViewDatePublished.setText(Utils.DateFormat(mDate));

        initWebView(mUrl);
                mFavoritesViewModel = new ViewModelProvider(this).get(SavedViewModels.class);



    }

    private String appendAuthorWithBullet(String mAuthor) {

        String author;

        if (this.mAuthor != null) {
            author = " \u2022 " + this.mAuthor;
        } else {
            author = "";
        }

        return author;
    }

    private void initWebView(String url) {

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            binding.dateBehavior.setVisibility(View.GONE);
            binding.layoutTitleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            binding.dateBehavior.setVisibility(View.VISIBLE);
            binding.layoutTitleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.open_in_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            startActivity(intent);
            return true;

        } else if (id == R.id.save) {

            mFavoritesViewModel.insertArticle(article);
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.share) {

            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n" + mUrl + "\n" + "Via News App." + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Share with :"));

            } catch (Exception e) {
                Toast.makeText(this, "Oops! \nCannot share this news", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
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