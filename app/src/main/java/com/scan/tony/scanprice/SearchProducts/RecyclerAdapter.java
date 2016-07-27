package com.scan.tony.scanprice.SearchProducts;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scan.tony.scanprice.R;

import java.util.ArrayList;


/**
 * Created by Tony on 09.07.2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<ProductModel> mData;
    private ProductModel model;
    private AppCompatActivity activity;

    public RecyclerAdapter(ArrayList<ProductModel> mData, AppCompatActivity activity) {
        this.activity = activity;
        this.mData = mData;
    }

    public ProductModel add(ProductModel model){
        mData.add(model);
        return model;
    }


    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

      //  String name = mData.get(position).getNameProduct();
      //  String cost = mData.get(position).getCostProduct();
      //  String unit = mData.get(position).getUnitProduct();

      //  String name =  mData[position].getNameProduct();
      //  String cost =  mData[position].getCostProduct();
      //  String unit = mData[position].getUnitProduct();


            holder.nameTextView.setText("Название: " + mData.get(position).getNameProduct());
            holder.unitTextView.setText("Единица измерения: " + mData.get(position).getUnitProduct());
            holder.costTextView.setText("Стоимость: " + mData.get(position).getCostProduct());


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameTextView;
        public TextView costTextView;
        public TextView unitTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.name_product);
            costTextView = (TextView) itemView.findViewById(R.id.cost_product);
            unitTextView = (TextView) itemView.findViewById(R.id.unit_product);

        }
    }
}