package com.example.thepound.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thepound.Comment;
import com.example.thepound.CommentAdapter;
import com.example.thepound.Post;
import com.example.thepound.PostsAdapter;
import com.example.thepound.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    private Post currentPost;
    private String commentPostId;
    private RecyclerView rvComments;
    protected CommentAdapter adapter;
    protected List<Comment> allComments;
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivProfile;
    private TextView tvCreatedAt;
    private TextView tvTile;
    private EditText etComment;
    private Button btnPost;

    public static DetailFragment newInstance(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            commentPostId = bundle.getString("postId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        tvTile = view.findViewById(R.id.tvTitle);
        btnPost = view.findViewById(R.id.btnPost);
        etComment = view.findViewById(R.id.etComment);

        allComments = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), allComments );

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        readBundle(getArguments());
        queryPosts();

        queryComments();
    }

    private void saveComment(final String description, ParseUser currentUser, String postObjectId){
        final Comment comment = new Comment();
        comment.setUser(currentUser);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("objectId", postObjectId);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts){
                    comment.setEvent(post);
                    comment.setDescription(description);
                    comment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Issue with saving comment", e);
                                Toast.makeText(getContext(), "Issue with saving comment", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.i(TAG, "Event save was successful!!");
                                etComment.setText("");
                                queryComments();
                            }
                        }
                    });
                }
            }
        });
    }

    protected void queryComments() {
        final ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER_ID);
        query.setLimit(20);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Comment comment: comments){
                    Log.i(TAG, "Comment: " + comment.getDescription() + " username: " + comment.getUser().getUsername());
                }
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo("objectId", commentPostId);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post: posts){
                    final String postObjectId = post.getObjectId();
                    currentPost = post;
                    tvDescription.setText(post.getDescription());
                    tvTile.setText(post.getTitle());
                    tvUsername.setText(post.getUser().getUsername());
                    tvCreatedAt.setText(post.getCreatedAt().toString());
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String comment = etComment.getText().toString();
                            if (comment.isEmpty()){
                                Toast.makeText(getContext(), "Text Box Empty", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                saveComment(comment, currentUser, postObjectId);
                            }
                        }
                    });
                    queryComments();
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }
}