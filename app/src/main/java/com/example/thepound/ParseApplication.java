package com.example.thepound;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Tbi74aQpluinfRmWRCDbZ7IxN9j1PjovZ7wfHfb4")
                .clientKey("MwekBLG93TjHBw8d8QUtQcVfkC1VzoasFHNc4GSj")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
