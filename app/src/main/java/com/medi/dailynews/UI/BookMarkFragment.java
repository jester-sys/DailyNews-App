package com.medi.dailynews.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.medi.dailynews.adapter.SavedListAdapter;
import com.medi.dailynews.databinding.FragmentBookMarkBinding;
import com.medi.dailynews.db.viewmodels.SavedViewModels;
import com.medi.dailynews.models.Article;

import java.util.List;



public class BookMarkFragment extends Fragment {
    private SavedListAdapter adapter;
    private Context mContext;

    private SavedViewModels savedViewModels;
    FragmentBookMarkBinding binding;

    public BookMarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Favorites");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentBookMarkBinding.inflate(inflater,container,false);
//        View rootView = inflater.inflate(R.layout.fragment_book_mark, container, false);

        mContext = getActivity();
        adapter = new SavedListAdapter(mContext);
        initEmptyRecyclerView();
        initAdapterGestures();

        savedViewModels = new ViewModelProvider(this).get(SavedViewModels.class);
        subscribeObservers();

        return binding.getRoot();
    }

    private void subscribeObservers() {
        savedViewModels.getAllSavedArticles().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> savedArticles) {

                initEmptyRecyclerView();

                if (!savedArticles.isEmpty()) {
                    adapter.submitList(savedArticles);
                    binding.emptyView.setVisibility(View.INVISIBLE);
                    binding.textViewTopHeadlines.setVisibility(View.VISIBLE);
                }

                if (savedArticles.isEmpty()) {
                    adapter.submitList(savedArticles);
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.emptyView.setText("No Articles Found!");
                    binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initEmptyRecyclerView() {
        adapter = new SavedListAdapter(mContext);
        binding.recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (mContext, LinearLayoutManager.VERTICAL, false);

        binding.recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initAdapterGestures() {
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Article newsItem = adapter.getArticleAt(position);
                        savedViewModels.deleteArticle(newsItem);
                        Toast.makeText(mContext, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                });

        helper.attachToRecyclerView(binding.recyclerView);
    }
}