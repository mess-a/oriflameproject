package com.example.android_oriflame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class productDetail extends AppCompatActivity implements View.OnClickListener {

    StorageReference storageRef;

    TextView pName, pDesc, pPrice, pCat;
    ImageView pImage, bBack;
    Button bWishlist, bContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String p_name = getIntent().getStringExtra("p_name");
        String p_desc = getIntent().getStringExtra("p_desc");
        String p_price = getIntent().getStringExtra("p_price");
        String p_cat = getIntent().getStringExtra("p_cat");
        String p_id = getIntent().getStringExtra("p_id");

        pName = findViewById(R.id.product_name);
        pPrice = findViewById(R.id.product_price);
        pDesc = findViewById(R.id.product_description);
        pCat = findViewById(R.id.product_cat);
        pImage = findViewById(R.id.product_image);

        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = this.storageRef.child("images/" + p_id + ".jpg");

        // Text
        pName.setText(p_name);
        pPrice.setText(p_price);
        pDesc.setText(p_desc);
        pCat.setText(p_cat);

        // Image
        Glide.with(this).load(imageRef).into(pImage);

        // Buttons
        bWishlist = findViewById(R.id.wishlistButton);
        bBack = findViewById(R.id.backButton);
        bContact = findViewById(R.id.contactDealerButton);

        bWishlist.setOnClickListener(this);
        bBack.setOnClickListener(this);
        bContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bWishlist) {
            Log.d("buttonLog", "onClick: " + "wishlist");
        } else if (v == bContact) {
            Log.d("buttonLog", "onClick: " + "contact");
        } else if (v == bBack) {
            finish();
        }
    }
}