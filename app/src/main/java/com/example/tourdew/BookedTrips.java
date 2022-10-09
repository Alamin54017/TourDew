package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tourdew.adapter.BookedAdapter;
import com.example.tourdew.adapter.SearchAdapter;
import com.example.tourdew.model.BookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookedTrips extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseDatabase database;
    RecyclerView bookedlistShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_trips);

        bookedlistShow=findViewById(R.id.bookedTripShow);
        database=FirebaseDatabase.getInstance();
        fAuth=FirebaseAuth.getInstance();

        List<BookModel> BookedList=new ArrayList<>();
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        bookedlistShow.setLayoutManager(layoutManager);
        BookedAdapter bookedAdapter =new BookedAdapter(this,BookedList);
        bookedlistShow.setAdapter(bookedAdapter);

        database.getReference().child("Booked").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    BookModel bookModel= dataSnapshot.getValue(BookModel.class);
                    BookedList.add(bookModel);
                }
                bookedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}