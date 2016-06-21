package com.caleb.fblaoutfit;

import com.twitter.sdk.android.core.Session;

//check to see if user is logged in accessible from any class
public class AutoLogin {
    public static Session recordInitialSessionState(Session twitterSession) {
        if (twitterSession != null) {
            return twitterSession;
        } else {
            return null;
        }
    }

}
