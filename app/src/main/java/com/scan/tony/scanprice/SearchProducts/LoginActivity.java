package com.scan.tony.scanprice.SearchProducts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.scan.tony.scanprice.BasketProducts.BasketActivity;
import com.scan.tony.scanprice.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button buttonVk;
    String token;
    private String[] scope = new String[]{VKScope.WALL, VKScope.EMAIL,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        buttonVk = (Button) findViewById(R.id.button_vk);

        buttonVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(LoginActivity.this, ProductsActivity.class);
                //intent.putExtra("kek",scope[1]);
               // startActivity(intent);
              //  String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getClass());
               // Log.d("MySertif",scope.toString());
                VKSdk.login(LoginActivity.this,scope);

            }
        });

    }



    




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался и ему выдается токен от сервера вк
// Отправляется запрос с выданным токеном на наш сервер, где токен записывается в БД.
               token = res.accessToken;
                Retrofit    Tokenretrofit = new Retrofit.Builder()
                        .baseUrl("http://appit.hostenko.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final RequestToken service = Tokenretrofit.create(RequestToken.class);
                final Call<String> CallString = (Call<String>) service.getProducts(token);
                CallString.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //на ответ пофигу.
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(LoginActivity.this, BasketActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Вы авторизировались" + res.accessToken,Toast.LENGTH_LONG).show();
                Log.d("lala", String.valueOf(res));
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Toast.makeText(getApplicationContext(),"Произошла ошибка",Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
