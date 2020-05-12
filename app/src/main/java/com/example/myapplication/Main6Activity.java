package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Main6Activity extends AppCompatActivity {
    private Button signup;
    public   EditText enroll,cnfpass;
    public   EditText email,pass;
    private TextView signin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextInputLayout inputLayout,inputLayout1,inputLayout2;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        getSupportActionBar().setTitle("Sign Up");
        init();
        hideprogressbar();
        mAuth=FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sin=new Intent(Main6Activity.this,MainActivity.class);
                sin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(sin);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup(v);
            }
        });










    }


    public void Signup(View v){
        if (checkInternetConnection(v))
            return;
        final String nemail=email.getText().toString();
        String npass=pass.getText().toString();
        String ncnfpass=cnfpass.getText().toString();

        if(validateEmail(nemail)&&validatePassword(npass,ncnfpass)) {
            showprogressbar();
            mAuth.createUserWithEmailAndPassword(nemail,npass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Intent intent = new Intent(Main6Activity.this,Info_Activity.class);
                        Toast.makeText(Main6Activity.this,"sign up",Toast.LENGTH_SHORT).show();
                        //intent.putExtra("email",nemail);
                        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("string_id",email); //InputString: from the EditText
                        editor.commit();*/
                        startActivity(intent);
                        updateUI();
                        hideprogressbar();
                    }
                    else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        hideprogressbar();
                        Toast.makeText(Main6Activity.this,"Email Already Used",Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }
    }


    private void init() {
        signup=findViewById(R.id.bt_sign_up);
        enroll=findViewById(R.id.enroll);
        email=findViewById(R.id.et_mail);
        pass=findViewById(R.id.et_pass);
        cnfpass=findViewById(R.id.et_cnfpass);
        signin=findViewById(R.id.sign_in);
        progressBar=findViewById(R.id.progressBar);
        inputLayout=findViewById(R.id.outlinedText);
        inputLayout1=findViewById(R.id.outlinedText1);
        inputLayout2=findViewById(R.id.outlinedText2);
    }

    private void updateUI() {
        FirebaseUser user =mAuth.getCurrentUser();
        if(user==null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT);
            //return;
        }
        else{

        }
    }
    private void showprogressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideprogressbar() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    private boolean validateEmail(String email) {


        if (email.isEmpty()) {
            inputLayout.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputLayout.setError("Please enter a valid email address");
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String pass, String cnfpass) {
        if (pass.isEmpty()) {
            inputLayout1.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(pass).matches()) {
            inputLayout1.setError("Minimum 6 character required");
            return false;
        } else if (!cnfpass.equals(pass)) {
            inputLayout2.setError("Password not matched");
            return false;
        } else {
            inputLayout1.setError(null);
            return true;
        }
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



}
