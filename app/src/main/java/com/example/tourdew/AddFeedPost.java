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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourdew.model.FeedModel;
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
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddFeedPost extends AppCompatActivity {
    ImageView chooseImage,image;
    Button post;
    EditText status;
    Uri imageUri;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Feed Post");
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    ImageView profileImage;
    TextView pName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed_post);
        chooseImage=findViewById(R.id.imageView28);
        image=findViewById(R.id.imageView29);
        post=findViewById(R.id.post);
        status=findViewById(R.id.editTextTextPersonName);
        profileImage=findViewById(R.id.profileImage);
        pName=findViewById(R.id.pName);
        database.getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image=snapshot.child("imageUrl").getValue(String.class);
                String name=snapshot.child("name").getValue(String.class);
                pName.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.ic_male_user_96).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postBody=status.getText().toString();
                uploadToFirebase(imageUri,postBody);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            image.setImageURI(imageUri);
            image.setVisibility(View.VISIBLE);

        }
    }

    private void uploadToFirebase(Uri uri,String postBody) {
        final StorageReference fileRef = reference.child("NewsFeed/").child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String UserID =fAuth.getCurrentUser().getUid().toString();
                        long time=new Date().getTime();
                        FeedModel model=new FeedModel();
                        model.setPostImg(uri.toString());
                        model.setCommentCount(0);
                        model.setLikesCount(0);
                        model.setPostedAt(time);
                        model.setStatus(postBody);
                        model.setPostedBy(UserID);
                        String modelId = root.push().getKey();
                        model.setPostId(modelId);
                        root.child(modelId).setValue(model);
                        Toast.makeText(AddFeedPost.this, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AddFeedPost.this,NewsFeed.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddFeedPost.this, "Failed to Post !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}