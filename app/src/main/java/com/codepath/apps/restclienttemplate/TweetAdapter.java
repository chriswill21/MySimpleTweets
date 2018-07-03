package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    // pass in the Tweets array into the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    Context context;
    // for each row, inflate
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }
    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // get the data according to the position
        Tweet tweet = mTweets.get(position);

        // populate the view according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);


        int round_radius = context.getResources().getInteger(R.integer.radius);
        int round_margin = context.getResources().getInteger(R.integer.margin);

        final RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(round_radius, round_margin);

        final RequestOptions requestOptions = RequestOptions.bitmapTransform(
                roundedCornersTransformation
        );


        // get the correct place holder and image view for the current orientation
//        int placeholderId = R.drawable.flicks_movie_placeholder;
//        ImageView imageView = holder.ivPosterImage;

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(requestOptions)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;

        public ViewHolder (View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);


        }

    }
}
