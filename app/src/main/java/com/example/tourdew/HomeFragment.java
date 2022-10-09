package com.example.tourdew;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tourdew.adapter.homeadapter;
import com.example.tourdew.model.PostTripData;
import com.example.tourdew.model.homemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    RecyclerView recentRecycler;
    homeadapter homeadapter;
    EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    Spinner place;
    Button search;
    ProgressBar progressBar;
    ImageView chat;
    String[] district={"Select Place","Bagerhat","Bandarban","Barguna","Barisal","Bhola","Bogra","Brahmanbaria","Chandpur","Chittagong","Chuadanga",
            "Comilla","Cox's Bazar","Dhaka","Dinajpur","Faridpur","Feni","Gaibandha","Gazipur","Gopalganj","Habiganj",
            "Jaipurhat","Jamalpur","Jessore","Jhalakati","Jhenaidah","Khagrachari","Khulna","Kishoreganj","Kurigram","Kushtia",
            "Lakshmipur","Lalmonirhat","Madaripur","Magura","Manikganj","Meherpur","Moulvibazar","Munshiganj","Mymensingh",
            "Naogaon","Narail","Narayanganj","Narsingdi","Natore","Nawabganj","Netrakona","Nilphamari","Noakhali","Pabna",
            "Panchagarh","Patuakhali","Pirojpur","Rajbari","Rajshahi","Rangpur","Satkhira","Sariatpur","Sherpur","Sirajganj",
            "Sunamganj","Sylhet","Tangail","Thakurgaon"};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        progressBar=view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        fAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        search=view.findViewById(R.id.search);
        chat=view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Chatting.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dist=place.getSelectedItem().toString();
                if(dist.compareTo("Select Place")!=0 && etDate.getText().toString()!=null){
                    Intent intent=new Intent(getActivity(),Search.class);
                    intent.putExtra("placeName",dist);
                    intent.putExtra("Date",etDate.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Please Select a Place and Date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        place= view.findViewById(R.id.places);
        place.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, district);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place.setAdapter(dataAdapter);

        etDate= view.findViewById(R.id.dateselecter);
        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date= day+"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        //Slider Data
        List<PostTripData> recentdatalist=new ArrayList<>();
        recentRecycler= view.findViewById(R.id.recent_recyle);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recentRecycler.setLayoutManager(layoutManager);
        homeadapter=new homeadapter(getContext(),recentdatalist);
        recentRecycler.setAdapter(homeadapter);
        database.getReference().child("Trips Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        PostTripData postTripData=dataSnapshot1.getValue(PostTripData.class);
                        postTripData.setPostId(dataSnapshot1.getKey());
                        recentdatalist.add(postTripData);
                        progressBar.setVisibility(View.GONE);
                    }
                }
                homeadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}