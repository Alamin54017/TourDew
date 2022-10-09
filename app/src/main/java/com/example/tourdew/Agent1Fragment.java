package com.example.tourdew;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourdew.adapter.AgentAdapter1;
import com.example.tourdew.adapter.GuiderAdapter;
import com.example.tourdew.model.AgentModel1;
import com.example.tourdew.model.GuiderModel;
import com.example.tourdew.model.PostTripData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Agent1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Agent1Fragment extends Fragment {
    RecyclerView recentRecycler;
    AgentAdapter1 AgentAdapter1;
    ImageView addtrip,back;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Agent1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Agent1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Agent1Fragment newInstance(String param1, String param2) {
        Agent1Fragment fragment = new Agent1Fragment();
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
        View v=inflater.inflate(R.layout.fragment_agent1, container, false);

        addtrip=v.findViewById(R.id.addtrip);
        back=v.findViewById(R.id.imageView16);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),home.class);
                startActivity(intent);
            }
        });
        addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddTrip.class);
                startActivity(intent);
            }
        });


        //Show Agent Trips
        List<PostTripData> DataList=new ArrayList<>();

        recentRecycler= v.findViewById(R.id.agent1recycle);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recentRecycler.setLayoutManager(layoutManager);
        AgentAdapter1=new AgentAdapter1(getContext(),DataList);
        recentRecycler.setAdapter(AgentAdapter1);
        database.getReference().child("Trips Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(fAuth.getCurrentUser().getUid().equals(dataSnapshot1.child("postedBy").getValue())){
                            PostTripData postTripData=dataSnapshot1.getValue(PostTripData.class);
                            DataList.add(postTripData);
                        }
                    }
                }
                AgentAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}