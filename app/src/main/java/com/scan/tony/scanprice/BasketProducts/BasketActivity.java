package com.scan.tony.scanprice.BasketProducts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.scan.tony.scanprice.R;
import com.scan.tony.scanprice.SearchProducts.ProductsActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class BasketActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "just";
    private ArrayList<Product> productList;
    private SQLiteDatabase db;
    private ProductAdapter adapter;
    private RecyclerView productRecycler;
    private Cursor cursor;
    private ProductDbHelper productDbHelper;
    private String token;
    @Override
    protected void onResume() {
        super.onResume();
        if(productRecycler.getAdapter() == null){
            adapter = new ProductAdapter(productList);
            productRecycler.setAdapter(adapter);
            //productRecycler.setAdapter(new ProductAdapter(productList));
        }else{
            productRecycler.getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intentBasket = getIntent();                            //В этой активности нужен токен,для отправки его на сервер и проверки,
        token = intentBasket.getStringExtra("token");    // есть ли данный токен в БД
                                                                      //Нужно подправить скрипт "1", т.к он не проверяет по токену.
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                boolean isFirstStart = getPrefs.getBoolean("start", true);

                if (isFirstStart) {
                    Intent i = new Intent(BasketActivity.this, MyIntro.class);
                    startActivity(i);

                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("start", false);
                    e.apply();
                }
            }
        });

        t.start();


        productRecycler = (RecyclerView) findViewById(R.id.product_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        productRecycler.setLayoutManager(layoutManager);
        productRecycler.setHasFixedSize(true);


        productDbHelper = new ProductDbHelper(this);
        db = productDbHelper.getReadableDatabase();


        cursor = productDbHelper.getInformation(db);

        cursor.moveToFirst();

        productList = new ArrayList<Product>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                productList.add(product);

            } while (cursor.moveToNext());
        }
       /* if(productRecycler.getAdapter() == null){
            adapter = new ProductAdapter(productList);
            productRecycler.setAdapter(adapter);
            //productRecycler.setAdapter(new ProductAdapter(productList));
        }else{
            productRecycler.getAdapter().notifyDataSetChanged();
        }

        */

        // productRecycler.getAdapter().notifyDataSetChanged();


        //adapter = new ProductAdapter(productList);
        //adapter.notifyDataSetChanged();
        // productRecycler.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDbHelper.close();
                new IntentIntegrator(BasketActivity.this).initiateScan();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.d("TAG", "Scanned");
            Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();
            String barcode = result.getContents();


            new Async().execute(barcode);


        } else {


            Log.d("TAG", "Cancelled scan");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); //получение id нажатой кнопки в баре

        if (id == R.id.search_product){
            Intent intentProducts = new Intent(BasketActivity.this, ProductsActivity.class);
            Log.d("MyToken", token);
            intentProducts.putExtra("token",token);
            startActivity(intentProducts);
        }

        if (id == R.id.clear_table) {

            db.delete(ProductInformation.ProductAdd.TABLE_NAME, null, null);
            productList.clear();
            adapter.notifyDataSetChanged();

            //   productRecycler.getAdapter().notifyItemRangeInserted(productList.size(),productList.size());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class Async extends AsyncTask<String, String, String> {

        private String resp;
        private String unit;
        private String name;
        private String price;

        @Override
        protected String doInBackground(String... params) {


            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler response = new BasicResponseHandler();
            HttpGet http = new HttpGet("http://appit.hostenko.com/1.php?sh=" + params[0]);

            try {
                resp = (String) hc.execute(http, response);
                Log.d("DAS", resp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject parsedObject = new JSONObject(result);
                price = parsedObject.getString("cost");
                name = parsedObject.getString("name");
                unit = parsedObject.getString("ed");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //ProductDbHelper productDbHelper = new ProductDbHelper(BasketActivity.this);
            db = productDbHelper.getWritableDatabase();
            productDbHelper.putInformation(name, unit, price, db);
            productDbHelper.close();




        }

    }

}
