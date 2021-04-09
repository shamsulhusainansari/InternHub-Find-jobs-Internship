package com.knoxtech.internhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.knoxtech.internhub.model.Data;
import com.knoxtech.internhub.model.PostViewHolder;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InternshipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InternshipFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirestorePagingAdapter<Data, PostViewHolder> mAdapter;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private Query mQuery;
    public InternshipFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InternshipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InternshipFragment newInstance(String param1, String param2) {
        InternshipFragment fragment = new InternshipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_internship, container, false);

        CollectionReference mPostsCollection = mFirestore.collection("Internship");

        mQuery = mPostsCollection.orderBy("priority", Query.Direction.DESCENDING);
        mRecyclerView= view.findViewById(R.id.Internship_recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.InternshiprefreshLayout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        setupAdapter();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });



        return view;
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
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(requireActivity(),FullActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", data.getType());
                        bundle.putString("doc", data.getDocuments());
                        intent.putExtras(bundle);
                        //ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation((Activity) requireContext(),sham);
                        startActivity(intent);
                    }
                });
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("InternshipFragment", Objects.requireNonNull(e.getMessage()));
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
                                requireContext(),
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}