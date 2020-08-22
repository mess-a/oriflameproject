package com.example.android_oriflame;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProductsActivity extends NavigationActivity {

    FirebaseFirestore db;
    private ArrayList<QueryDocumentSnapshot> arrayList;
    private ArrayList<DocumentReference> docList;
    private RecyclerView product_list;
    private RecyclerView.LayoutManager layoutManager;
    private AllProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        this.page_name = "All Products";

        super.bootstrapNav();

        this.db = FirebaseFirestore.getInstance();
        this.arrayList = new ArrayList<>();
        this.docList = new ArrayList<>();
        this.product_list = findViewById(R.id.products_list);
        this.adapter = new AllProductsAdapter(this.arrayList, this.docList);
        this.layoutManager = new LinearLayoutManager(this);
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
                                docList.add(document.getReference());
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