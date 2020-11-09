package com.example.thepound.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thepound.Like;
import com.example.thepound.LoginActivity;
import com.example.thepound.Post;
import com.example.thepound.PostsAdapter;
import com.example.thepound.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LikedEventsFragment extends Fragment {

    public static final String TAG = "LikedEventsFragment";
    private RecyclerView rvLikedPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private List<Like> allLikes;

    public LikedEventsFragment() {
        // Required empty public constructor
    }

    public void queryLikes(){
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER_ID);
        query.include(Like.KEY_EVENT_ID);
        query.whereEqualTo(Like.KEY_USER_ID, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                allLikes.addAll(likes);
                queryPosts();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLikedPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        allLikes = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts );

        rvLikedPosts.setAdapter(adapter);
        rvLikedPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryLikes();
       // queryPosts();
    }

    protected void queryPosts() {
        for(Like like: allLikes){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
            String postevent = like.getEvent().getObjectId();
        query.whereEqualTo("objectId", like.getEvent().getObjectId());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post: posts){
                    Log.i(TAG, "Post: " + post.getDescription() + " username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
        }
    }
}