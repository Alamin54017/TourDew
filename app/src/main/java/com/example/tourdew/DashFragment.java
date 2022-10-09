package com.example.tourdew;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tourdew.model.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashFragment extends Fragment {
    LinearLayout agent,wallet,newsfeed,profile,guide,logout;
    FirebaseAuth fAuth;
    TextView name,balance;
    ImageView proPic;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public DashFragment() {
    }

    public static DashFragment newInstance(String param1, String param2) {
        DashFragment fragment = new DashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_dash, container, false);

        fAuth=FirebaseAuth.getInstance();
        name=v.findViewById(R.id.textView85);
        balance=v.findViewById(R.id.textView9);
        proPic=v.findViewById(R.id.imageView13);

        FirebaseDatabase.getInstance().getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserDetails user=snapshot.getValue(UserDetails.class);
                    name.setText(user.getName());
                    balance.setText(user.getBalance());
                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_male_user_96).into(proPic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        logout=v.findViewById(R.id.logout);
        wallet=v.findViewById(R.id.wallet);
        agent=v.findViewById(R.id.agency);
        newsfeed=v.findViewById(R.id.newsfeed);
        profile=v.findViewById(R.id.profile);
        guide=v.findViewById(R.id.guider);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                Toast.makeText(getActivity(),"You are Logged Out",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),GuideVerify.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),Profile.class);
                startActivity(i);
            }
        });
        newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),NewsFeed.class);
                startActivity(i);
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),Wallet.class);
                startActivity(i);
            }
        });
        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AgentVerify.class);
                startActivity(intent);
            }
        });
        return v;
    }
}