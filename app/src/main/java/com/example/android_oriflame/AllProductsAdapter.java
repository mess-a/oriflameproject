package com.example.android_oriflame;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ProductViewHolder> {

    private ArrayList<QueryDocumentSnapshot> list;
    private ArrayList<DocumentReference> docList;
    private Context context;
    private ArrayList<String> product_id;

    public AllProductsAdapter(ArrayList<QueryDocumentSnapshot> list, ArrayList<DocumentReference> docList, Context context) {
        this.list = list;
        this.docList = docList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.all_products_layout, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(layout);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        final Map<String, Object> data = list.get(position).getData();

        holder.name.setText(data.get("p_name").toString());
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position); // remove item from ui
                docList.get(position).delete();

                // Remove the image
                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" + docList.get(position).getId() + ".jpg");
                imageRef.delete();

                docList.remove(position);
                notifyDataSetChanged();
            }
        });

        // Calculate Discount
        final int discountRate = Integer.valueOf(data.get("p_discount").toString());
        int discountPrice = 0;
        int discount = 0;
        int price = Integer.valueOf(data.get("p_price").toString());
        if (discountRate > 0) {
            discount = Math.round((discountRate * price) / 100);
            discountPrice = price - discount;
        }

        final int finalDiscountPrice = discountPrice;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Button delete_button;

        public ProductViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }
}
