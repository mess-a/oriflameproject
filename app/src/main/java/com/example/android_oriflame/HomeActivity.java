package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends NavigationActivity {

    FirebaseFirestore db;
    StorageReference storageRef;
    private ArrayList<Map<String, Object>> list;
    private ArrayList<RequestBuilder<Drawable>> images;
    private ArrayList<String> product_ids;
    private RecyclerView product_list;
    private RecyclerView.LayoutManager layoutManager;
    private HomeProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        super.bootstrapNav();

        this.db = FirebaseFirestore.getInstance();
        // offline cache
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.list = new ArrayList<>();
        this.images = new ArrayList<>();
        this.product_ids = new ArrayList<>();

        this.product_list = findViewById(R.id.home_products);
        this.adapter = new HomeProductsAdapter(this.list, this.images, getApplicationContext(), this.product_ids);
        this.layoutManager = new GridLayoutManager(this, 2);
        this.product_list.setLayoutManager(this.layoutManager);
        this.product_list.setAdapter(this.adapter);
        this.product_list.setHasFixedSize(true);

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getData().isEmpty()) {
                                    list.add(document.getData());
                                    images.add(getImage(document.getId()));
                                    product_ids.add(document.getId());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private RequestBuilder<Drawable> getImage(final String pid) {
        StorageReference imageRef = this.storageRef.child("images/" + pid + ".jpg");
        return Glide.with(this).load(imageRef);
    }
}