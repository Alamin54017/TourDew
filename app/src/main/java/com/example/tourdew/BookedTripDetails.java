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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourdew.model.AgentData;
import com.example.tourdew.model.PostTripData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookedTripDetails extends AppCompatActivity {

    private Dialog dialog;
    private Button ShowDialog;
    FirebaseDatabase database;
    FirebaseAuth fAuth;
    ImageView placeImg;
    TextView place,agency,rating,startDate,EndDate,price,seats,visitPlace,including,excluding,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_trip_details);

        Intent intent=getIntent();
        String postedBy=intent.getStringExtra("postedBy");
        String postId=intent.getStringExtra("postId");
        String seatTaken=intent.getStringExtra("seat");
        String amount=intent.getStringExtra("amount");
        placeImg=findViewById(R.id.imageView17);
        place=findViewById(R.id.textView22);
        agency=findViewById(R.id.textView23);
        rating=findViewById(R.id.textView24);
        startDate=findViewById(R.id.textView28);
        EndDate=findViewById(R.id.textView29);
        price=findViewById(R.id.textView27);
        seats=findViewById(R.id.textView32);
        visitPlace=findViewById(R.id.textView33);
        including=findViewById(R.id.textView35);
        excluding=findViewById(R.id.textView38);
        description=findViewById(R.id.textView40);
        database=FirebaseDatabase.getInstance();

        //Retrive Post Data
        database.getReference().child("Trips Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PostTripData postTripData= dataSnapshot.child(postId).getValue(PostTripData.class);
                    try{
                        place.setText(postTripData.getPlaceName());
                        Picasso.get().load(postTripData.getImageUrl()).placeholder(R.drawable.index).into(placeImg);
                        startDate.setText(postTripData.getStartDate());
                        EndDate.setText(postTripData.getEndDate());
                        visitPlace.setText(postTripData.getVisitplace());
                        including.setText(postTripData.getFacility());
                        excluding.setText(postTripData.getExcluded());
                        description.setText(postTripData.getDescription());
                        seats.setText(seatTaken);
                        price.setText(amount);
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Agents").child(postedBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AgentData user=snapshot.getValue(AgentData.class);
                        agency.setText(user.getAgentName());
                        rating.setText(user.getRating());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        ShowDialog = findViewById(R.id.button);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.agent_rating);
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
                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        ShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show(); // Showing the dialog here
            }
        });
    }
}