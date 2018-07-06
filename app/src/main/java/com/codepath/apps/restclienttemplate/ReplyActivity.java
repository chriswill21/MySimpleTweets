package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.wafflecopter.charcounttextview.CharCountTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ReplyActivity extends AppCompatActivity {


    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(this);
        setContentView(R.layout.activity_reply);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        final EditText tweetResponse = (EditText) findViewById(R.id.etReplyTweet);
        final TextView charCount = (TextView) findViewById(R.id.etReplyTweet);
        final Button btPostTweet = (Button) findViewById(R.id.btReplyPostTweet) ;

        Tweet tweetToRespondTo = Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));
        tweetResponse.append("@" + tweetToRespondTo.user.screenName + " ");



        CharCountTextView tvCharCount = (CharCountTextView) findViewById(R.id.tvReplyTextCounter);
        EditText simpleEditText = (EditText) findViewById(R.id.etReplyTweet);

        tvCharCount.setEditText(simpleEditText);
        tvCharCount.setMaxCharacters(140); //Will default to 150 anyway (Twitter emulation)
        tvCharCount.setExceededTextColor(Color.RED); //Will default to red also
        tvCharCount.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int countRemaining, boolean hasExceededLimit) {
                if (hasExceededLimit) {
                    btPostTweet.setEnabled(false);
                } else {
                    btPostTweet.setEnabled(true);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void composeReplyTweet (View view) {
        EditText tweetResponse = (EditText) findViewById(R.id.etReplyTweet);


        client.sendTweet(tweetResponse.getText().toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Twitter client", response.toString());
                try {
                    Tweet newTweet = new Tweet().fromJSON(response);

                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("User", Parcels.wrap(newTweet.user));
                    data.putExtra("Tweet", Parcels.wrap(newTweet));

                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish(); // closes the activity, pass data to parent

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Twitter client", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter client", responseString);
                throwable.printStackTrace();
            }
        });

    }
}
