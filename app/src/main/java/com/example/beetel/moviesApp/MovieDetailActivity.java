package com.example.beetel.moviesApp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.beetel.moviesApp.data.model.MovieResultListModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by beetel on 7/03/2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Bind(R.id.vp_movie_detail)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    private boolean twoPane=false;

    MovieResultListModel.ResultModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviesdetail_layout);
        ButterKnife.bind(this);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);
    }
}
