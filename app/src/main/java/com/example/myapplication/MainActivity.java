package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity  {
    private EditText et_pass,et_email;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar ;
    private TextView sign_up,forgotPassword;
    private TextInputLayout inputLayout_email,inputLayout_pass;
    private CheckBox checkBox;
    public static final String SHARED_PREFS="sharedprefs";
    public static final String EMAIL="eml";
    public static final String PASSWORD="pass";
    public static final String CHECKBOX="checkbox";
    public static Boolean check;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String text1,text2;
    //private DatabaseRegistrar databaseRegistrar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Sign In");
        init();

        firebaseAuth=FirebaseAuth.getInstance();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sinup=new Intent(MainActivity.this,Main6Activity.class);
                sinup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(sinup);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(et_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Email Sent",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }
        });


       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection(v))
                    return;
                saveData();
                login(v);
            }

        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
        super.onBackPressed();
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

    private void init() {
        et_email=(EditText)findViewById(R.id.et_mail);
        et_pass=(EditText)findViewById(R.id.et_pass);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        sign_up=findViewById(R.id.signup);
        button=(Button)findViewById(R.id.et_bt1);
        inputLayout_email=findViewById(R.id.outlinedTextsignin);
        inputLayout_pass=findViewById(R.id.outlinedText_sign_pass);
        checkBox=findViewById(R.id.checkbox);
        forgotPassword=(TextView)findViewById(R.id.forgot_pass);
        loadData();
        updateView();
    }


    public void login(final View v){
        final String email=et_email.getText().toString();
        String pass=et_pass.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
            inputLayout_email.setError("please enter email");
            inputLayout_pass.setError("please enter password");
            return;
        }
            {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        showToast(v);


                        Intent intent = new Intent(MainActivity.this,Main5Activity.class);
                        //Toast.makeText(MainActivity.this,"logged in",Toast.LENGTH_SHORT).show();
                        //intent.putExtra("email",email);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("string_id",email); //InputString: from the EditText
                        editor.commit();

                        startActivity(intent);
                        updateUI();
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        //invalid pass
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this,"Wrong password entered",Toast.LENGTH_LONG).show();
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        //Email not in used
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this,"Wrong Email entered",Toast.LENGTH_LONG).show();
                    }


                }
            });

        }
    }
    private void updateUI() {
        FirebaseUser user =firebaseAuth.getCurrentUser();
        if(user==null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT);
            //return;
        }
        else{

        }
    }
    public void showToast(View v) {
        StyleableToast.makeText(this, "You Logged In Successfully!", R.style.mytoast).show();
    }
    public void saveData(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(EMAIL,et_email.getText().toString());
        editor.putString(PASSWORD,et_pass.getText().toString());
        editor.putBoolean(CHECKBOX,checkBox.isChecked());
        editor.apply();
    }
    public void loadData(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text1=sharedPreferences.getString(EMAIL,"");
        text2=sharedPreferences.getString(PASSWORD,"");
        check=sharedPreferences.getBoolean(CHECKBOX,false);
    }
    public void updateView(){
        if (check){
            et_email.setText(text1);
            et_pass.setText(text2);
            checkBox.setChecked(check);
        }

    }

}
