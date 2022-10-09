package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourdew.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    EditText email,fullname,phone,password,repassword;
    Button signup;
    FirebaseAuth fAuth;
    TextView backlogin;
    RadioGroup GenderGroup;
    RadioButton gender;
    private static final String TAG="CreateAccount";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email=findViewById(R.id.email);
        fullname=findViewById(R.id.fullname);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        signup=findViewById(R.id.signup);
        GenderGroup=findViewById(R.id.GenderGroup);
        GenderGroup.clearCheck();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail=email.getText().toString().trim();
                String mPass=password.getText().toString().trim();
                String repass=repassword.getText().toString().trim();
                String mName=fullname.getText().toString().trim();
                String mphone=phone.getText().toString().trim();
                int radioid= GenderGroup.getCheckedRadioButtonId();
                gender=findViewById(radioid);
                String textgender;

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Email is Required.");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    email.setError("Please Enter a Valid Email");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mName)){
                    fullname.setError("Full Name is Required");
                    fullname.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mphone)){
                    phone.setError("Phone Number is Required");
                    phone.requestFocus();
                    return;
                }
                if (mphone.length()!=11){
                    phone.setError("Phone Number must be 11 digits");
                    phone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    password.setError("Password is Required.");
                    password.requestFocus();
                    return;
                }
                if (mPass.length()<6){
                    password.setError("Password must be more than 6 Characters");
                    password.requestFocus();
                    return;
                }
                if (!mPass.equals(repass)){
                    repassword.setError("Password Doesn't Matched!");
                    repassword.requestFocus();
                    return;
                }
                if (GenderGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(CreateAccount.this,"Please Select Your Gender",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    textgender=gender.getText().toString();
                    registerUser(mEmail,mName,mphone,mPass,textgender);
                }
            }
        });




        backlogin=findViewById(R.id.backlogin);
        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(CreateAccount.this,Login.class);
                startActivity(i);
            }
        });
    }

    private void registerUser(String mEmail, String mName, String mphone, String mPass, String textgender) {
        fAuth=FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=fAuth.getCurrentUser();
                    Boolean agent=false;
                    Boolean guider=false;
                    UserDetails userDetails=new UserDetails(mName,mphone,textgender,agent,guider);
                    DatabaseReference rf= FirebaseDatabase.getInstance().getReference("User Data");
                    rf.child(firebaseUser.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(CreateAccount.this,"User Registered Successfully.",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(CreateAccount.this,Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(CreateAccount.this,"User Registration Failed! Please try agan.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        password.setError("Password is too weak! Use Strong Password");
                        password.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(CreateAccount.this,"Your Email is invalid or already in use! Kindly Re-enter",Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        Toast.makeText(CreateAccount.this,"User is Already Registered with this email",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(CreateAccount.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}