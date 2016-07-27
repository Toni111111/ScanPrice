package com.scan.tony.scanprice.BasketProducts;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ProductDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "product_db";
    public static final int DB_VERSION = 1;
    //прописываем, как создастся бд и добавляем описание одной записи в ней
    public static final String CREATE_QUERY = "create table " + ProductInformation.ProductAdd.TABLE_NAME +
            "( " + ProductInformation.ProductAdd.NAME + " text,"
            + ProductInformation.ProductAdd.UNIT + " text,"
            + ProductInformation.ProductAdd.PRICE + " text);";
    public static final String DROP_QUERY = "drop table if exists " + ProductInformation.ProductAdd.TABLE_NAME + ";";

    //конструктор
    public ProductDbHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        Log.d("database", "database open");
    }

    //создание бд и описания по константе сверху
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("database", "table created");

    }

    //обновление бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_QUERY);
        Log.d("database", "database updated");

    }

    //метод для создания новой записи в бд
    public void putInformation(String name, String unit, String price, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductInformation.ProductAdd.NAME, name);
        contentValues.put(ProductInformation.ProductAdd.UNIT, unit);
        contentValues.put(ProductInformation.ProductAdd.PRICE, price);
        long l = db.insert(ProductInformation.ProductAdd.TABLE_NAME, null, contentValues); //что есть это?

        Log.d("database", "Add one row");

    }

    //получаем информацию из бд
    public Cursor getInformation(SQLiteDatabase db) {
        String projection[] = {ProductInformation.ProductAdd.NAME,
                ProductInformation.ProductAdd.UNIT, ProductInformation.ProductAdd.PRICE};

        Cursor cursor = db.query(ProductInformation.ProductAdd.TABLE_NAME, projection, null, null, null, null,null);

        return cursor;
    }

}

