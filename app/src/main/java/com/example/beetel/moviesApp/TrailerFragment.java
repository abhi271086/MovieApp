package com.example.beetel.moviesApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.beetel.moviesApp.adapter.RecyclerViewVideoAdapter;
import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.data.model.MovieVideoListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beetel on 7/03/2016.
 */
public class TrailerFragment extends Fragment {
    private static final String LOG_TAG=TrailerFragment.class.getSimpleName();
    private MovieAPI movieAPI;
    private ArrayList<MovieVideoListModel.VideoModel> mVideoList=null;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewVideoAdapter videoAdapter;
    private MovieResultListModel.ResultModel resultModel;
    private ShareActionProvider shareActionProvider=null;
    private Subscription videoSubscription;
    @Bind(R.id.recycler_view_movie_trailer)
    RecyclerView mRecyclerView;

    public TrailerFragment(){}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.movie_fragment_trailer,container,false);
        ButterKnife.bind(this, view);
        Bundle bundle=getArguments();
        MovieResultListModel.ResultModel resultModel=bundle.getParcelable(MovieAPIUtility.EXTRA_TRAILER_FRAGMENT);
        getActivity().supportInvalidateOptionsMenu();
        String id=null;
        if(resultModel!=null)
        {
            id=resultModel.getMovieId();
        }
        linearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if(savedInstanceState!=null && savedInstanceState.containsKey(MovieAPIUtility.EXTRA_VIDEO)){
            mVideoList=savedInstanceState.getParcelableArrayList(MovieAPIUtility.EXTRA_VIDEO);
            videoAdapter=new RecyclerViewVideoAdapter(mVideoList,getActivity());

            mRecyclerView.setAdapter(videoAdapter);
            videoAdapter.notifyDataSetChanged();
        }
        callTrailer(id);
        return view;
    }
    private void callTrailer(String id) {
        movieAPI = RServiceGenerator.createService(MovieAPI.class);
        videoSubscription = movieAPI.getMovieTrailerRx(id, getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieVideoListModel>() {
                    @Override
                    public void onCompleted() {
                        videoAdapter = new RecyclerViewVideoAdapter(mVideoList, getActivity());

                        mRecyclerView.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                        Log.d(LOG_TAG, "Completed loading movie videos");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MovieVideoListModel movieVideoListModel) {
                        mVideoList = new ArrayList<>();
                        for (int i = 0; i < movieVideoListModel.getResults().size(); i++) {
                            mVideoList.add(movieVideoListModel.getResults().get(i));
                            Log.d(LOG_TAG, MovieAPIUtility.UTUBE_THUMBNAIL_URL_BASE + movieVideoListModel.getResults().get(i).getKey() + "/default.jpg");
                            mVideoList = new ArrayList<>();
                        }
                    }
                });

    }

@Override

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
    inflater.inflate(R.menu.menu_trailer_fragment, menu);
    MenuItem menuItem=menu.findItem(R.id.action_share_trailer);
    String url="No video found";

    shareActionProvider= (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
    Intent intent=new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    try {
        url=MovieAPIUtility.UTUBE_PLAYER_URL_BASE+mVideoList.get(0).getKey();
    }
    catch (IndexOutOfBoundsException|NullPointerException e){
        e.printStackTrace();
}
   intent.putExtra(Intent.EXTRA_TEXT,url);
    shareActionProvider.setShareIntent(intent);
}

@Override
public void onSaveInstanceState(Bundle outState)
{
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(MovieAPIUtility.EXTRA_VIDEO,mVideoList);
}
}