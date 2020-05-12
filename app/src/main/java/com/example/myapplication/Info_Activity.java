package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

import org.w3c.dom.Comment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import static android.widget.ArrayAdapter.createFromResource;

public class Info_Activity extends AppCompatActivity {
    private Button upload,update;
    final static int Gallry_pic=1;
    private MKLoader loader;
    private ImageView imageView;
    private final int PERMISSION_ALL = 2534;
    private ImagePicker imagePicker;
    public EditText inputName,inputDistrict,inputEnrool,inputCollege;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private TextInputLayout inputLayoutName,inputLayoutDistrict,inputLayoutyear,inputLayoutState,inputLayoutEnrool,inputLayoutCollege;
    private StorageReference mStorage;
    private AutoCompleteTextView autoCompleteTextView,autoCompleteTextView_State,autoCompleteTextView_branch;
    ArrayAdapter adapter,adapter_state,adapter_branch;
    public String state,year,branch;
    private TextView ch;
    private StorageReference UserProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_);

        init();

        final String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent();
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                profileIntent.setType("image/*");
                startActivityForResult(profileIntent,Gallry_pic);
            }
        });
        



     /*   upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(Info_Activity.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(Info_Activity.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                    imagePicker.choosePicture(true );
            }
        });

        imagePicker = new ImagePicker(this,
                null,
                new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        UCrop.of(imageUri, getTempUri())
                                .withAspectRatio(1, 1)
                                .start(Info_Activity.this);
                    }
                });*/
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference();

        mRef =  mDatabase.getReference().child("Users");

        //readData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEnrool.getText().toString().equals("")){
                    inputEnrool.setError("Enrolment number required");
                    return;
                }
                if (inputDistrict.getText().toString().equals("")){
                    inputDistrict.setError("District required");
                    return;
                }



                final HashMap<String, String> map=new HashMap<>();
                map.put("Name",inputName.getText().toString());
                map.put("College",inputCollege.getText().toString());
                map.put("EnrolmentNo",inputEnrool.getText().toString());
                map.put("State",state);
                map.put("Branch",branch);
                map.put("Year",year);
                map.put("District",inputDistrict.getText().toString());
                map.put("StateDistrict",state+inputDistrict.getText().toString().toLowerCase().replace(" ",""));
                map.put("Id",FirebaseAuth.getInstance().getCurrentUser().getUid());

                mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map);
                //onBackPressed();
                Intent go=new Intent(Info_Activity.this,Main5Activity.class);
                startActivity(go);
            }
        });

         adapter= new ArrayAdapter<CharSequence>(
                Info_Activity.this,getAdaptorItemLayout(),
                getResources().getStringArray(R.array.Year)
        );
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                year = arg0.getAdapter().getItem(arg2).toString();

            }
        });
        adapter_branch= new ArrayAdapter<CharSequence>(
                Info_Activity.this,getAdaptorItemLayout(),
                getResources().getStringArray(R.array.Departments)
        );
        autoCompleteTextView_branch.setAdapter(adapter_branch);
        autoCompleteTextView_branch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                 branch = arg0.getAdapter().getItem(arg2).toString();

            }
        });
        adapter_state= new ArrayAdapter<CharSequence>(
                Info_Activity.this,getAdaptorItemLayout(),
                getResources().getStringArray(R.array.india_states)
        );
        autoCompleteTextView_State.setAdapter(adapter_state);
        autoCompleteTextView_State.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                 state = arg0.getAdapter().getItem(arg2).toString();

            }
        });



    }

    private void init() {
        upload = findViewById(R.id.button_profile);
        loader = findViewById(R.id.loader);
        imageView = findViewById(R.id.profile);
        inputLayoutName = (TextInputLayout) findViewById(R.id.outlinedTextField);
        inputLayoutyear=(TextInputLayout)findViewById(R.id.outlinedTextField4);
        //inputLayoutState = (TextInputLayout) findViewById(R.id.outlinedTextField5);
        inputLayoutEnrool=(TextInputLayout)findViewById(R.id.outlinedTextField1);
        inputLayoutCollege=(TextInputLayout)findViewById(R.id.outlinedTextField2);
        inputLayoutDistrict = (TextInputLayout) findViewById(R.id.outlinedTextField6);
        inputName=(EditText)findViewById(R.id.edit_name);
        inputEnrool=(EditText)findViewById(R.id.enrool);
        inputCollege=(EditText)findViewById(R.id.college);

        inputDistrict=(EditText)findViewById(R.id.edit_district);
        update= (Button)findViewById(R.id.update);
        autoCompleteTextView_State=findViewById(R.id.State_spinner);
        autoCompleteTextView_branch=findViewById(R.id.branch_spinner);
        autoCompleteTextView=findViewById(R.id.year_spinner);
        ch=findViewById(R.id.check);
        UserProfileImageRef=FirebaseStorage.getInstance().getReference().child("Profile Image");

    }

    private int getAdaptorItemLayout() {
        return android.R.layout.simple_dropdown_item_1line;
    }
/*
    private void readData(){

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inputName.setText(dataSnapshot.child("Name").getValue(String.class));
                inputDistrict.setText(dataSnapshot.child("District").getValue(String.class));
                state=(dataSnapshot.child("State").getValue(String.class));

                //String link=(dataSnapshot.child("Link").getValue(String.class));
               // Picasso.get().load(link).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(Info_Activity.this,state,Toast.LENGTH_LONG).show();
    }

    private Uri getTempUri(){
        String dri = Environment.getExternalStorageDirectory()+File.separator+"Temp";
        File dirFile = new File(dri);
        dirFile.mkdir();
        String file = dri+File.separator+"temp.png";
        File tempFile = new File(file);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempFile);
    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                    boolean flag = true;
                    for (int i = 0 ; i < permissionsList.length ; i++ ) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            flag = false;
                        }
                    }

                    if(flag){
                        imagePicker.choosePicture(true);
                    }

                }
                return;
            }
        }
    }

    */
    /*
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            upload(resultUri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.i("dsjknjsdkn", "onActivityResult: "+cropError.getMessage());
        }
    }

*/
/*
    void upload(Uri uri)
    {
        loader.setVisibility(View.VISIBLE);
        final StorageReference riversRef =FirebaseStorage.getInstance().getReference().child("Temp/"+System.currentTimeMillis()+".png");
        riversRef.putFile(uri).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Info_Activity.this,"failed upload",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loader.setVisibility(View.GONE);
                        //imageView.setImageURI(null);
                        //Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        })
    }

*/





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
            /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                loader.setVisibility(View.VISIBLE);
                final Uri resultUri=result.getUri();


                    final StorageReference filePath=UserProfileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Info_Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl=uri.toString();
                                       // Picasso.get().load(downloadUrl).placeholder(R.drawable.profile_default).into(imageView);
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
                                                            Toast.makeText(Info_Activity.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                            loader.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });

                                    }
                                });


                            }
                            else
                                Toast.makeText(Info_Activity.this,"Error:"+task.getException().toString(),Toast.LENGTH_SHORT).show();

                        }
                    });


            }*/

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                loader.setVisibility(View.VISIBLE);
                final Uri resultUri=result.getUri();


                final StorageReference filePath=UserProfileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Info_Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl=uri.toString();

                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("ProfileImg").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Picasso.get().load(downloadUrl).into(imageView);
                                                        loader.setVisibility(View.INVISIBLE);
                                                    }
                                                    else {
                                                        Toast.makeText(Info_Activity.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        loader.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            });

                                }
                            });


                        }
                        else
                            Toast.makeText(Info_Activity.this,"Error:"+task.getException().toString(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }



}