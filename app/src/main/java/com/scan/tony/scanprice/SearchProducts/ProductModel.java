package com.scan.tony.scanprice.SearchProducts;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony on 09.07.2016.
 */
public class ProductModel implements Parcelable {
    @SerializedName("name")
    @Expose
    String nameProduct;
    @SerializedName("ed")
    @Expose
    String unitProduct;
    @SerializedName("cost")
    @Expose
    String costProduct;
    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getUnitProduct() {
        return unitProduct;
    }

    public void setUnitProduct(String unitProduct) {
        this.unitProduct = unitProduct;
    }

    public String getCostProduct() {
        return costProduct;
    }

    public void setCostProduct(String costProduct) {
        this.costProduct = costProduct;
    }

    protected ProductModel(Parcel in) {
        nameProduct = in.readString();
        unitProduct = in.readString();
        costProduct = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameProduct);
        dest.writeString(unitProduct);
        dest.writeString(costProduct);
    }
}
