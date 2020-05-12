package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class OtpPage extends AppCompatActivity {
    private TextView next,resend;
    private EditText n1,n2,n3,n4,n5,n6;
    final static String PHONE="phone";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String id;
    String phoneN0=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);
        getSupportActionBar().setTitle("OTP confirmation");
        next=findViewById(R.id.next);
        resend=findViewById(R.id.resend);
        progressBar=findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        n1=findViewById(R.id.et1);
        n2=findViewById(R.id.et2);
        n3=findViewById(R.id.et3);
        n4=findViewById(R.id.et4);
        n5=findViewById(R.id.et5);
        n6=findViewById(R.id.et6);
        n1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==1) {
                    n1.clearFocus();
                    n2.setCursorVisible(true);
                    n2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        n2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==1) {
                    n2.clearFocus();
                    n3.setCursorVisible(true);
                    n3.requestFocus();
                }
                if (count==0){
                    n2.clearFocus();
                    n1.setCursorVisible(true);
                    n1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        n3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==1) {
                    n3.clearFocus();
                    n4.setCursorVisible(true);
                    n4.requestFocus();
                }
                if (count==0){
                    n3.clearFocus();
                    n2.setCursorVisible(true);
                    n2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        n4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==1) {
                    n4.clearFocus();
                    n5.setCursorVisible(true);
                    n5.requestFocus();
                }
                if (count==0){
                    n4.clearFocus();
                    n3.setCursorVisible(true);
                    n3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        n5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==1) {
                    n5.clearFocus();
                    n6.setCursorVisible(true);
                    n6.requestFocus();
                }
                if (count==0){
                    n5.clearFocus();
                    n4.setCursorVisible(true);
                    n4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        n6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==0){
                    n6.clearFocus();
                    n5.setCursorVisible(true);
                    n5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //otp=n1.getText().toString()+n2.getText().toString()+n3.getText().toString()+n4.getText().toString()
         //       +n5.getText().toString()+n6.getText().toString();

       phoneN0="+91"+getIntent().getStringExtra(PHONE);

        SendVerificationCode(phoneN0 );



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp=n1.getText().toString()+n2.getText().toString()
                        +n3.getText().toString()+n4.getText().toString()
                        +n5.getText().toString()+n6.getText().toString();
                //Toast.makeText(OtpPage.this,otp,Toast.LENGTH_LONG).show();

                if (otp.isEmpty()){
                    Toast.makeText(OtpPage.this,"Please Enter Your OTP",Toast.LENGTH_LONG).show();
                }
                else if (otp.replace(" ","").length()<6){
                    Toast.makeText(OtpPage.this,"Please Enter 6 Digit OTP",Toast.LENGTH_LONG).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationCode(phoneN0);
            }
        });

    }

    private void SendVerificationCode(String phoneN0) {
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);
            }
        }.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneN0,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OtpPage.this.id=id;
                        //super.onCodeSent(s, forceResendingToken);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OtpPage.this," Verification Failed "+e.toString(),Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpPage.this,"OTP Verified",Toast.LENGTH_LONG).show();
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                //do create new user
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Phone").setValue(phoneN0);
                                Intent inte = new Intent(OtpPage.this,Info_Activity.class);
                                inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(inte);

                            } else {
                                //user is exists, just do login
                                startActivity(new Intent(OtpPage.this,Main5Activity.class));
                            }


                            progressBar.setVisibility(View.GONE);
                        }
                        else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(OtpPage.this,"Verification Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
