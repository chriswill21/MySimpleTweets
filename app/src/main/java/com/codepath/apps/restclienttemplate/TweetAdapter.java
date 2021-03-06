package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
//public class TweetAdapter extends ListAdapter<Tweet, TweetAdapter.ViewHolder> {

    private final Activity activity;
    private List<Tweet> mTweets;

    // pass in the Tweets array into the constructor
    public TweetAdapter(Activity activity, List<Tweet> tweets) {
        this.activity = activity;
        mTweets = tweets;
    }
    Context context;
    // for each row, inflate
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(activity, tweetView);
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
        holder.tvDate.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tweet = tweet;


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

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        public final Activity activity;
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvDate;
        public Tweet tweet;
        private final int REQUEST_CODE = 21;


        public ViewHolder (Activity activity, View itemView) {
            super(itemView);
            this.activity = activity;

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(itemView.getContext(), TweetDetailsActivity.class);
            i.putExtra("UserName", tvUsername.getText().toString());
            i.putExtra("Body", tvBody.getText().toString());
            i.putExtra("Date", tvDate.getText().toString());
            i.putExtra("Tweet", Parcels.wrap(tweet));
            itemView.getContext().startActivity(i);
        }

        @Override
        public boolean onLongClick(View v) {
            // handle click here
            Intent i = new Intent(itemView.getContext(), ReplyActivity.class);
            i.putExtra("UserName", tvUsername.getText().toString());
            i.putExtra("Tweet", Parcels.wrap(tweet));
            activity.startActivityForResult(i, REQUEST_CODE);
            return true;
        }


    }


    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }


}
