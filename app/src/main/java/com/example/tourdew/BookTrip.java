package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tourdew.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookTrip extends AppCompatActivity {

    EditText name,phone,seat;
    FirebaseAuth fAuth;
    Button payment;
    String dist;
    Double mseat= Double.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);

        Intent intent=getIntent();
        String postId=intent.getStringExtra("postId");
        Double price= Double.valueOf(intent.getStringExtra("price"));
        dist=intent.getStringExtra("district");
        fAuth=FirebaseAuth.getInstance();
        name=findViewById(R.id.bookername);
        phone=findViewById(R.id.bookerphone);
        seat=findViewById(R.id.seat);

        FirebaseDatabase.getInstance().getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserDetails user=snapshot.getValue(UserDetails.class);
                    name.setText(user.getName());
                    phone.setText(user.getPhone());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        payment=findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mseat= Double.valueOf(seat.getText().toString());
                mseat=price*mseat;
                Intent intent=new Intent(BookTrip.this,TripPayment.class);
                intent.putExtra("postId",postId);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("phone",phone.getText().toString());
                intent.putExtra("price",mseat.toString());
                intent.putExtra("seat",seat.getText().toString());
                intent.putExtra("district",dist);
                startActivity(intent);
            }
        });
    }
}