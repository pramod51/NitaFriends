package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Main31Activity extends AppCompatActivity  {

    private RecyclerView recyclerView ;
    private RecyclerView recyclerView1 ;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    Post_Adopter post_adopter;
    Post_Adopter post_adopter1;
    private Query query;
    private Query query1;
    private static String state_this,district;
    private static final String DISTRICT="dis";
    private ProgressBar prg;
    int count=1;
    private LinearLayout linearLayout,linearLayout1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main31);
        getSupportActionBar().setTitle("District Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String nstate = prefs.getString("nstate", "defval"); //no id: default value
        final String newDistrict=prefs.getString(DISTRICT,"");



        FloatingActionMenu materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        FloatingActionButton floatingActionButton1 = (FloatingActionButton)findViewById(R.id.material_design_floating_action_menu_item1);
        FloatingActionButton floatingActionButton2 = (FloatingActionButton)findViewById(R.id.material_design_floating_action_menu_item2);








        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prg = (ProgressBar) findViewById(R.id.flv_progress_bar);
        prg.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        String userid = "null";
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Main31Activity.this,"You are now here",Toast.LENGTH_SHORT).show();

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(Main31Activity.this,"Option2 clicked",Toast.LENGTH_SHORT).show();
                onBackPressed();

            }
        });



        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                state_this = dataSnapshot.child("State").getValue(String.class);
                if (dataSnapshot.child("District").exists()){
                    district=dataSnapshot.child("District").getValue(String.class);
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Main31Activity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nstate", state_this); //InputString: from the EditText
                editor.putString(DISTRICT,district);
                editor.apply();
                prg.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        mRef = mDatabase.getReference();
        //Toast.makeText(Main3Activity.this,nstate,Toast.LENGTH_LONG).show();
        //query = mRef.child("Users").orderByChild("State").equalTo(nstate);
        query = mRef.child("Users").orderByChild("StateDistrict").equalTo(nstate+newDistrict.replace(" ","").toLowerCase());

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        post_adopter = new Post_Adopter(options);
        recyclerView.setAdapter(post_adopter);

       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               count=(int)dataSnapshot.getChildrenCount();
               mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("DistrictFriend").setValue(count-1);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


        //mRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Statefriend").setValue(count);
    }

    @Override
    protected void onStart() {
        super.onStart();
        post_adopter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        post_adopter.stopListening();
    }



}
