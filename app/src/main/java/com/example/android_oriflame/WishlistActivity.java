package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends NavigationActivity {

    FirebaseFirestore db;
    private ArrayList<QueryDocumentSnapshot> arrayList;
    private ArrayList<DocumentReference> docList;
    private RecyclerView product_list;
    private RecyclerView.LayoutManager layoutManager;
    private WishlistAdapter adapter;
    private List<String> wishlistProducts;
    private TextView noItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        this.page_name = "Wishlist";

        super.bootstrapNav();

        this.db = FirebaseFirestore.getInstance();
        // Offline cache
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        this.arrayList = new ArrayList<>();
        this.wishlistProducts = new ArrayList<>();
        this.docList = new ArrayList<>();
        this.noItems = findViewById(R.id.noItems);
        this.product_list = findViewById(R.id.wishlist_products);
        this.adapter = new WishlistAdapter(this.arrayList, this.docList);
        this.layoutManager = new LinearLayoutManager(this);
        this.product_list.setLayoutManager(this.layoutManager);
        this.product_list.setAdapter(this.adapter);
        this.product_list.setHasFixedSize(true);
        
        db.collection("wishlist")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Map<String, Object> data = queryDocumentSnapshot.getData();
                                docList.add(queryDocumentSnapshot.getReference());
                                wishlistProducts.add(data.get("product_id").toString());
                            }
                            if (wishlistProducts.size() > 0) {
                                getProduct();
                            } else {
                                noItems.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(WishlistActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getProduct() {
        db.collection("products")
                .whereIn(FieldPath.documentId(), wishlistProducts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getData().isEmpty()) {
                                    arrayList.add(document);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}