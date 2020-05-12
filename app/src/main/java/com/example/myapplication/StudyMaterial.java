package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static androidx.appcompat.app.AppCompatActivity.*;

public class StudyMaterial extends YouTubeBaseActivity {

    private RecyclerView recyclerView ;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    StudyMatAdopter studyMatAdopter;
    private Query query;
    private static String state_this;
    String[] choices = {"1st Semester", "2nd Semester", "3rd Semester","4th Semester","Others"};
    private TextView Semester;
    private Button editText,addData;
    private ProgressBar prg;
    public static final String SHARED_PREFS="sharedprefs";
    public static final String SEMESTER="sem";
    private String text;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mat_layout);
     // getActionBar().getSupportActionBar().setTitle("Study Material");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addData=findViewById(R.id.add);
        recyclerView=findViewById(R.id.recyclerview);
        editText=findViewById(R.id.editSemester);
        Semester=findViewById(R.id.semester);
        progressBar=findViewById(R.id.progress_bar_sem);
        progressBar.setVisibility(View.VISIBLE);
        loadData();
        updateView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();


        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Choose Semester")
                        .setPositiveButton("Ok", null)
                        .setNeutralButton("Cancel", null)
                        .setSingleChoiceItems(choices, 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView lw = ((AlertDialog) dialog).getListView();
                                Object checkedItem = lw.getAdapter().getItem(which);
                               Semester.setText(checkedItem.toString());
                               saveData(checkedItem.toString());
                               restartActivity(StudyMaterial.this);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        //Toast.makeText(StudyMaterial.this,Semester.getText().toString(),Toast.LENGTH_LONG).show();
        mRef = mDatabase.getReference();
        //Toast.makeText(Main3Activity.this,nstate,Toast.LENGTH_LONG).show();
        query = mRef.child("Semester").child(Semester.getText().toString());



        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        studyMatAdopter = new StudyMatAdopter(options);
        recyclerView.setAdapter(studyMatAdopter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //FirebaseDatabase.getInstance().getReference().child("abc").setValue();
                studyMatAdopter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
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



        studyMatAdopter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        studyMatAdopter.stopListening();
    }

    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();

    }

    public void saveData(String sem){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(SEMESTER,sem);
        editor.apply();
    }
    public void loadData(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text=sharedPreferences.getString(SEMESTER,"");
    }
    public void updateView(){
            Semester.setText(text);

    }


    public void openDialog() {
        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(StudyMaterial.this);
        View customView = getLayoutInflater().inflate(R.layout.add_data_study_material,null);
        final EditText addTittle,addDescription,addLink;
        Button ok;
        addTittle=(EditText)customView.findViewById(R.id.add_title);
        addDescription=(EditText)customView.findViewById(R.id.add_description);
        addLink=(EditText)customView.findViewById(R.id.add_link);
        ok=customView.findViewById(R.id.done);


       /* et_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(Main4Activity.this, v);
                popupMenu.getMenu().add("1st Year");
                popupMenu.getMenu().add("2nd Year");
                popupMenu.getMenu().add("3rd Year");
                popupMenu.getMenu().add("4th Year");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        et_year.setText(item.getTitle());
                        year.setText(et_year.getText().toString());
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
*/

        alertDialog1.setView(customView);
        final AlertDialog dialog=alertDialog1.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(StudyMaterial.this,"Make Sure You Already Selected Correct Semester on Top Screen",Toast.LENGTH_LONG).show();
                String tittle = addTittle.getText().toString();
                String link = addLink.getText().toString();
                String description = addDescription.getText().toString();
                String sem=Semester.getText().toString();

                mRef =  mDatabase.getReference().child("Semester").child(sem).push();
                if (!tittle.equals("")){
                    mRef.child("Id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mRef.child("tittle").setValue(tittle);
                }
                if (!link.equals("")){
                    mRef.child("link").setValue(link);
                }
                if (!description.equals("")){

                    mRef.child("description").setValue(description);
                }
                if (!sem.equals("")&&!tittle.equals("")){
                    mRef.child("sem").setValue(sem);
                }
                dialog.dismiss();
                restartActivity(StudyMaterial.this);

            }
        });


    }

}

