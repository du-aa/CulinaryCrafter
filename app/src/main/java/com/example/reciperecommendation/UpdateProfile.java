package com.example.reciperecommendation;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.Management.UserModelClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    Uri resulturi;
    Uri newpicUri;
    final int PICK_IMAGE_REQUEST = 1;
    String UserName = "";
    String Password = "";
    String Emails = "";
    EditText food_allergies ;
    EditText dietary_preferences ;
    String Phone = "";
    String Lattitude = "";
    String Longitude = "";
    String picUri = "";
    String Cnic = "";
    String status = "";
    String Skills = "";
    String City = "";
    String Age, Price;

    DatabaseReference UserFirebaseDatabasae;
    Button Updatebtn, Deletebtn;

    ImageView circularImageView;
    TextView Updateusernametextview, UpdateEmailtextvieew, UpdatetCity, Skillstextview;
    ImageView imageView;
    EditText Infousername, InfoEmail, InfoPhone, Infocity, InfoPassword, Infocnic, Infoage, InfoPrice, InfoStatus;
    UserModelClass[] UserModel;
    StorageReference UserStorageDatabase;
    ;
    String Rating = "";
    String Keys;

    ImageView UpdateLocationButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofileupdation_layout);
        UpdateLocationButton = findViewById(R.id.updatelocationpostwork);
        UserStorageDatabase = FirebaseStorage.getInstance().getReference("uploads");
        UserFirebaseDatabasae = FirebaseDatabase.getInstance().getReference("users1");
        circularImageView = findViewById(R.id.upimgbtn);
        Updateusernametextview = findViewById(R.id.upusernamebtn);
        UpdateEmailtextvieew = findViewById(R.id.upemailbtn);
        UpdatetCity = findViewById(R.id.upcity);
        Skillstextview = findViewById(R.id.upskill);
        imageView = findViewById(R.id.editcontrolpost);
        Infousername = findViewById(R.id.infousername);
        InfoEmail = findViewById(R.id.infoemail);
        InfoPhone = findViewById(R.id.infophone);
        food_allergies = findViewById(R.id.food_allergies);
        dietary_preferences = findViewById(R.id.dietary_preferences);
        Infocity = findViewById(R.id.infocity);
        InfoPassword = findViewById(R.id.infopassword);
        Infocnic = findViewById(R.id.infocnic);
        Infoage = findViewById(R.id.infoage);
        InfoPrice = findViewById(R.id.infoprice);
        InfoStatus = findViewById(R.id.infostatus);
        UserModel = new UserModelClass[1];
        mAuth = FirebaseAuth.getInstance();
        Keys = mAuth.getUid();
        UserFirebaseDatabasae.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserModelClass usersModelClass = dataSnapshot1.getValue(UserModelClass.class);
                    if (mAuth.getUid().equals(usersModelClass.getUUID())) {
                        UserName = usersModelClass.getUserName();
                        Password = usersModelClass.getPassword();
                        Emails = usersModelClass.getEmails();
                        Phone = usersModelClass.getPhone();
                        Lattitude = usersModelClass.getLattitude();
                        Longitude = usersModelClass.getLongitude();
                        picUri = usersModelClass.getProfilePhoto();
                        Cnic = usersModelClass.getCnic();
                        Skills = usersModelClass.getUserType();
                        dietary_preferences.setText(usersModelClass.getDietary_preferences());
                        food_allergies.setText(usersModelClass.getFood_allergies());
                        setData(UserName, Password, Emails, Phone, Lattitude, Longitude, picUri, Cnic, "status",
                                Skills, City, "Age", "Price");
                    }
                }
                circularImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Infousername.setEnabled(true);
                InfoEmail.setEnabled(false);

                InfoPhone.setEnabled(true);
                Infocity.setEnabled(true);
                InfoPassword.setEnabled(true);
                Infocnic.setEnabled(true);
                Infoage.setEnabled(true);
                InfoPrice.setEnabled(true);
                InfoStatus.setEnabled(true);
            }
        });
        // Update and Detele Account Button //
        Updatebtn = findViewById(R.id.upcontract);
        Deletebtn = findViewById(R.id.Deletepost);
        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resulturi == null) {
                    UserName = Infousername.getText().toString().trim();
                    Password = InfoPassword.getText().toString().trim();
                    Emails = InfoEmail.getText().toString().trim();
                    Phone = InfoPhone.getText().toString().trim();
                    Cnic = Infocnic.getText().toString().trim();
                    status = InfoStatus.getText().toString();
                    City = Infocnic.getText().toString().trim();
                    Price = InfoPrice.getText().toString().trim();
                    Age = Infoage.getText().toString().trim();

                    if (Lattitude.isEmpty() || Longitude.isEmpty()) {
                        Toast.makeText(UpdateProfile.this, "Connecting", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // update cons
                    UserModelClass userModelClass = new UserModelClass(
                            mAuth.getUid(), UserName, Emails, Password, Cnic, picUri, Phone, "User", Lattitude, Longitude, "true", "true", Rating ,dietary_preferences.getText().toString() ,food_allergies.getText().toString() );
                    UserFirebaseDatabasae.child(Keys).setValue(userModelClass);
                    Toast.makeText(UpdateProfile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }

    private void PictureUpdate() {
        try {
            UserStorageDatabase.child(System.currentTimeMillis() + "." + getExtension(newpicUri)).putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 500);
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // update cons
                            UserModelClass userModelClass = new UserModelClass(
                                    mAuth.getUid(), UserName, Emails, Password, Cnic, picUri, Phone, Lattitude, Longitude, "Consumer", "true", "false", Rating , dietary_preferences.getText().toString() , food_allergies.getText().toString());
                            UserFirebaseDatabasae.child(Keys).setValue(userModelClass);
                            Toast.makeText(UpdateProfile.this, "Successfull Pic", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                }
            });

        } catch (Exception e) {
            Log.d(e.toString(), "Plz Select Pic");
            Toast.makeText(getApplicationContext(), "Plz Select Pic ", Toast.LENGTH_LONG).show();

        }
    }

    private void setData(String userName, String password, String emails, String phone, String lattitude, String longitude, String picUri, String cnic, String status, String skills, String city, String age, String price) {
        Infousername.setText(userName);
        InfoPassword.setText(password);
        InfoEmail.setText(emails);
        InfoPhone.setText(phone);
        Infocnic.setText(cnic);
        InfoStatus.setText(status);
        Skillstextview.setText(skills);
        Infoage.setText(age);
        InfoPrice.setText(price);
        Infocity.setText("User");
        Updateusernametextview.setText(userName);
        UpdateEmailtextvieew.setText(emails);
        UpdatetCity.setText(city);
        Picasso.get().load(picUri).fit().centerCrop().into(circularImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference UserFirebaseDatabasae = FirebaseDatabase.getInstance().getReference("users");
        UserFirebaseDatabasae.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    UserModelClass usersModelClass = dataSnapshot1.getValue(UserModelClass.class);
                    if (mAuth.getUid().equals(usersModelClass.getUUID())) {
                        UserName = usersModelClass.getUserName();
                        Password = usersModelClass.getPassword();
                        Emails = usersModelClass.getEmails();
                        Phone = usersModelClass.getPhone();
                        Lattitude = usersModelClass.getLattitude();
                        Longitude = usersModelClass.getLongitude();
                        picUri = usersModelClass.getProfilePhoto();
                        Cnic = usersModelClass.getCnic();
                        Skills = usersModelClass.getUserType();
                        setData(UserName, Password, Emails, Phone, Lattitude, Longitude, picUri, Cnic, "status",
                                Skills, City, "Age", "Price");
                    }
                }
                circularImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void openFileChooser() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Toast.makeText(getApplicationContext(), "Call", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                newpicUri = data.getData();
            }
            if (null != picUri) {
                ImageView im = findViewById(R.id.upimgbtn);
                String path = getPathFromURI(newpicUri);

                PictureUpdate();
            }

        }
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

}

