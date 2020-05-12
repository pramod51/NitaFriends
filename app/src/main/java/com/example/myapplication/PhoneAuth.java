package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneAuth extends AppCompatActivity {

    private EditText phoneNo;
    private Button continue_no;
    final static String PHONE="phone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        phoneNo=findViewById(R.id.mobile_no);
        continue_no=findViewById(R.id.continue_button);
        continue_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNo.getText().toString().isEmpty()){
                    phoneNo.setError("Please enter mobile number");
                }
                else if (phoneNo.getText().toString().length()<10){
                    phoneNo.setError("Please enter Valid mobile number");
                }
                else {
                    Intent intent=new Intent(PhoneAuth.this,OtpPage.class);
                    intent.putExtra(PHONE,phoneNo.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
