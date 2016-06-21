package com.caleb.fblaoutfit;

import com.caleb.fblaoutfit.UserFeedActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    //variables
    TwitterLoginButton loginButton;
    static String User;//contains user name of logged in twitter account

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TwitterButtonSetup();//set up twitter button
    }

    public void TwitterButtonSetup() {

        //create twitter button
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        //login to twitter on button press
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //login succeeded
                Intent feed_intent = new Intent(LoginActivity.this, UserFeedActivity.class);//create feed activity intent
                User = result.data.getUserName();//get user name from result
                userLog(User);//send user name to user log
                startActivity(feed_intent);//start feed activity
            }

            @Override
            public void failure(TwitterException e) {
                //login failed
                Toast.makeText(LoginActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();//let user know something went wrong, possible reason internet

            }
        });

    }

    public void userLog(String name) {
        //tell which user logged in
        Answers.getInstance().logCustom(new CustomEvent("UserLogin")//makes a group called "userLogin"
                .putCustomAttribute(name, "Login")//puts username in group
                .putCustomAttribute("Length", 350));//no reason to have in this case but required for function to work (commonly used to tell developer how many times a user has accessed this code)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish(); //go back to system menu, fixes going to blank white screen on back pressed (initial activity)
    }
}