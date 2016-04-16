package com.example.beetel.moviesApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.beetel.moviesApp.adapter.RecyclerViewMoviesAdapter;
import com.example.beetel.moviesApp.data.model.MovieContract;
import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beetel on 8/03/2016.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.recycler_view_movie)
    RecyclerView mRecyclerView;
    private static final String TAG=MainActivityFragment.class.getSimpleName();
private ArrayList<MovieResultListModel.ResultModel>mPosterList;
private GridLayoutManager mlayoutManager;
private RecyclerViewMoviesAdapter moviesAdapter;
private MovieAPI movieAPI;
private Subscription movieSubscription;
private ActionBar mActionBar;
private int Orientation;
private RecyclerViewMoviesAdapter.MovieOnClickHandler movieOnClickHandler;
private boolean mutiPane=false;
private SharedPreferences sharedPreferences;
private SharedPreferences.Editor editor;
private int choice;
private MenuInflater menuInflater;


private StaggeredGridLayoutManager staggeredGridLayoutManager;

public MainActivityFragment(){}
@Override
public void onActivityCreated(Bundle savedInstanceState)
{
super.onActivityCreated(savedInstanceState);
setHasOptionsMenu(true);
getActivity().supportInvalidateOptionsMenu();
mActionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
if (getActivity().findViewById(R.id.multipan)!=null) {
    mutiPane = true;
    Toast.makeText(getActivity(), "TwoPane", Toast.LENGTH_SHORT).show();
}
    Log.d(TAG, "Choice on Activity=" + choice);
    switch (choice){

    case 0:
    mActionBar.setTitle(getString(R.string.app_name));
    break;
    case 1:
    mActionBar.setTitle(getString(R.string.top_rated_movies));
    break;
    case 2:
    mActionBar.setTitle(getString(R.string.fav));
    break;
}
}
@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.moviefragment_main, container, false);
    ButterKnife.bind(this, view);
    Orientation = getActivity().getResources().getConfiguration().orientation;
    sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();

    choice = sharedPreferences.getInt(MovieAPIUtility.PREF_CHOICE, 0);

    movieOnClickHandler = new RecyclerViewMoviesAdapter.MovieOnClickHandler() {
        @Override
        public void onClick(RecyclerViewMoviesAdapter.MoviesViewHolder moviesViewHolder, MovieResultListModel.ResultModel model, RecyclerViewMoviesAdapter.MoviesViewHolder holder) {
            if (!mutiPane) {
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra(MovieAPIUtility.EXTRA_RESULT_MODEL, model);
               ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
                ActivityCompat.startActivity(getActivity(), intent, activityOptionsCompat.toBundle());
            } else {
                Fragment fragment = new DetailsFragmentActivity();
                Bundle arg = new Bundle();
                arg.putParcelable(MovieAPIUtility.EXTRA_RESULT_MODEL, model);
                fragment.setArguments(arg);
               getActivity() .getSupportFragmentManager().beginTransaction().replace(R.id.frame_movie_detail, fragment).commit();
            }
        }

    };
    if (Orientation == Configuration.ORIENTATION_PORTRAIT) {
        mlayoutManager = new GridLayoutManager(getActivity(), 2);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
    } else {
        mlayoutManager = new GridLayoutManager(getActivity(), 3);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);

    }
    //Setup layout manager to recycler view
    mRecyclerView.setLayoutManager(mlayoutManager);
    //Restore saved instance
    if (savedInstanceState != null && savedInstanceState.containsKey(MovieAPIUtility.EXTRA_MOVIE)) {
        mPosterList = savedInstanceState.getParcelableArrayList(MovieAPIUtility.EXTRA_MOVIE);
        moviesAdapter = new RecyclerViewMoviesAdapter(getActivity(), mPosterList, movieOnClickHandler);

        mRecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.notifyDataSetChanged();
    }
    //create REST service endpoint
    movieAPI = RServiceGenerator.createService(MovieAPI.class);
    Log.d(TAG, "Choice" + choice);
    //Call MovieAPI
    switch (choice) {
        case 0:
            callMovieApi(MovieAPIUtility.SORT_POPULAR_DESC);
            break;
        case 1:
            callMovieApi(MovieAPIUtility.SORT_RATING_DESC);
            break;
        case 2:
            showfav();
            break;
    }
    return view;
}
    private void showfav() {
        Log.d(TAG, "Enter");
        Cursor cursor = getContext().getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);

        Log.d(TAG, String.valueOf(cursor.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_TITLE)));
        mPosterList = new ArrayList<>();
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                MovieResultListModel.ResultModel resultModel = new MovieResultListModel.ResultModel();

                resultModel.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TITLE)));
                resultModel.setPosterUrl(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_URL)));
                resultModel.setBackdropUrl(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_BACK_DROP_URL)));
                resultModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_TITLE)));
                resultModel.setPlot(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_PLOT)));
                resultModel.setRating(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RATING)));
                resultModel.setReleasedate(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RELEASE_DATE)));
                resultModel.setMovieId(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_MOVIE_ID)));
                mPosterList.add(resultModel);
                moviesAdapter = new RecyclerViewMoviesAdapter(getActivity(), mPosterList, movieOnClickHandler);
                mRecyclerView.setAdapter(moviesAdapter);
                moviesAdapter.notifyDataSetChanged();
            }
        }
    }
    private void callMovieApi(String sort){
        movieSubscription=movieAPI.IodeMoviesRx(sort,getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieResultListModel>() {
                    @Override
                    public void onCompleted() {
                        moviesAdapter=new RecyclerViewMoviesAdapter(getActivity(),mPosterList,movieOnClickHandler);

                        mRecyclerView.setAdapter(moviesAdapter);
                        moviesAdapter.notifyDataSetChanged();
                        Log.d(TAG,"Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                     e.printStackTrace();
                    }

                    @Override
                    public void onNext(MovieResultListModel movieResultListModel) {
                      mPosterList=new ArrayList<>();
                        Toast.makeText(getActivity(),"Loading.......",Toast.LENGTH_SHORT).show();
                        for (int i=0;i<movieResultListModel.getResults().size();i++){
                            mPosterList.add(movieResultListModel.getResults().get(i));
                            Log.d(TAG,MovieAPIUtility.URL_IMAGE_BASE+movieResultListModel.getResults().get(i).getPosterUrl());
                        }
                    }
                });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        menuInflater=inflater;
        menu.clear();
        inflater.inflate(R.menu.fragment_menu_main,menu);

}
    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if(!mutiPane){
            menu.clear();
            menuInflater.inflate(R.menu.fragment_menu_main,menu);
            switch (choice)
            {
                case 0:
                    mActionBar.setTitle(getString(R.string.app_name));
                    break;
                case 1:
                    mActionBar.setTitle(getString(R.string.top_rated_movies));
                    break;
                case 2:
                    mActionBar.setTitle(getString(R.string.fav));
                    break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_sort_popular:
                callMovieApi(MovieAPIUtility.SORT_POPULAR_DESC);
                mActionBar.setTitle(getString(R.string.app_name));
                editor.putInt(MovieAPIUtility.PREF_CHOICE, 0);
                editor.commit();
                break;
            case R.id.action_sort_rated:
                callMovieApi(MovieAPIUtility.SORT_RATING_DESC);
                mActionBar.setTitle(getString(R.string.top_rated_movies));
                editor.putInt(MovieAPIUtility.PREF_CHOICE, 1);
                editor.commit();
                break;
            case R.id.action_fav:
                showfav();
                mActionBar.setTitle(getString(R.string.fav));
                editor.putInt(MovieAPIUtility.PREF_CHOICE, 2);
                editor.commit();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onPause(){
        super.onPause();
      if(movieSubscription!=null)
          movieSubscription.unsubscribe();


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MovieAPIUtility.EXTRA_MOVIE,mPosterList);
    }

}