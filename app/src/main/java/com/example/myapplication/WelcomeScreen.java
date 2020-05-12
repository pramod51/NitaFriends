package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Window window = WelcomeScreen.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(WelcomeScreen.this,R.color.dark_magenta));
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                {
                    if(user==null){
                        Intent intent = new Intent(WelcomeScreen.this,FirstPage.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent activitys=new Intent(WelcomeScreen.this,Main5Activity.class);
                        startActivity(activitys);
                        finish();
                    }
                }


            }
        }),2000);
    }
}
