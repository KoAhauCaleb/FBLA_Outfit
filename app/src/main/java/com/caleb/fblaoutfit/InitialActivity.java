package com.caleb.fblaoutfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.twitter.sdk.android.Twitter;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class InitialActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "IuMOf7d5A7FHus9XvOhgiFIkf";
    private static final String TWITTER_SECRET = "mJZ4PX5v4uTWjOYiLqlMx3cgwZZLmotWJLTdRcLN4Y9WD182rL";
    static String User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Answers(), new Crashlytics(), new Twitter(authConfig));
        Appsee.start(getString(R.string.com_appsee_apikey));

        final Session activeSession;
        activeSession =
                AutoLogin.recordInitialSessionState(Twitter.getSessionManager().getActiveSession());

        if (activeSession != null) {
            startFeedActivity();
        } else {
            startLoginActivity();
        }
    }

    private void startFeedActivity() {

        startActivity(new Intent(this, UserFeedActivity.class));
    }

    private void startLoginActivity() {

        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
