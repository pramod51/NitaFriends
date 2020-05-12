package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdopter extends FirebaseRecyclerAdapter<Post, ChatListAdopter.ChatListViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatListAdopter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position, @NonNull final Post model) {
        holder.name.setText(model.getName());

      /*  CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {



            }
            public void onFinish() {

            }
        };
        newtimer.start();*/

      /*  FirebaseDatabase.getInstance().getReference()
                .child("Messages").child(model.getId()).child("pageStatus")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("countNewMsg").exists()&&dataSnapshot.child("countNewMsg").getValue(Integer.class).equals(0))
                            holder.newMessage.setTextColor(Color.WHITE);
                        else if (dataSnapshot.child("countNewMsg").exists()&&!dataSnapshot.child("countNewMsg").getValue(Integer.class).equals(0))
                            holder.newMessage.setText(Integer.toString(dataSnapshot.child("countNewMsg").getValue(Integer.class))+" new message");
                        else
                            holder.newMessage.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

*/


        Picasso.get().load(model.getProfileImg()).placeholder(R.drawable.profile_default).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visit_user_id = getRef(position).getKey();
                Intent intent=new Intent(v.getContext(),Chat.class);
                intent.putExtra("visit_user_id",model.getId());
                intent.putExtra("visit_user_name",model.getName());
                intent.putExtra("visit_image",model.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        TextView name,newMessage;
        CircleImageView imageView;
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            newMessage=(TextView)itemView.findViewById(R.id.last_message);
            imageView=itemView.findViewById(R.id.user_img);
        }
    }
}
