package com.saifi.warehouse.constant;

import com.saifi.warehouse.retrofitmodel.AllStatusModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApi(@Field("key")String key,@Field("page")String page,@Field("status")String status);

}
