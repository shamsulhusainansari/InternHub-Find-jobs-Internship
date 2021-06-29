package com.knoxtech.internhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.knoxtech.internhub.model.Data;
import com.knoxtech.internhub.model.PostViewHolder;

import java.util.Objects;

public class SortActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirestorePagingAdapter<Data, PostViewHolder> mAdapter;
    private final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String collection1 = bundle.getString("collection");
        String selection1=bundle.getString("selection");
        String equalto1=bundle.getString("equalto");

        assert collection1 != null;
        CollectionReference mPostsCollection = mFirestore.collection(collection1);

        assert selection1 != null;
        mQuery = mPostsCollection.orderBy("priority", Query.Direction.DESCENDING).whereEqualTo(selection1,equalto1);

        mRecyclerView = findViewById(R.id.sort_recycler_view);

        mSwipeRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        mSwipeRefreshLayout.setOnRefreshListener(() -> mAdapter.refresh());

    }

    private void setupAdapter() {


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        FirestorePagingOptions options = new FirestorePagingOptions.Builder<Data>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Data.class)
                .build();

        mAdapter = new FirestorePagingAdapter<Data, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.card_layout, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder viewHolder, int i, @NonNull final Data data) {
                viewHolder.bind(data);
                viewHolder.itemView.setOnClickListener(v -> {
                    Intent intent=new Intent(SortActivity.this,FullActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", data.getType());
                    bundle.putString("doc", data.getDocuments());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("SortActivity", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                        Log.d("PAGINATION: ","Loading Initial Data!");
                    case LOADING_MORE:

                        mSwipeRefreshLayout.setRefreshing(true);
                        Log.d("PAGINATION: ","Loading More!");
                        break;

                    case LOADED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("PAGINATION: ","Loaded");
                        break;

                    case ERROR:
                        Log.d("PAGINATION: ","Error!");
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("PAGINATION: ","All Item Loaded: "+getItemCount());
                        break;
                }
            }

        };

        mRecyclerView.setAdapter(mAdapter);

    }
}