package com.example.beetel.moviesApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.beetel.moviesApp.R;
import com.example.beetel.moviesApp.data.model.MovieVideoListModel;
import com.example.beetel.moviesApp.utilities.MovieAPIUtility;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by beetel on 9/03/2016.
 */
public class RecyclerViewVideoAdapter extends RecyclerView.Adapter<RecyclerViewVideoAdapter.VideoViewHolder> {

    private static final String LOG_TAG=RecyclerViewVideoAdapter.class.getSimpleName();
    private ArrayList<MovieVideoListModel.VideoModel> mVideoList;
    private Context mContext;

    public RecyclerViewVideoAdapter(ArrayList<MovieVideoListModel.VideoModel> mVideoList, Context mContext) {
        this.mVideoList = mVideoList;
        this.mContext = mContext;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_trailer_view_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        String url = MovieAPIUtility.UTUBE_THUMBNAIL_URL_BASE + mVideoList.get(position).getKey() + "/default.jpg";
        String title = mVideoList.get(position).getName();
        holder.tvTrailerTitle.setText(title);
        Glide.with(holder.imageViewThumbnails.getContext())
                .load(url)
                .error(R.drawable.poster)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewThumbnails);

    }

    @Override
    public int getItemCount() {
            if (mVideoList!=null)
                return mVideoList.size();
        else
        return 0;
        }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       @Bind(R.id.imageview_trailer_thumbnail)
       ImageView imageViewThumbnails;
        @Bind(R.id.textview_trailername)
        TextView tvTrailerTitle;

        public VideoViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
         String vld=mVideoList.get(getAdapterPosition()).getKey();
            String url= MovieAPIUtility.UTUBE_PLAYER_URL_BASE+vld;
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            Log.d(LOG_TAG, "Playing Video with Youtube URL Support" + url);
        }
    }
}



