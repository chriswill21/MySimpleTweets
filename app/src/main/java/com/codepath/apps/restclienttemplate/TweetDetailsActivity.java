package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvDate;
    public Tweet tweet;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_details);

        tvUsername = (TextView) findViewById(R.id.tvTweetDetailsUserName);
        tvBody = (TextView) findViewById(R.id.tvTweetDetailsBody);
        tvDate = (TextView) findViewById(R.id.tvTweetDetailsDate);
        ivProfileImage = (ImageView) findViewById(R.id.ivTweetDetailsProfileImage);

        tvUsername.setText(getIntent().getStringExtra("UserName"));
        tvBody.setText(getIntent().getStringExtra("Body"));
        tvDate.setText(getIntent().getStringExtra("Date"));
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));


        getSupportActionBar().setTitle(String.format("%s's Tweet", tvUsername.getText().toString()));

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
                .into(ivProfileImage);
    }

}
