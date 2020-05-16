package com.saifi.warehouse.constant;

import com.google.gson.JsonObject;
import com.saifi.warehouse.retrofitmodel.AllStatusModel;
import com.saifi.warehouse.retrofitmodel.LoginModel;
import com.saifi.warehouse.retrofitmodel.StatusModel;
import com.saifi.warehouse.retrofitmodel.SubmitToWareHouseModel;
import com.saifi.warehouse.retrofitmodel.qcModel.StatusAllQC;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Status;
import com.saifi.warehouse.retrofitmodel.storeModel.StatusTabModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("warehouse_allshop_purchase_list")
    Call<AllStatusModel> hitAllApi(@Field("key") String key, @Field("page") String page, @Field("status") String status, @Field("category") String category);

    @FormUrlEncoded
    @POST("rco_history")
    Call<RCO_Status> hitOpenBoxApi(@Field("key") String key, @Field("page") String page, @Field("category") String category);

    @FormUrlEncoded
    @POST("rco_history")
    Call<RCO_Status> hitOpenBoxApiSearch(@Field("key") String key, @Field("page") String page, @Field("category") String category,
                                          @Field("search") String search );

    @FormUrlEncoded
    @POST("getstore_history")
    Call<RCO_Status> hitStoreHistoryApi(@Field("key") String key, @Field("page") String page, @Field("business_location_id") String id);


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
    @POST("update_business_address")
    Call<SubmitQCModel> hitSubmitStore(@Field("key") String key, @Field("phone_id") String phoneId, @Field("business_address_id") String buisnessId);

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

    @FormUrlEncoded
    @POST("getbusiness")
    Call<StatusTabModel> hitStatusTabApi(@Field("key") String key);

    @Multipart
    @POST("warehouse_upload_mobile_img")
    Call<JsonObject> imageAPi(@Part MultipartBody.Part[] imageArray1, @PartMap() Map<String, RequestBody> partMap);
}
