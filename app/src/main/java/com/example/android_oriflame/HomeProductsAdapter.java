package com.example.android_oriflame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;
import java.util.Map;

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.ProductViewHolder> {

    private ArrayList<Map<String, Object>> list;
    private ArrayList<RequestBuilder<Drawable>> images;
    private ArrayList<String> product_ids;
    private Context context;

    public HomeProductsAdapter(ArrayList<Map<String, Object>> list, ArrayList<RequestBuilder<Drawable>> images, Context context, ArrayList<String> product_ids) {
        this.list = list;
        this.images = images;
        this.context = context;
        this.product_ids = product_ids;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.home_products_layout, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(layout);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        final Map<String, Object> product = list.get(position);
        RequestBuilder<Drawable> image = images.get(position);

        // Add Image
        image.into(holder.image);

        // Calculate Discount
        int discountRate = Integer.valueOf(product.get("p_discount").toString());
        int discountPrice = 0;
        int discount = 0;
        int price = Integer.valueOf(product.get("p_price").toString());
        if (discountRate > 0) {
            discount = Math.round((discountRate * price) / 100);
            discountPrice = price - discount;
        }

        final int finalDiscountPrice = discountPrice;
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, productDetail.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("p_id", product_ids.get(position));
                intent.putExtra("p_name", product.get("p_name").toString());
                intent.putExtra("p_desc", product.get("p_desc").toString());
                intent.putExtra("p_price", finalDiscountPrice);
                intent.putExtra("p_cat", product.get("p_cat").toString());
                context.getApplicationContext().startActivity(intent);
            }
        });

        // Add Text
        holder.name.setText(product.get("p_name").toString());
        holder.price.setText("Rs. " + String.valueOf(price));
        if (discountRate > 0) {
            holder.discount_price.setText("Rs. " + String.valueOf(discountPrice));
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.discount_price.setWidth(0);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll;
        TextView name, price, discount_price;
        ImageView image;

        public ProductViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);

            ll = itemView;
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.product_image);
            discount_price = itemView.findViewById(R.id.product_discount_price);
        }
    }
}
