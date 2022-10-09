package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tourdew.adapter.ChatAdapter;
import com.example.tourdew.model.Chatlist;
import com.example.tourdew.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class Chatting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<UserDetails> mUsers;
    FirebaseAuth fuser;
    FirebaseDatabase reference;
    private List<Chatlist> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fuser=FirebaseAuth.getInstance();
        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance();
        reference.getReference("Chatlist").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()){
                    Chatlist chatlist = datasnapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance();
        reference.getReference("User Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserDetails user = snapshot.getValue(UserDetails.class);
                    user.setUserId(snapshot.getKey());
                    for (Chatlist chatlist : usersList){
                        if (snapshot.getKey().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                ChatAdapter chatAdapter = new ChatAdapter(Chatting.this, mUsers, true);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}