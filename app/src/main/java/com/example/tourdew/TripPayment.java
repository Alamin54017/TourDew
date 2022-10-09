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
import android.widget.TextView;

import com.example.tourdew.model.BookModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TripPayment extends AppCompatActivity {

    ImageView bkash,nagad;
    TextView total;
    private Dialog dialog,dialog2;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    String dist,postId,price,name,phone;
    int seat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_payment);
        bkash=findViewById(R.id.imageView21);
        nagad=findViewById(R.id.imageView23);
        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");
        price=intent.getStringExtra("price");
        name=intent.getStringExtra("name");
        phone=intent.getStringExtra("phone");
        seat= Integer.parseInt(intent.getStringExtra("seat"));
        dist=intent.getStringExtra("district");
        total=findViewById(R.id.textView82);
        total.setText(price);


        //Success Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.success);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay1 = dialog.findViewById(R.id.okaybutton);

        Okay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Error Dialogue
        dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.failed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false); //Optional
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay2 = dialog2.findViewById(R.id.failedbutton);

        Okay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //Bkash
        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book();
            }
        });

        //Nagad
        nagad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book();
            }
        });


    }

    void book(){
        BookModel bookModel=new BookModel();
        bookModel.setBookedBy(fAuth.getCurrentUser().getUid());
        bookModel.setBookedName(name);
        bookModel.setPhone(phone);
        bookModel.setPrice(price);
        bookModel.setSeatsTaken(seat);
        bookModel.setPostId(postId);
        database.getReference().child("Booked").child(fAuth.getCurrentUser().getUid()).push().setValue(bookModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("Trips Post").child(dist).child(postId).child("BookingCount")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int booking=0;
                                        if (snapshot.exists()){
                                            booking=snapshot.getValue(Integer.class);
                                        }
                                        database.getReference().child("Trips Post").child(dist).child(postId).child("BookingCount")
                                                .setValue(booking+seat).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        dialog2.show();
                                    }
                                });

                        database.getReference().child("Trips Post").child(dist).child(postId).child("Payment")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        float amount=0;
                                        float pr= Float.parseFloat(price);
                                        if (snapshot.exists()){
                                            amount=snapshot.getValue(float.class);
                                        }
                                        database.getReference().child("Trips Post").child(dist).child(postId).child("Payment")
                                                .setValue(amount+pr);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });
    }
}