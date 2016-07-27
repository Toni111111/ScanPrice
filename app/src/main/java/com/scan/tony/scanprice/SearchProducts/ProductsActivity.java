package com.scan.tony.scanprice.SearchProducts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;


import com.scan.tony.scanprice.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsActivity extends AppCompatActivity  {
    public String email = "q";
    private ArrayList<ProductModel> productArr = new ArrayList<>();
    public TextView txtv;
    public boolean isLoading = false;
    private RecyclerAdapter adapter;
    private int page = 0;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        if(token == null) {
            Log.d("Tokens is ", "null");
        }else{
            Log.d("Token is",token);
        }
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);

        final  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(productArr, ProductsActivity.this);
        // txtv = (TextView) findViewById(R.id.mytxt);

     //   RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //  Button lol = (Button)findViewById(R.id.btn);


        // .setOnClickListener(new View.OnClickListener() {
        //  @Override
        // public void onClick(View v) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://appit.hostenko.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Retro service = retrofit.create(Retro.class);


        final String[] a = new String[1];
        final Call<ArrayList<ProductModel>> call = (Call<ArrayList<ProductModel>>) service.getProducts("pagin.php",token, 0);
        call.enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                if (response.code() == 200) {
                    int prevSize = productArr.size();
                    productArr.addAll(response.body());
                    if(rv.getAdapter() == null){
                        rv.setAdapter(new RecyclerAdapter(productArr, ProductsActivity.this));
                    }
                    rv.getAdapter().notifyItemRangeInserted(prevSize,productArr.size());


                   // for (ProductModel model : response.body()) {
                         //a[0] = a[0] + model.getNameProduct();
                        //test  = test.addA(response.body())
                       //  a[0] = a[0] + response.body();
                       //  Log.d("myfirst",a[0]);

                       // rv.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
                        //productArr.addAll(response.body());

                        //adapter = new RecyclerAdapter(response.body(), ProductsActivity.this);
                     //   rv.setAdapter(adapter);
                    //}
                   // txtv.setText(a[0]);
                   // Log.d("mymsg", a[0]);
                } else {
                   // Log.e("MyLog", "пришёл ответ сервера " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {

            }


        });





        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount(); //количество элементов на экране
                int totalItemCount = linearLayoutManager.getItemCount(); //сколько всего элементов.
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition(); //какая позиция первого элемента
                page++;


                MyInterface loadListener = new MyInterface() {

                    @Override
                    public void loadMoreItems(int total) {
                        final Call<ArrayList<ProductModel>> call2 = (Call<ArrayList<ProductModel>>) service.getProducts("pagin.php",token, page);
                        call2.enqueue(new Callback<ArrayList<ProductModel>>() {
                            @Override
                            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                                if (response.code() == 200) {

                                    productArr.addAll(response.body());
                                    if(rv.getAdapter() == null){
                                        rv.setAdapter(new RecyclerAdapter(productArr, ProductsActivity.this));
                                    }
                                    rv.getAdapter().notifyItemRangeInserted(productArr.size(),productArr.size());
                                    isLoading = false;
                                  //  for (ProductModel model : response.body()) {
                                    //adapter.add(model);
                                      //      adapter.notifyDataSetChanged();


                                        //RecyclerAdapter adapter = new RecyclerAdapter(response.body(), ProductsActivity.this); //костыль
                                        //rv.setAdapter(adapter);
                                       // rv.noti
                                        //нужно изменить массив на другую стрктуру
                             //       }
                               // } else {
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {

                            }


                        });
                    }
                };


                if(!isLoading){
                    if (visibleItemCount+firstVisibleItems >= totalItemCount){
                        isLoading = true;
                        if(loadListener != null){
                            loadListener.loadMoreItems(totalItemCount);

                        }
                    }
                }


                //int FirstPage = 0;
              /*  int page = 0;
                Log.d("MyTag", String.valueOf(visibleItemCount));

                if(visibleItemCount <= 8){ //если количество элементов равно количеству элементу, то увеличиваем на 1+
                    // there will be get request
                    page++;
                Log.d("Mytag1", String.valueOf(page));
                    Call<ProductModel[]> call2= (Call<ProductModel[]>) service.getProducts("pagin.php","asda", page);
                    call2.enqueue(new Callback<ProductModel[]>() {
                        @Override
                        public void onResponse(Call<ProductModel[]> call, Response<ProductModel[]> response) {
                            if (response.code() == 200) {


                                for (ProductModel model : response.body()) {
                                    RecyclerAdapter adapter = new RecyclerAdapter(response.body(), ProductsActivity.this);

                                    rv.setAdapter(adapter);
                                }
                            } else {
                                // Log.e("MyLog", "пришёл ответ сервера " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductModel[]> call, Throwable t) {
                            // txtv.setText("Ошибка сервера" + t.getMessage());
                        }
                    });



                }else{
                    Toast.makeText(ProductsActivity.this,"not 5",Toast.LENGTH_LONG);
                }
*/
            }


        };
        rv.setOnScrollListener(scrollListener);
    }
}
