package com.saifi.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.StatusModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends Activity {

    String id ="";
    Views views;
    SessonManager sessonManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        views = new Views();
        sessonManager = new SessonManager(SplashActivity.this);

        if(sessonManager.getToken().isEmpty()||sessonManager.getToken()==null||sessonManager.getToken().equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Intent i = new Intent(SplashActivity.this, LoginAcivity.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);

        }else {
            hitStatusApi();
        }

    }




    private void hitStatusApi() {
        views.showProgress(SplashActivity.this);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<StatusModel> call = api.hitStatusApi(Url.key,sessonManager.getToken());
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                views.hideProgress();
                if(response.isSuccessful()){
                    StatusModel statusModel = response.body();
                    if(statusModel.getCode().equalsIgnoreCase("200")){
                        views.showToast(getBaseContext(),statusModel.getMsg());
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                    }
                    else {
                        views.showToast(getBaseContext(),statusModel.getMsg());
                        finish();
                        Intent i = new Intent(getBaseContext(), LoginAcivity.class);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                views.showToast(getBaseContext(),t.getMessage());
                views.hideProgress();
            }
        });
    }

}
