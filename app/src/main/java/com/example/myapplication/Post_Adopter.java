package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;


public class Post_Adopter extends FirebaseRecyclerAdapter<Post,Post_Adopter.PostViewHolder> {
    public Post_Adopter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull final Post post) {
        holder.branch.setText(post.getBranch()+" /");
        holder.name.setText(post.getName());
        holder.year.setText(post.getYear());

    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull List<Object> payloads) {

        super.onBindViewHolder(holder, position, payloads);
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView branch,name,year;
        private PostViewHolder(@NonNull View itemView) {
            super(itemView);
            branch=itemView.findViewById(R.id.branch);
            name=itemView.findViewById(R.id.name);
            year=itemView.findViewById(R.id.year);
        }
    }
}
