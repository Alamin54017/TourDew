package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tourdew.model.PostTripData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

public class AddTrip extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText placeName,startDate,EndDate,totalSeats,price,visitplace,facility,excluded,description;
    Button chooseImage,publish;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Trips Post");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    Uri imageUri;
    Spinner place;
    String[] district={"Select Place","Bagerhat","Bandarban","Barguna","Barisal","Bhola","Bogra","Brahmanbaria","Chandpur","Chittagong","Chuadanga",
            "Comilla","Cox's Bazar","Dhaka","Dinajpur","Faridpur","Feni","Gaibandha","Gazipur","Gopalganj","Habiganj",
            "Jaipurhat","Jamalpur","Jessore","Jhalakati","Jhenaidah","Khagrachari","Khulna","Kishoreganj","Kurigram","Kushtia",
            "Lakshmipur","Lalmonirhat","Madaripur","Magura","Manikganj","Meherpur","Moulvibazar","Munshiganj","Mymensingh",
            "Naogaon","Narail","Narayanganj","Narsingdi","Natore","Nawabganj","Netrakona","Nilphamari","Noakhali","Pabna",
            "Panchagarh","Patuakhali","Pirojpur","Rajbari","Rajshahi","Rangpur","Satkhira","Sariatpur","Sherpur","Sirajganj",
            "Sunamganj","Sylhet","Tangail","Thakurgaon"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        place= findViewById(R.id.district);
        place.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, district);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place.setAdapter(dataAdapter);


        placeName=findViewById(R.id.place_Name);
        startDate=findViewById(R.id.Startdate);
        EndDate=findViewById(R.id.EndDate);
        totalSeats=findViewById(R.id.totalSeats);
        price=findViewById(R.id.Pprice);
        visitplace=findViewById(R.id.visitplace);
        facility=findViewById(R.id.facility);
        excluded=findViewById(R.id.excluding);
        description=findViewById(R.id.description);
        chooseImage=findViewById(R.id.choose_image);
        publish=findViewById(R.id.publish);
        place=findViewById(R.id.district);

        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date= day+"/"+month+"/"+year;
                        startDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AddTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        String date= day+"/"+month+"/"+year;
                        EndDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+1000);
                datePickerDialog.show();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PlaceName=placeName.getText().toString().trim();
                String StartDate=startDate.getText().toString().trim();
                String endDate=EndDate.getText().toString().trim();
                String Seats=totalSeats.getText().toString().trim();
                String Price=price.getText().toString().trim();
                String Visitplace=visitplace.getText().toString().trim();
                String Facility=facility.getText().toString().trim();
                String Excluded=excluded.getText().toString().trim();
                String Description=description.getText().toString().trim();
                if (imageUri != null){
                    uploadToFirebase(imageUri,PlaceName,StartDate,endDate,Seats,Price,Visitplace,Facility,Excluded,Description);
                }else{
                    Toast.makeText(AddTrip.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            chooseImage.setText("Image Selected");

        }
    }

    private void uploadToFirebase(Uri uri, String placeName, String startDate, String endDate, String totalSeats, String price, String visitplace, String facility, String excluded, String description) {
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String UserID =fAuth.getCurrentUser().getUid().toString();
                        String dist=place.getSelectedItem().toString();
                        long time=new Date().getTime();
                        PostTripData model=new PostTripData(UserID,placeName,dist,startDate,endDate,totalSeats,price,visitplace,facility,excluded,description,uri.toString(),time);
                        String modelId = root.push().getKey();
                        root.child(dist).child(modelId).setValue(model);
                        Toast.makeText(AddTrip.this, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTrip.this, "Failed to Post !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}