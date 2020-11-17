package com.example.thepound;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thepound.fragments.DetailFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = "PostAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfile;
        private TextView tvCreatedAt;
        private TextView tvTile;
        private ImageView ivLike;
        private TextView tvLikes;
        private EditText etComment;
        private Button btnPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvTile = itemView.findViewById(R.id.tvTitle);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnPost = itemView.findViewById(R.id.btnPost);
            etComment = itemView.findViewById(R.id.etComment);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Post post) {
            tvDescription.setText(post.getDescription());
            tvTile.setText(post.getTitle());
            tvUsername.setText(post.getUser().getUsername());
            tvCreatedAt.setText(post.getCreatedAt().toString());
            tvLikes.setText(post.getLikes().toString() + " Likes");
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
                tvTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DetailFragment detailFragment = DetailFragment.newInstance(post.getObjectId());
                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContainer, detailFragment);
                        fragmentTransaction.commit();;
                    }
                });
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = etComment.getText().toString();
                        if (comment.isEmpty()){
                            Toast.makeText(context, "Text Box Empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            saveComment(comment, currentUser, post);
                        }
                    }
                });

                ivLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        saveLike(currentUser, post);
                    }
                });
                ParseFile profileImage = post.getUser().getParseFile("profilePic");
                if (profileImage != null) {
                    Glide.with(context).load(post.getUser().getParseFile("profilePic").getUrl()).into(ivProfile);
                }

            }
        }

        private void saveComment(String description, ParseUser currentUser, Post post){
            Comment comment = new Comment();
            comment.setUser(currentUser);
            comment.setEvent(post);
            comment.setDescription(description);
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with saving comment", e);
                        Toast.makeText(context, "Issue with saving comment", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.i(TAG, "Event save was successful!!");
                        etComment.setText("");
                    }
                }
            });
        }

        private void saveLike(ParseUser currentUser, final Post post){
            final boolean[] liked = {false};
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.include(Like.KEY_USER_ID);
            query.include(Like.KEY_EVENT_ID);
            query.whereEqualTo(Like.KEY_USER_ID, currentUser);
            query.whereEqualTo(Like.KEY_EVENT_ID, post);
            query.findInBackground(new FindCallback<Like>() {
                                       @Override
                                       public void done(List<Like> likes, ParseException e) {
                                           if (e != null){
                                               Log.e(TAG, "Issue with liking event", e);
                                               return;
                                           }

                                           if(likes.size() != 0){
                                               for(Like like: likes){
                                                   try {
                                                       liked[0] = true;
                                                       like.delete();
                                                       post.subtractLike();
                                                       post.saveInBackground(new SaveCallback() {
                                                           @Override
                                                           public void done(ParseException e) {
                                                               if (e != null) {
                                                                   Log.e(TAG, "Issue liking events", e);
                                                                   Toast.makeText(context, "Issue liking event", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                                       notifyDataSetChanged();
                                                   } catch (ParseException ex) {
                                                       ex.printStackTrace();
                                                   }
                                               }
                                               return;
                                           }
                                           if (liked[0] != true) {
                                               Like like = new Like();
                                               like.setUser(ParseUser.getCurrentUser());
                                               like.setEvent(post);
                                               like.saveInBackground(new SaveCallback() {
                                                   @Override
                                                   public void done(ParseException e) {
                                                       if (e != null) {
                                                           Log.e(TAG, "Issue with liking event", e);
                                                           Toast.makeText(context, "Issue with liking event", Toast.LENGTH_SHORT).show();
                                                       } else {
                                                           Log.i(TAG, "Liked!!");
                                                       }
                                                   }
                                               });
                                               post.addLike();
                                               post.saveInBackground(new SaveCallback() {
                                                   @Override
                                                   public void done(ParseException e) {
                                                       if (e != null) {
                                                           Log.e(TAG, "Issue liking events", e);
                                                           Toast.makeText(context, "Issue liking event", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });
                                               notifyDataSetChanged();
                                           }

                                       }
                                   }
            );
        }
    }
}
