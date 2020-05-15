package com.saifi.warehouse.retrofitmodel.storeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreDatumTabModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("business_location")
    @Expose
    private String businessLocation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cdate")
    @Expose
    private String cdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

}
