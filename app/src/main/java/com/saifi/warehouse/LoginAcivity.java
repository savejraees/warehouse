package com.saifi.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.LoginModel;
import com.saifi.warehouse.retrofitmodel.ResponseError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

public class LoginAcivity extends AppCompatActivity {

  EditText edtUserName,edtPassword;
  Button btnLogin;
    SessonManager sessonManager;
    Views views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);
        getSupportActionBar().hide();

        views = new Views();
        sessonManager = new SessonManager(LoginAcivity.this);

        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getBaseContext(),MainActivity.class));
//                finishAffinity();
                
                if(edtUserName.getText().toString().isEmpty()||edtUserName.getText().toString().equals("")){
                    edtUserName.setError("Please enter Mobile No.");
                    edtUserName.requestFocus();
                }
                else if(edtPassword.getText().toString().isEmpty()||edtPassword.getText().toString().equals("")){
                    edtPassword.setError("Please enter Password");
                    edtPassword.requestFocus();
                }else{
                    hitLoginApi();
                }
                    
            }
        });
    }

    private void hitLoginApi() {
        views.showProgress(LoginAcivity.this);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<LoginModel> call = api.hitLoginApi(Url.key, edtUserName.getText().toString(),
                edtPassword.getText().toString(), "warehouse", "warehouse");

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                views.hideProgress();
                if (response.isSuccessful()) {
                    LoginModel loginModel = response.body();

                    if (loginModel.getCode().equals("200")) {
                        views.showToast(getApplicationContext(), loginModel.getMsg());

                        String name = loginModel.getName();
                        String mobile = loginModel.getMobile();
                        String location = loginModel.getLocation();
                        int Buisnesslocation = loginModel.getBusinessLocationId();

                        int userId = loginModel.getUserid();
                        sessonManager.setToken(String.valueOf(userId));
                        sessonManager.setMobile(mobile);
                        sessonManager.setUserName(name);
                        sessonManager.setLocation(location);
                        sessonManager.setBuisnessLocationId(String.valueOf(Buisnesslocation));

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();
                    } else {
                        // Gson gson = new GsonBuilder().create();
//                        ResponseError responseError = gson.fromJson(gson.toJson(loginModel.getMsg()),ResponseError.class);
                        views.showToast(getApplicationContext(), "" + loginModel.getMsg());
                    }
                } else {
                    Gson gson = new GsonBuilder().create();
                    ResponseError responseError = gson.fromJson(response.errorBody().charStream(), ResponseError.class);
                    views.showToast(getApplicationContext(), responseError.getMsg());
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                views.showToast(getApplicationContext(), t.getMessage());

            }
        });
    }
}
