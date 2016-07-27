package com.scan.tony.scanprice.BasketProducts;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scan.tony.scanprice.R;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> productList;

    public ProductAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productUnit, productPrice;

        public ViewHolder(CardView cv) {
            super(cv);

            productName = (TextView) cv.findViewById(R.id.text_name);
            productUnit = (TextView) cv.findViewById(R.id.text_unit);
            productPrice = (TextView) cv.findViewById(R.id.text_price);

        }
    }


    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.productName.setText("Название: "+product.getName());
        holder.productUnit.setText("Ед.измерения: " + product.getUnit());
        holder.productPrice.setText("Стоимость: " + product.getPrice());

        Log.d("holder", "create holder: " + position);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
