package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourdew.model.Guide;
import com.example.tourdew.model.PostTripData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class GuideVerify extends AppCompatActivity {
    Button nidimg,submit;
    EditText guidefor,description,startPrice;
    Uri imageUri;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Guiders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_verify);

        nidimg=findViewById(R.id.choose_image);
        guidefor=findViewById(R.id.guidefor);
        submit=findViewById(R.id.submit);
        description=findViewById(R.id.description);
        startPrice=findViewById(R.id.startPrice);

        nidimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guideplace=guidefor.getText().toString();
                String Description=description.getText().toString().trim();
                int price= Integer.parseInt(startPrice.getText().toString());
                int tripcom=0;
                int balance=0;
                if (imageUri != null){
                    uploadToFirebase(imageUri,guideplace,Description,tripcom,balance,price);
                    database.getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).child("guiderVerified").setValue(true);
                }else{
                    Toast.makeText(GuideVerify.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            nidimg.setText("Image Selected");

        }
    }

    private void uploadToFirebase(Uri uri, String guideplace,String description,int tripcom,int balance,int price) {
        final StorageReference fileRef = reference.child("AgentNID/").child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String UserID =fAuth.getCurrentUser().getUid().toString();
                        Guide model=new Guide();
                        model.setDescription(description);
                        model.setBalance(0);
                        model.setGuideFor(guideplace);
                        model.setGuideNID(uri.toString());
                        model.setTripCompleted(tripcom);
                        model.setGuideId(UserID);
                        model.setRating((float) 5.00);
                        model.setPrice(price);
                        root.child(UserID).setValue(model);
                        Toast.makeText(GuideVerify.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GuideVerify.this, "Failed to Upload !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean agent= (Boolean) snapshot.child("guiderVerified").getValue();
                if (agent==true){
                    Intent intent=new Intent(GuideVerify.this,GuiderMenu.class);
                    startActivity(intent);
                    finish();
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}