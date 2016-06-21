package com.caleb.fblaoutfit;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.content.Intent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.CollectionTimeline;




public class FeedActivity extends ListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FeedSetUp();//start feed setup activity
    }

    public void FeedSetUp() {
        setContentView(R.layout.activity_feed);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);//find swipe layout so it can be accessed by class

        //search for tweets in collection
        CollectionTimeline userTimeline = new CollectionTimeline.Builder()
                .id(724059920992096257L)//search input
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)//set listview where content will be placed
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)//set content style to light and actions such as like and retweet
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
                        // do nothing on failure
                    }
                });
            }
        });
    }

    public void buttonOnClickPost(View v) {
        startActivity(new Intent(FeedActivity.this, PostActivity.class));//start post activity
    }
    public void buttonOnClickTimeline(View v) {
        startActivity(new Intent(FeedActivity.this, UserFeedActivity.class));//start user feed activity
    }
    public void buttonOnClickLogout(View v) {
        Twitter.getSessionManager().clearActiveSession();//logout by clearing session
        startActivity(new Intent(FeedActivity.this, InitialActivity.class));//restart app after logout
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
