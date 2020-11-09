package com.example.thepound;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_USER_ID = "user";
    public static final String KEY_EVENT_ID = "event";
    public static final String KEY_DESCRIPTION = "description";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER_ID);
    }

    public void setUser(ParseUser parseuser){
        put(KEY_USER_ID, parseuser);
    }

    public ParseObject getEvent() {
        return getParseObject(KEY_EVENT_ID);
    }

    public void setEvent(Post event){
        put(KEY_EVENT_ID, event);
    }
}
