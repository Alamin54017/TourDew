package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class TripActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth fAuth;
    ImageView placeImg;
    TextView place,agency,rating,startDate,EndDate,price,seats,visitPlace,including,excluding,description;
    Button booktrip;
    String dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Intent intent=getIntent();
        String postId=intent.getStringExtra("postId");
        String postedBy=intent.getStringExtra("postedBy");
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
                        price.setText(postTripData.getPrice());
                        visitPlace.setText(postTripData.getVisitplace());
                        including.setText(postTripData.getFacility());
                        excluding.setText(postTripData.getExcluded());
                        description.setText(postTripData.getDescription());
                        dist=postTripData.getDistrict();
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





        //Booking Button
        booktrip=findViewById(R.id.booktrip);
        booktrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TripActivity.this,BookTrip.class);
                intent.putExtra("postId",postId);
                intent.putExtra("price",price.getText().toString());
                intent.putExtra("district",dist);
                startActivity(intent);
            }
        });
    }
}