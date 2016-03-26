package com.example.beetel.moviesApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beetel.moviesApp.adapter.RecyclerViewReviewAdapter;
import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.data.model.MovieReviewListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beetel on 8/03/2016.
 */
public class ReviewsFragment extends Fragment {
    private static final String LOG_TAG=ReviewsFragment.class.getSimpleName();
    private MovieAPI movieAPI;
    private ArrayList<MovieReviewListModel.ReviewModel>reviewList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewReviewAdapter recyclerViewReviewAdapter;
    private Subscription reviewSubscription;
    @Bind(R.id.recycler_view_movie_review)
    RecyclerView mRecyclerView;
    public ReviewsFragment(){

    }
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.movie_fragment_reviews,container,false);
        ButterKnife.bind(this, view);

        Bundle bundle=getArguments();
        MovieResultListModel.ResultModel resultModel=bundle.getParcelable(MovieAPIUtility.EXTRA_REVIEW_FRAGMENT);
        String id=null;
        if(resultModel!=null){
            id=resultModel.getMovieId();
        }
        linearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if(savedInstanceState!=null&& savedInstanceState.containsKey(MovieAPIUtility.EXTRA_REVIEW)){
            reviewList=savedInstanceState.getParcelableArrayList(MovieAPIUtility.EXTRA_REVIEW);
            recyclerViewReviewAdapter=new RecyclerViewReviewAdapter(reviewList,getActivity());
            recyclerViewReviewAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(recyclerViewReviewAdapter);
    }
    callReview(id);
    return view;
}
private void callReview(String id) {
    movieAPI = RServiceGenerator.createService(MovieAPI.class);
    reviewSubscription = movieAPI.getMovieReviewRx(id, getString(R.string.api_key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(new Observer<MovieReviewListModel>() {
                @Override
                public void onCompleted() {
                    recyclerViewReviewAdapter = new RecyclerViewReviewAdapter(reviewList, getActivity());
                    recyclerViewReviewAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(recyclerViewReviewAdapter);
                    Log.d(LOG_TAG, "Completed loading movie videos");

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(MovieReviewListModel movieReviewListModel) {
                    reviewList = new ArrayList<>();
                    for (int i = 0; i < movieReviewListModel.getResults().size(); i++) {
                        reviewList.add(movieReviewListModel.getResults().get(i));
                        Log.d(LOG_TAG, movieReviewListModel.getResults().get(i).getContent());
                    }
                }
            });
}

@Override
public void onPause(){
        super.onPause();
         reviewSubscription.unsubscribe();

        }
@Override
public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MovieAPIUtility.EXTRA_REVIEW,reviewList);
        }
        }