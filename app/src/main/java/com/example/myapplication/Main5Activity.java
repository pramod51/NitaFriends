package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Main5Activity extends AppCompatActivity  {
    private Button button_1,button_2,button_3,button_4,button_5,button_6;
    private long backPressedTime;
    private FirebaseAuth mAuth;
    private Toast backToast;
    private DatabaseReference mRef;
    private ViewFlipper viewFlipper;

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        init();












        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        //MobileAds.initialize(this, "ca-app-pub-4725607852978925~7627961705");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        
        mAuth=FirebaseAuth.getInstance();
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection(v))
                    return;
                Intent intent=new Intent(Main5Activity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        mRef=FirebaseDatabase.getInstance().getReference().child("images");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(Main5Activity.this,dataSnapshot.child("img1").getValue(String.class),Toast.LENGTH_LONG).show();

               String s1,s2,s3,s4,s5;
                        s1=dataSnapshot.child("img2").getValue(String.class);
                       s2= dataSnapshot.child("img3").getValue(String.class);
                        s3=dataSnapshot.child("img4").getValue(String.class);
                        s4=dataSnapshot.child("img5").getValue(String.class);
                        s5=dataSnapshot.child("img1").getValue(String.class);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Main5Activity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("img1",s1); //InputString: from the EditText
                editor.putString("img2",s2);
                editor.putString("img3",s3);
                editor.putString("img4",s4);
                editor.putString("img5",s5);
                editor.apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ss1 = prefs.getString("img1", "img2"); //no id: default value
        String ss2=prefs.getString("img2","img3");
        String ss3=prefs.getString("img3","img4");
        String ss4=prefs.getString("img4","img5");
        String ss5=prefs.getString("img5","img1");
        //String[] ImgAry={s1,s2,s3,s4,s5};
        //String[] ImgAry={"https://firebasestorage.googleapis.com/v0/b/my-app-ddc5c.appspot.com/o/10599313_1481051762165229_9107971942888388185_n.jpg?alt=media&token=d48ca2ef-4da8-4aee-a67c-9b715633856c",
        //"https://firebasestorage.googleapis.com/v0/b/my-app-ddc5c.appspot.com/o/10655442_1494164247520647_7269740583449746542_o.png?alt=media&token=f48bd8c7-28f9-4dc5-8d68-912345e65983"};

        String[] ImgAry={ss1,ss2,ss2,ss3,ss4,ss5};


        for(int i=0;i<ImgAry.length;i++)
        {
            // create dynamic image view and add them to ViewFlipper
            setImageInFlipr(ImgAry[i]);

        }







        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection(v))
                    return;
                Intent intent1=new Intent(Main5Activity.this, Main3Activity.class);
                startActivity(intent1);
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection(v))
                    return;
                Intent intent2=new Intent(Main5Activity.this, Main4Activity.class);
                startActivity(intent2);
            }
        });
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Main5Activity.this,Info_Activity.class));
                //Toast.makeText(Main5Activity.this,"okk",Toast.LENGTH_LONG).show();
            }
        });
        button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main5Activity.this,ChatUsers.class));
            }
        });
        button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main5Activity.this,StudyMaterial.class));
            }
        });



    }



    private Boolean checkInternetConnection(View view) {
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null){
            Snackbar snackbar = Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.dark_orange));
            snackbar.setTextColor(ContextCompat.getColor(this,R.color.black));
            snackbar.show();
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.signout:
                if (checkInternetConnection(getWindow().getDecorView().getRootView()))
                    return false;
                mAuth.signOut();
                startActivity(new Intent(Main5Activity.this,FirstPage.class));
                Toast.makeText(Main5Activity.this,"Logged Out",Toast.LENGTH_LONG).show();
                return true;
            case R.id.privacy:
                startActivity(new Intent(Main5Activity.this,PrivacyPoliciy.class));
                return true;
            case R.id.delete_acc:
                final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseAuth.getInstance().getCurrentUser()
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(uid)
                                    .removeValue();
                            Toast.makeText(Main5Activity.this,"Account Deleted",Toast.LENGTH_LONG).show();
                            Intent ac = new Intent(Main5Activity.this,FirstPage.class);
                            ac.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(ac);
                        }else{
                            Toast.makeText(Main5Activity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void init() {
        viewFlipper=findViewById(R.id.vflipper);
        button_1=findViewById(R.id.button1);
        button_2=findViewById(R.id.button2);
        button_3=findViewById(R.id.button3);
        button_4=findViewById(R.id.button4);
        button_5=findViewById(R.id.button5);
        button_6=findViewById(R.id.button6);
    }
    private void setImageInFlipr(String imgUrl) {

        ImageView image = new ImageView(getApplicationContext());
        image.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal);
        Picasso.get().load(imgUrl).into(image);
        viewFlipper.addView(image);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        //Animation
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
private CountDownTimer newtimer;
    @Override
    protected void onStart() {
        super.onStart();

        /* newtimer = new CountDownTimer(1000000000, 1000) {

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



        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String  saveCurrentTime = currentTime.format(calendar.getTime());

                FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("userState")
                        .child("state").setValue("online");
                FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userState")
                        .child("date").setValue(saveCurrentDate);

        FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userState")
                .child("time").setValue(saveCurrentTime);


        FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userState")
                .child("state").onDisconnect().setValue("offline");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}


