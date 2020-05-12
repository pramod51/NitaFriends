package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tuyenmonkey.mkloader.MKLoader;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.constraint.Compression;

public class Main4Activity extends AppCompatActivity{
    private TextView branch,college,year,profile,email,state,Nphone,Sfriend,districtFriends;
    private DatabaseReference mRef;
    private Button edit;
    private FirebaseDatabase mDatabase;
    public String new_year;
    private String name,ncollege,phone;
    private ProgressBar bar;
    public final static  int GALLERY_PIC=1;
    private final int PERMISSION_ALL = 2534;
    private ImagePicker imagePicker;
    private MKLoader loader;
    private StorageReference UserProfileImageRef;
    private ImageView imageView;
    private Button upload,delete_img;
    private LinearLayout scrollView;
    ArrayAdapter<CharSequence> adapter;
    Bitmap thumb_Bitmap=null;
    final static int Gallry_pic=1;
    private AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upload = findViewById(R.id.button_profile_img);
        loader = findViewById(R.id.loader_img);
        imageView = findViewById(R.id.profile_img);
        scrollView=findViewById(R.id.scrollview);

        districtFriends=findViewById(R.id.district_friend);
        delete_img=findViewById(R.id.delete_img);
        profile=findViewById(R.id.profile_name);
        branch=(TextView)findViewById(R.id.branch);
        college=(TextView)findViewById(R.id.college);
        year=(TextView)findViewById(R.id.year);
        email=(TextView)findViewById(R.id.email);
        state=(TextView)findViewById(R.id.State);
        edit=findViewById(R.id.edit_profi);
        Nphone=(TextView)findViewById(R.id.phone_no);
        bar=findViewById(R.id.progress_bar);
        bar.setVisibility(View.VISIBLE);
        Sfriend=(TextView) findViewById(R.id.state_friend);
        Sfriend.setText("not set");
        scrollView.setVisibility(View.INVISIBLE);
        UserProfileImageRef=FirebaseStorage.getInstance().getReference().child("Profile Image");
        final String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };
        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent();
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                profileIntent.setType("image/*");
                startActivityForResult(profileIntent,Gallry_pic);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String data = prefs.getString("string_id", "no id"); //no id: default value
        email.setText(data);


        mRef =  mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        readData();






    }

    private void OpenDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Are you sure to Delete")
//set message
                .setMessage("Your Profile Picture will removed")
//set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("ProfileImg").removeValue();
                        finish();
                    }
                })
//set negative button
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallry_pic&&resultCode==RESULT_OK&&data!=null){
           Uri ImageUri = data.getData();
           CropImage.activity()
                   .setGuidelines(CropImageView.Guidelines.ON)
                   .setAspectRatio(1,1)
                   .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                loader.setVisibility(View.VISIBLE);
                final Uri resultUri=result.getUri();


                final StorageReference filePath=UserProfileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Main4Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl=uri.toString();
                                    Picasso.get().load(downloadUrl).into(imageView);
                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("ProfileImg").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        loader.setVisibility(View.INVISIBLE);
                                                    }
                                                    else {
                                                        Toast.makeText(Main4Activity.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        loader.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            });

                                }
                            });


                        }
                        else
                            Toast.makeText(Main4Activity.this,"Error:"+task.getException().toString(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    private void readData(){
        mRef =  mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("ProfileImg").exists())
                    Picasso.get().load(dataSnapshot.child("ProfileImg").getValue(String.class)).into(imageView);
                if(!Objects.equals(dataSnapshot.child("Name").getValue(String.class), ""))
                profile.setText(dataSnapshot.child("Name").getValue(String.class));
               if (!Objects.equals(dataSnapshot.child("College").getValue(String.class), ""))
                college.setText(dataSnapshot.child("College").getValue(String.class));
                if (!Objects.equals(dataSnapshot.child("Year").getValue(String.class), ""))
                year.setText(dataSnapshot.child("Year").getValue(String.class));
                if (!Objects.equals(dataSnapshot.child("Branch").getValue(String.class), ""))
                branch.setText(dataSnapshot.child("Branch").getValue(String.class));
                if(dataSnapshot.child("Phone").exists()&&!Objects.equals(dataSnapshot.child("Phone").getValue(String.class), ""))
                    Nphone.setText(dataSnapshot.child("Phone").getValue(String.class));
                if(dataSnapshot.child("Statefriend").exists()&&!Objects.equals(dataSnapshot.child("Statefriend").getValue(Integer.class), "-1"))
                   Sfriend.setText(Integer.toString(dataSnapshot.child("Statefriend").getValue(Integer.class)));
                if(dataSnapshot.child("DistrictFriend").exists()&&!Objects.equals(dataSnapshot.child("DistrictFriend").getValue(Integer.class), "-1"))
                    districtFriends.setText(Integer.toString(dataSnapshot.child("DistrictFriend").getValue(Integer.class)));
                //Toast.makeText(Main4Activity.this,dataSnapshot.child("State").getValue(String.class),Toast.LENGTH_LONG).show();
                //state.setText(dataSnapshot.child("State").getValue(String.class));
                scrollView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void openDialog() {
        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(Main4Activity.this);
        	                    View customView = getLayoutInflater().inflate(R.layout.layout_dialog,null);
        	                    Button btnClose = customView.findViewById(R.id.apply);
        	                    final EditText et_name,et_year,et_college,et_phone;
        	                    et_name=(EditText)customView.findViewById(R.id.etname);
        	                    et_year=(EditText)customView.findViewById(R.id.etyear);
                                et_college=(EditText)customView.findViewById(R.id.etcollege);
                                et_phone=(EditText)customView.findViewById(R.id.etphone);
        disableEditText(et_year);


        et_year.setOnClickListener(new View.OnClickListener() {
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


        alertDialog1.setView(customView);
        final AlertDialog dialog=alertDialog1.create();
        dialog.show();

        	                    btnClose.setOnClickListener(new View.OnClickListener() {
	                        @SuppressLint("SetTextI18n")
                            @Override
	                        public void onClick(View view) {
	                            name=et_name.getText().toString();
	                            new_year=et_year.getText().toString();
	                            ncollege=et_college.getText().toString();
	                            phone=et_phone.getText().toString();

                                mRef =  mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                if (!name.equals("")){
                                    mRef.child("Name").setValue(name);
                                }
                                if (!ncollege.equals("")){
                                    mRef.child("College").setValue(ncollege);
                                }
                                if (!new_year.equals("")){

                                    mRef.child("Year").setValue(new_year);
                                }
                                if (!phone.equals("")){
                                    if(phone.length()<10){
                                        et_phone.setError("Please enter correct phone no.");
                                        return;
                                    }
                                    Nphone.setText("+91 "+phone);
                                    mRef.child("Phone").setValue("+91 "+phone);
                                }
                                dialog.dismiss();


                	                        }
	                    });


    }

    private void enableEditText(EditText editText) {
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        //editText.setEnabled(false);
        //editText.setCursorVisible(false);
        //editText.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }




}
