package com.example.android_oriflame;

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

import java.util.ArrayList;
import java.util.Map;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ProductViewHolder> {

    private ArrayList<QueryDocumentSnapshot> list;
    private ArrayList<DocumentReference> docList;

    public AllProductsAdapter(ArrayList<QueryDocumentSnapshot> list, ArrayList<DocumentReference> docList) {
        this.list = list;
        this.docList = docList;
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
        Map<String, Object> data = list.get(position).getData();

        holder.name.setText(data.get("p_name").toString());
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position); // remove item from ui
                docList.get(position).delete();
                docList.remove(position);
                notifyDataSetChanged();
            }
        });
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
