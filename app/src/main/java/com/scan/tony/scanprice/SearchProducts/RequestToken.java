package com.scan.tony.scanprice.SearchProducts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Tony on 19.07.2016.
 */
public interface RequestToken {
    @GET("GetToken.php")
    Call<String> getProducts(@Query("token") String token);
}
