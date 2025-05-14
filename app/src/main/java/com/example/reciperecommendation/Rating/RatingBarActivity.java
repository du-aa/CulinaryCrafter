package com.example.reciperecommendation.Rating;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.Management.UserModelClass;
import com.example.reciperecommendation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RatingBarActivity extends AppCompatActivity {

    List<RatingLaborModelClass> ratingLaborModelClasses ;
    private int Year_x, Month_x, Dat_x;

    double  TotalRating ;
    int counter ;
    FirebaseAuth mAuth ;
    TextView LogName ;
    EditText Comment ;
    RatingBar ratingBar ;
    DatabaseReference RatingLabor ;
    Button SubRating ;
    ListView listView ;
    String DateStr  = "";
    String LoggerName ;



    public static final int DIALOG_ID = 0;

    ImageView CalanderView  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);


        Calendar calendar = Calendar.getInstance();
        Year_x = calendar.get(calendar.YEAR);
        Month_x = calendar.get(calendar.MONTH);
        Dat_x = calendar.get(calendar.DAY_OF_MONTH);




        mAuth = FirebaseAuth.getInstance();

        LogName = findViewById(R.id.logname);
    listView = findViewById(R.id.ratinglistview);
    Comment = findViewById(R.id.comment);
    ratingBar = findViewById(R.id.ratingbar);
    SubRating  = findViewById(R.id.submitrating);
        (CalanderView  = findViewById(R.id.calenderr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    ratingLaborModelClasses = new ArrayList<>();
    RatingLabor = FirebaseDatabase.getInstance().getReference("itemrating").child(getIntent().getStringExtra("itemkey"));
    SubRating.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String CommentUSer = Comment.getText().toString().trim();
                float rat = ratingBar.getRating() ;
                if(CommentUSer.isEmpty()){
                    Comment.setError("This field is Empty");
                    return;
                }
            if(LoggerName.isEmpty()){
                LogName.setError("This field is Empty");
                return;
             }

            if (DateStr.isEmpty()){
                Toast.makeText(RatingBarActivity.this, "Pleause Select Date ", Toast.LENGTH_SHORT).show();
                return;

            }

            try {
                String Ratkey = RatingLabor.push().getKey();
                RatingLaborModelClass ratingLaborModelClass = new RatingLaborModelClass(
                        LoggerName, String.valueOf(rat), CommentUSer, Ratkey, DateStr
                );
                RatingLabor.child(mAuth.getUid()).setValue(ratingLaborModelClass);
            }catch (Exception e){
              //  Toast.makeText(RatingBarActivityLabors.this, "", Toast.LENGTH_SHORT).show();
            }
        }
    });
    RatingLabor.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ratingLaborModelClasses.clear();
            counter =0 ;
            TotalRating = 0.0f ;
            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
           RatingLaborModelClass ratingLaborModelClass = dataSnapshot1.getValue(RatingLaborModelClass.class);
            ratingLaborModelClasses.add(ratingLaborModelClass);

            counter ++ ;
            TotalRating = TotalRating + Double.valueOf(ratingLaborModelClass.getRating());
       }
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ItemDatabase").child(getIntent().getStringExtra("itemkey"));
            mDatabase.child("rating").setValue(String.valueOf(TotalRating));
            RatingAdapter ratingBarLaborAdapter = new RatingAdapter(RatingBarActivity.this,
            ratingLaborModelClasses);
            listView.setAdapter(ratingBarLaborAdapter);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, Year_x, Month_x, Dat_x);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month , int dayOfMonth) {
                DateStr = dayOfMonth + " " + (month+1) + " " + year;
            Toast.makeText(RatingBarActivity.this, "d" + DateStr, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users1");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                UserModelClass userModelClass =
                        dataSnapshot.getValue(UserModelClass.class);
                if(userModelClass.getUUID().equals(FirebaseAuth.getInstance()
                        .getUid())){
                    LogName.setText(userModelClass.getUserName());
                    LoggerName = userModelClass.getUserName();

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}
