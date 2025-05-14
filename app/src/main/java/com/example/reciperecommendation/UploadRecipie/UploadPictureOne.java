package com.example.reciperecommendation.UploadRecipie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.reciperecommendation.FirebaseModel.RecipieModel;
import com.example.reciperecommendation.R;
import com.example.reciperecommendation.ViewMeal.RecipeFeedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPictureOne extends AppCompatActivity {

    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    Button Upload;
    EditText Title;
    EditText Description;
    ImageView imageView;
    Uri picUri;
    Button ViewCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipei_upload);

        ViewCommunity = findViewById(R.id.viewcommunity);
        ViewCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadPictureOne.this , RecipeFeedActivity.class));
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        Upload = findViewById(R.id.upload);
        Title = findViewById(R.id.title);
        Description = findViewById(R.id.des);

        imageView = findViewById(R.id.pic1);


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Title.getText().toString().isEmpty()) {
                    Title.setError("Field is empty");
                    return;
                }

                if (Description.getText().toString().isEmpty()) {
                    Description.setError("Field is empty");
                    return;
                }


                if (picUri == null) {
                    Toast.makeText(UploadPictureOne.this, "Select picture ", Toast.LENGTH_SHORT).show();
                    return;
                }


                storageReference.child(System.currentTimeMillis() + "." + getExtension(picUri)).putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recipe");
                                String Key = databaseReference.push().getKey();
                                RecipieModel recipieModel
                                        = new RecipieModel(FirebaseAuth.getInstance().getUid(), Key, Title.getText().toString(), Description.getText().toString(), uri.toString());
                                databaseReference.child(Key).setValue(recipieModel);
                                Toast.makeText(UploadPictureOne.this, "successfully uploaded!", Toast.LENGTH_SHORT).show();
                                Title.setText("");
                                Description.setText("");

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(UploadPictureOne.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        ProgressBar progressBar = findViewById(R.id.prg);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress((int) progress);
                    }
                });

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImageFromeGallery();
            }
        });

    }


    private void ChooseImageFromeGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }


    public String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                picUri = data.getData();

            }
            if (null != picUri) {
                ImageView im = findViewById(R.id.pic1);
                String path = getPathFromURI(picUri);
                im.setImageURI(picUri);
            }
        }
    }
}