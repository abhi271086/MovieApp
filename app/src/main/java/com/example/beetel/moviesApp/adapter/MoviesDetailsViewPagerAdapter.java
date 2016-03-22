package com.example.beetel.moviesApp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.beetel.moviesApp.DetailsFragmentActivity;
import com.example.beetel.moviesApp.ReviewsFragment;
import com.example.beetel.moviesApp.TrailerFragment;
import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

/**
 * Created by beetel on 7/03/2016.
 */
public class MoviesDetailsViewPagerAdapter extends FragmentStatePagerAdapter {
    private MovieResultListModel.ResultModel resultModel;
    public MoviesDetailsViewPagerAdapter(FragmentManager fm,MovieResultListModel.ResultModel model)
    {
        super(fm);
        this.resultModel=model;
    }

    @Override
    public int getCount() {
        return MovieAPIUtility.TABS_MOVIE_DETAIL.length;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }
    @Override
    public Fragment getItem(int pos)
    {
        switch (pos)
        {
            case 0:
                DetailsFragmentActivity detailsFragmentActivity=new DetailsFragmentActivity();
                Bundle arg=new Bundle();
                arg.putParcelable(MovieAPIUtility.EXTRA_DETAIL_FRAGMENT, resultModel);
                detailsFragmentActivity.setArguments(arg);
                return detailsFragmentActivity;

            case 1:
                TrailerFragment trailerFragment=new TrailerFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable(MovieAPIUtility.EXTRA_TRAILER_FRAGMENT, resultModel);
                trailerFragment.setArguments(bundle);
                return trailerFragment;
            case 2:
                ReviewsFragment reviewsFragment=new ReviewsFragment();
                Bundle args=new Bundle();
                args.putParcelable(MovieAPIUtility.EXTRA_REVIEW_FRAGMENT, resultModel);
                reviewsFragment.setArguments(args);
                return reviewsFragment;
            default:
                return new Fragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position ){
        if (position>=0&& position<MovieAPIUtility.TABS_MOVIE_DETAIL.length)
        {
            return MovieAPIUtility.TABS_MOVIE_DETAIL[position];
        }
        return null;
    }
}
