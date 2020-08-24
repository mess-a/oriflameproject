package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class productDetail extends AppCompatActivity implements View.OnClickListener {

    StorageReference storageRef;
    FirebaseFirestore db;

    TextView pName, pDesc, pPrice, pCat;
    ImageView pImage, bBack;
    Button bWishlist, bContact, bGoWishlist;
    String p_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String p_name = getIntent().getStringExtra("p_name");
        String p_desc = getIntent().getStringExtra("p_desc");
        String p_price = getIntent().getStringExtra("p_price");
        String p_cat = getIntent().getStringExtra("p_cat");
        p_id = getIntent().getStringExtra("p_id");

        pName = findViewById(R.id.product_name);
        pPrice = findViewById(R.id.product_price);
        pDesc = findViewById(R.id.product_description);
        pCat = findViewById(R.id.product_cat);
        pImage = findViewById(R.id.product_image);

        db = FirebaseFirestore.getInstance();
        // Offline cache
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = this.storageRef.child("images/" + p_id + ".jpg");

        // Text
        pName.setText(p_name);
        pPrice.setText("Rs. " + p_price);
        pDesc.setText(p_desc);
        pCat.setText(p_cat);

        // Image
        Glide.with(this).load(imageRef).into(pImage);

        // Buttons
        bWishlist = findViewById(R.id.wishlistButton);
        bBack = findViewById(R.id.backButton);
        bGoWishlist = findViewById(R.id.goToWishlistButton);
        bContact = findViewById(R.id.contactDealerButton);

        bWishlist.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bContact.setOnClickListener(this);
        bGoWishlist.setOnClickListener(this);
        
        db.collection("wishlist")
                .whereEqualTo("product_id", p_id)
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                bWishlist.setVisibility(View.GONE);
                                bGoWishlist.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(productDetail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == bWishlist) {
            addToWishlist();
        } else if (v == bContact) {
            Log.d("buttonLog", "onClick: " + "contact");
        } else if (v == bBack) {
            finish();
        } else if (v == bGoWishlist) {
            startActivity(new Intent(getApplicationContext(), WishlistActivity.class));
            finish();
        }
    }

    private void addToWishlist() {
        Map<String, Object> wishlist_item = new HashMap<>();
        wishlist_item.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        wishlist_item.put("product_id", p_id);

        db.collection("wishlist")
                .add(wishlist_item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(productDetail.this, "Added to wishlist!", Toast.LENGTH_SHORT).show();
                        bWishlist.setVisibility(View.GONE);
                        bGoWishlist.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(productDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}