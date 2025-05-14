package com.example.reciperecommendation.Management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassword extends AppCompatActivity {

    Boolean isExist ;
    Button Submit  ; 
    EditText Email ;
    DatabaseReference databaseReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    
       Email = findViewById(R.id.d_email);
       Submit = findViewById(R.id.submit) ;
       databaseReference = FirebaseDatabase.getInstance().getReference("users") ; 
       Submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               
               if(Email.getText().toString ().isEmpty()){
                   Email.setError("Field is Empty");
                   return;  
               }
               FirebaseAuth.getInstance().sendPasswordResetEmail(Email.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(ForgotPassword.this, "Reset Link is Sent", Toast.LENGTH_SHORT).show();
                               }
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(ForgotPassword.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                   }
               });
               
               
           }
       });
       
    
    }
}