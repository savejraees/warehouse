package com.saifi.warehouse.retrofitmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllListDatum {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_no")
    @Expose
    private Object orderNo;
    @SerializedName("purchase_cat_name")
    @Expose
    private String purchaseCatName;
    @SerializedName("product_category")
    @Expose
    private String productCategory;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("series_name")
    @Expose
    private String seriesName;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("gb")
    @Expose
    private String gb;
    @SerializedName("warrenty")
    @Expose
    private String warrenty;
    @SerializedName("warrenty_month")
    @Expose
    private String warrentyMonth;
    @SerializedName("imei_no")
    @Expose
    private String imeiNo;
    @SerializedName("product_condotion")
    @Expose
    private String productCondotion;
    @SerializedName("purchase_amount")
    @Expose
    private Integer purchaseAmount;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_mobile")
    @Expose
    private String customerMobile;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("cdate")
    @Expose
    private String cdate;
    @SerializedName("status_ok")
    @Expose
    private String statusOk;
    @SerializedName("status_prexo")
    @Expose
    private String statusPrexo;
    @SerializedName("status_reparing")
    @Expose
    private String statusReparing;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("sale_amount")
    @Expose
    private Integer saleAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("app_price")
    @Expose
    private Integer appPrice;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("uniquecode")
    @Expose
    private Object uniquecode;
    @SerializedName("exchange")
    @Expose
    private Object exchange;
    @SerializedName("barcode_scan")
    @Expose
    private Object barcodeScan;
    @SerializedName("category_type")
    @Expose
    private Object categoryType;
    @SerializedName("business_location_id")
    @Expose
    private Object businessLocationId;
    @SerializedName("device_shop_address_id")
    @Expose
    private Object deviceShopAddressId;
    @SerializedName("stock_check_status")
    @Expose
    private String stockCheckStatus;
    @SerializedName("stock_check_date")
    @Expose
    private Object stockCheckDate;
    @SerializedName("device_shop_address_date")
    @Expose
    private String deviceShopAddressDate;
    @SerializedName("managertowarehouse_date")
    @Expose
    private Object managertowarehouseDate;
    @SerializedName("manager_recive_status")
    @Expose
    private Object managerReciveStatus;
    @SerializedName("manager_recive_date")
    @Expose
    private Object managerReciveDate;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("puchase_shopaddress")
    @Expose
    private Object puchaseShopaddress;
    @SerializedName("stock_shopaddress")
    @Expose
    private Object stockShopaddress;
    @SerializedName("viewstatus")
    @Expose
    private String viewstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Object orderNo) {
        this.orderNo = orderNo;
    }

    public String getPurchaseCatName() {
        return purchaseCatName;
    }

    public void setPurchaseCatName(String purchaseCatName) {
        this.purchaseCatName = purchaseCatName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGb() {
        return gb;
    }

    public void setGb(String gb) {
        this.gb = gb;
    }

    public String getWarrenty() {
        return warrenty;
    }

    public void setWarrenty(String warrenty) {
        this.warrenty = warrenty;
    }

    public String getWarrentyMonth() {
        return warrentyMonth;
    }

    public void setWarrentyMonth(String warrentyMonth) {
        this.warrentyMonth = warrentyMonth;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getProductCondotion() {
        return productCondotion;
    }

    public void setProductCondotion(String productCondotion) {
        this.productCondotion = productCondotion;
    }

    public Integer getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Integer purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getStatusOk() {
        return statusOk;
    }

    public void setStatusOk(String statusOk) {
        this.statusOk = statusOk;
    }

    public String getStatusPrexo() {
        return statusPrexo;
    }

    public void setStatusPrexo(String statusPrexo) {
        this.statusPrexo = statusPrexo;
    }

    public String getStatusReparing() {
        return statusReparing;
    }

    public void setStatusReparing(String statusReparing) {
        this.statusReparing = statusReparing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Integer saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(Integer appPrice) {
        this.appPrice = appPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Object getUniquecode() {
        return uniquecode;
    }

    public void setUniquecode(Object uniquecode) {
        this.uniquecode = uniquecode;
    }

    public Object getExchange() {
        return exchange;
    }

    public void setExchange(Object exchange) {
        this.exchange = exchange;
    }

    public Object getBarcodeScan() {
        return barcodeScan;
    }

    public void setBarcodeScan(Object barcodeScan) {
        this.barcodeScan = barcodeScan;
    }

    public Object getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Object categoryType) {
        this.categoryType = categoryType;
    }

    public Object getBusinessLocationId() {
        return businessLocationId;
    }

    public void setBusinessLocationId(Object businessLocationId) {
        this.businessLocationId = businessLocationId;
    }

    public Object getDeviceShopAddressId() {
        return deviceShopAddressId;
    }

    public void setDeviceShopAddressId(Object deviceShopAddressId) {
        this.deviceShopAddressId = deviceShopAddressId;
    }

    public String getStockCheckStatus() {
        return stockCheckStatus;
    }

    public void setStockCheckStatus(String stockCheckStatus) {
        this.stockCheckStatus = stockCheckStatus;
    }

    public Object getStockCheckDate() {
        return stockCheckDate;
    }

    public void setStockCheckDate(Object stockCheckDate) {
        this.stockCheckDate = stockCheckDate;
    }

    public String getDeviceShopAddressDate() {
        return deviceShopAddressDate;
    }

    public void setDeviceShopAddressDate(String deviceShopAddressDate) {
        this.deviceShopAddressDate = deviceShopAddressDate;
    }

    public Object getManagertowarehouseDate() {
        return managertowarehouseDate;
    }

    public void setManagertowarehouseDate(Object managertowarehouseDate) {
        this.managertowarehouseDate = managertowarehouseDate;
    }

    public Object getManagerReciveStatus() {
        return managerReciveStatus;
    }

    public void setManagerReciveStatus(Object managerReciveStatus) {
        this.managerReciveStatus = managerReciveStatus;
    }

    public Object getManagerReciveDate() {
        return managerReciveDate;
    }

    public void setManagerReciveDate(Object managerReciveDate) {
        this.managerReciveDate = managerReciveDate;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getPuchaseShopaddress() {
        return puchaseShopaddress;
    }

    public void setPuchaseShopaddress(Object puchaseShopaddress) {
        this.puchaseShopaddress = puchaseShopaddress;
    }

    public Object getStockShopaddress() {
        return stockShopaddress;
    }

    public void setStockShopaddress(Object stockShopaddress) {
        this.stockShopaddress = stockShopaddress;
    }

    public String getViewstatus() {
        return viewstatus;
    }

    public void setViewstatus(String viewstatus) {
        this.viewstatus = viewstatus;
    }
}
