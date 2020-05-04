package com.saifi.warehouse.constant;

import com.saifi.warehouse.retrofitmodel.AllStatusModel;
import com.saifi.warehouse.retrofitmodel.LoginModel;
import com.saifi.warehouse.retrofitmodel.StatusModel;
import com.saifi.warehouse.retrofitmodel.SubmitToWareHouseModel;
import com.saifi.warehouse.retrofitmodel.qcModel.StatusAllQC;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApi(@Field("key") String key, @Field("page") String page, @Field("status") String status, @Field("category") String category);

    @FormUrlEncoded
    @POST("rco_history")
    Call<StatusAllQC> hitOpenBoxApi(@Field("key") String key, @Field("page") String page, @Field("category") String category);

    @FormUrlEncoded
    @POST("rco_history")
    Call<StatusAllQC> hitOpenBoxApiSearch(@Field("key") String key, @Field("page") String page, @Field("category") String category,
                                          @Field("search") String search );

    @FormUrlEncoded
    @POST("qc_purchase_history")
    Call<StatusAllQC> hitAllQCApi(@Field("key") String key, @Field("page") String page, @Field("status") String status);

    @FormUrlEncoded
    @POST("qc_purchase_history")
    Call<StatusAllQC> hitAllQCApiSearch(@Field("key") String key, @Field("page") String page, @Field("status") String status, @Field("search") String search);

    @FormUrlEncoded
    @POST("qc_check")
    Call<SubmitQCModel> hitCheckQC(@Field("key") String key, @Field("phone_id") String phoneId, @Field("status_code") String status_code);

    @FormUrlEncoded
    @POST("qc_pass_category")
    Call<SubmitQCModel> hitQCPassCategory(@Field("key") String key, @Field("phone_id") String phoneId, @Field("category") String category);

    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApiSearch(@Field("key") String key, @Field("page") String page, @Field("status") String status, @Field("category") String category, @Field("search") String search);


    @FormUrlEncoded
    @POST("manager_subto_warehouse")
    Call<SubmitToWareHouseModel> hitSubmitWarehoueApi(@Field("key") String key, @Field("phone_id") String phoneId, @Field("code") String barcode);

    @FormUrlEncoded
    @POST("purchase_login_new")
    Call<LoginModel> hitLoginApi(@Field("key") String key, @Field("mobile") String mobile, @Field("password") String password,
                                 @Field("role") String role, @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST("check_active_user")
    Call<StatusModel> hitStatusApi(@Field("key") String key, @Field("user_id") String id);
}
