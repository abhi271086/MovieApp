package com.example.beetel.moviesApp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beetel.moviesApp.adapter.MoviesDetailsViewPagerAdapter;
import com.example.beetel.moviesApp.data.model.MovieResultListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import butterknife.ButterKnife;

/**
 * Created by beetel on 4/03/2016.
 */
public class DetailsFragmentActivity extends Fragment {
    ImageView imgPoster;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MovieResultListModel.ResultModel model;
    private boolean fromactivity=true;
    private boolean twoPane=false;
    public DetailsFragmentActivity(){}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar2 = (Toolbar) getActivity().findViewById(R.id.toolbar_movie_detail);
        imgPoster = (ImageView) getActivity().findViewById(R.id.toolbar_image_backdrop);
        if (getActivity().findViewById(R.id.multipan) != null) {
            twoPane = true;
            toolbar2.setTitle(model.getOriginalTitle());
        }
        if (!twoPane)

            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(model.getOriginalTitle());
            try {
                setViewpagerAndBackDrop();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

  private void setViewpagerAndBackDrop()
    {
        viewPager=(ViewPager)getActivity().findViewById(R.id.vp_movie_detail);
        tabLayout=(TabLayout)getActivity().findViewById(R.id.tabs);
        //FragmentManager fragmentManager=this.getFragmentManager();
        MoviesDetailsViewPagerAdapter pagerAdapter=new MoviesDetailsViewPagerAdapter(getActivity().getSupportFragmentManager(),model);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Glide.with(DetailsFragmentActivity.this)
                .load(MovieAPIUtility.URL_IMAGE_BACKDROP_BASE+model.getBackdropUrl())
                .error(R.drawable.poster)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPoster);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.movie_fragment_main,container,false);
        ButterKnife.bind(this,view);

        model=getActivity().getIntent().getParcelableExtra(MovieAPIUtility.EXTRA_RESULT_MODEL);

        if(model==null){
            model=this.getArguments().getParcelable(MovieAPIUtility.EXTRA_RESULT_MODEL);
            fromactivity=false;

        }
        return view;
    }
    }

