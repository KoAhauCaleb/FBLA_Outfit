package com.caleb.fblaoutfit;

import com.caleb.fblaoutfit.LoginActivity;
import com.caleb.fblaoutfit.InitialActivity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.ListActivity;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class UserFeedActivity extends ListActivity {

    Long Userid = Twitter.getSessionManager().getActiveSession().getUserId();//get user Id from active session and save to variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FeedSetUp();//populate feed with content
    }

    public void FeedSetUp() {
        setContentView(R.layout.activity_user_feed);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);//find swipe layout so it can be accessed by class

        //using Userid variable search for users tweets and populate listview with content
        UserTimeline searchTimeline = new UserTimeline.Builder()
                .userId(Userid)//search input
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)//set listview where content will be placed
                .setViewStyle(R.style.tw__TweetLightStyle)//set content style to light
                .build();
        setListAdapter(adapter);//populate listview

        //refresh if content pulled down
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);//show that its refreshing
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                    }
                });
            }
        });
    }

    public void buttonOnClickPost(View v) {
        startActivity(new Intent(UserFeedActivity.this, PostActivity.class));//start post activity
    }

    public void buttonOnClickTimeline(View v) {
        startActivity(new Intent(UserFeedActivity.this, FeedActivity.class));//start Full Feed activity
    }

    public void buttonOnClickLogout(View v) {
        Twitter.getSessionManager().clearActiveSession();//logout by clearing session
        startActivity(new Intent(UserFeedActivity.this, InitialActivity.class));//restart app after logout
    }
    @Override
    public void onBackPressed() {
        //return home, used because if you press back oon this activity it takes you to a blank activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
