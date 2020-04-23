package com.saifi.warehouse.constant;

import com.saifi.warehouse.retrofitmodel.AllStatusModel;
import com.saifi.warehouse.retrofitmodel.SubmitToWareHouseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApi(@Field("key")String key,@Field("page")String page,@Field("status")String status,@Field("category")String category);

    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApiSearch(@Field("key")String key,@Field("page")String page,@Field("status")String status,@Field("category")String category,@Field("search")String search);


    @FormUrlEncoded
    @POST("manager_subto_warehouse")
    Call<SubmitToWareHouseModel> hitSubmitWarehoueApi(@Field("key")String key,@Field("phone_id")String phoneId,@Field("code")String barcode);
}
