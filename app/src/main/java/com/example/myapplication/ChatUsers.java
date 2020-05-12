package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatUsers extends AppCompatActivity {
    private DatabaseReference mRef;
    private ChatListAdopter chatListAdopter;
    private RecyclerView recyclerView;
    private Query query;
    private FirebaseDatabase mDatabase;
    private ProgressBar prg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prg = (ProgressBar) findViewById(R.id.flv_progress_bar);
        prg.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        String userid = "null";
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nstate = prefs.getString("nstate", "defval"); //no id: default value





        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String state_this = dataSnapshot.child("State").getValue(String.class);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChatUsers.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nstate", state_this); //InputString: from the EditText
                editor.apply();
                prg.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mRef = mDatabase.getReference();
        query = mRef.child("Users").orderByChild("State").equalTo(nstate);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        chatListAdopter = new ChatListAdopter(options);
        recyclerView.setAdapter(chatListAdopter);
        chatListAdopter.startListening();
        FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("userState")
                .child("state").setValue("online");


       /*  newtimer = new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                String  saveCurrentTime = currentTime.format(calendar.getTime());


                FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("userState")
                        .child("state").setValue(saveCurrentTime.toUpperCase());
                FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userState")
                        .child("date").setValue(saveCurrentDate);

            }
            public void onFinish() {

            }
        };
        newtimer.start();
*/



    }
    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onStop() {
        super.onStop();
        chatListAdopter.stopListening();

    }
}

