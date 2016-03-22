package com.example.beetel.moviesApp.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beetel.moviesApp.R;
import com.example.beetel.moviesApp.data.model.MovieReviewListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by beetel on 9/03/2016.
 */
public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.ReviewViewHolder> {
    private static final String LOG_TAG=RecyclerViewReviewAdapter.class.getSimpleName();
private Context mContext;
    private ArrayList<MovieReviewListModel.ReviewModel> mReviewList;

    public RecyclerViewReviewAdapter(ArrayList<MovieReviewListModel.ReviewModel> mReviewList, FragmentActivity mContext) {
        this.mReviewList=mReviewList;
       this.mContext=mContext;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_review_view_item,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
String author=mReviewList.get(position).getAuthor();
        String content=mReviewList.get(position).getContent();
        String link=mReviewList.get(position).getUrl();
        holder.tvAuthor.setText(author);
        holder.tvContent.setText(content);
        holder.tvLink.setText(link);
        Linkify.addLinks(holder.tvLink, Linkify.WEB_URLS);
    }

    @Override
    public int getItemCount() {
        if (mReviewList!=null)
            return mReviewList.size();
        else
            return 0;
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {  @Bind(R.id.textview_review_author)
    TextView tvAuthor;
        @Bind(R.id.textview_review_content)
        TextView tvContent;
        @Bind(R.id.textview_review_link)
        TextView tvLink;
        public ReviewViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
