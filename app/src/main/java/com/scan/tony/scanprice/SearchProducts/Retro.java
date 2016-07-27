package com.scan.tony.scanprice.SearchProducts;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tony on 10.07.2016.
 */
public interface Retro {

    //@GET("pagin.php") // work
    @GET("{script}")
    Call<ArrayList<ProductModel>> getProducts(@Path("script") String script, @Query("token") String token, @Query("page") int page);   // get запрос retrofit , в параметры передаем значения

}
