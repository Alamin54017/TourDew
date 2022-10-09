package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourdew.model.Guide;
import com.example.tourdew.model.GuideRequest;
import com.example.tourdew.model.UserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GuideDetails extends AppCompatActivity {
    ImageView profileImg;
    TextView name,tripcom,guidefor,description,price;
    RatingBar ratingBar;
    private Dialog dialog;
    Button hire;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    DatabaseReference root=FirebaseDatabase.getInstance().getReference().child("Guider Request");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_details);
        Intent intent=getIntent();
        String guideId=intent.getStringExtra("GuideId");
        profileImg=findViewById(R.id.circleImageView);
        name=findViewById(R.id.textView37);
        ratingBar=findViewById(R.id.ratingBar);
        tripcom=findViewById(R.id.textView43);
        price=findViewById(R.id.textView90);
        guidefor=findViewById(R.id.textView45);
        description=findViewById(R.id.textView47);

        database.getReference().child("User Data").child(guideId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails=snapshot.getValue(UserDetails.class);
                name.setText(userDetails.getName());
                Picasso.get().load(userDetails.getProfilePic()).placeholder(R.drawable.male_icon).into(profileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Guiders").child(guideId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Guide guide=snapshot.getValue(Guide.class);
                    ratingBar.setRating(guide.getRating());
                    tripcom.setText(guide.getTripCompleted()+"");
                    price.setText(guide.getPrice()+"");
                    guidefor.setText(guide.getGuideFor());
                    description.setText(guide.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Bidding Dialog
        hire = findViewById(R.id.button);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.bidding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay = dialog.findViewById(R.id.submit);
        TextView Cancel = dialog.findViewById(R.id.close);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText date,place,price;
                date=dialog.findViewById(R.id.date);
                place=dialog.findViewById(R.id.place);
                price=dialog.findViewById(R.id.price);

                GuideRequest guideRequest=new GuideRequest();
                guideRequest.setDate(date.getText().toString());
                guideRequest.setGuideId(guideId);
                guideRequest.setSenderId(fAuth.getCurrentUser().getUid());
                guideRequest.setPlace(place.getText().toString());
                guideRequest.setPrice(Integer.parseInt(price.getText().toString()));
                String modelId = root.push().getKey();
                guideRequest.setId(modelId);
                root.child(modelId).setValue(guideRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GuideDetails.this,"Request Sent Successfully",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent1=new Intent(GuideDetails.this,home.class);
                        startActivity(intent1);

                    }
                });

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
}