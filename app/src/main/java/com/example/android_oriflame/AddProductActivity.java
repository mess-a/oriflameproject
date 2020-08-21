package com.example.android_oriflame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddProductActivity extends NavigationActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 234;
    private EditText pName, pDesc, pPrice, pDiscount;
    private Button bImage, bUpload;
    private Uri filePath;
    private StorageReference storageRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        this.page_name = "Add Product";

        this.pName = findViewById(R.id.product_name);
        this.pDesc = findViewById(R.id.product_description);
        this.pPrice = findViewById(R.id.product_price);
        this.pDiscount = findViewById(R.id.product_discount);
        this.bImage = findViewById(R.id.image_select_button);
        this.bUpload = findViewById(R.id.upload_button);
        this.progressBar = findViewById(R.id.upload_progress);

        super.bootstrapNav();

        this.bImage.setOnClickListener(this);
        this.bUpload.setOnClickListener(this);

        this.storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == this.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.filePath = data.getData();
        }
    }

    private void uploadFile() {
        StorageReference imageRef = storageRef.child("images/" + this.pName.getText().toString().toLowerCase() + ".jpg");

        imageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Unable to upload image, try again!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setMax(100);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        Log.d("progress", "onProgress: " + progress);
                        progressBar.setProgress((int)progress, true);
                    }
                });
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), this.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if (v == this.bImage) {
            this.showImageChooser();
        } else if (v == this.bUpload) {
            this.uploadFile();
        }
    }
}