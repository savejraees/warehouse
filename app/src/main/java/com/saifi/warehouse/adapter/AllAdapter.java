package com.saifi.warehouse.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.fragment.HomeFragment;
import com.saifi.warehouse.fragmentTotalPurchase.AllFragment;
import com.saifi.warehouse.retrofitmodel.AllListDatum;
import com.saifi.warehouse.retrofitmodel.SubmitToWareHouseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class AllAdapter extends RecyclerView.Adapter<AllAdapter.TotalHolder> {
//
//    Context context;
//    ArrayList<AllListDatum> list;
//    Views views = new Views();
////    List<AllListDatum> filterlist;
//
//
//    public AllAdapter(Context context, ArrayList<AllListDatum> list) {
//        this.context = context;
//        this.list = list;
////        this.filterlist = list;
//    }
//
//    @NonNull
//    @Override
//    public TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.row_all, parent, false);
//        return new TotalHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final TotalHolder holder, int position) {
//        final AllListDatum totalModel = list.get(position);
//        holder.txtBrandAll.setText(totalModel.getBrandName());
//        holder.txtModelAll.setText(totalModel.getModelName());
//        holder.txtGBAll.setText(totalModel.getGb());
////        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
//        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
//        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
//        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );
//
//        holder.scanButtonAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int phoneId = totalModel.getId();
//                final int position = holder.getAdapterPosition();
//                final String barcode = totalModel.getBarcodeScan();
//
//                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                ScanFragment scanFragment = new ScanFragment();
//                fragmentTransaction.add(R.id.frame, scanFragment);
//                fragmentTransaction.commit();
//
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.warehouse_dialog);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setCancelable(false);
//                Button codeSubmitButton = dialog.findViewById(R.id.codeSubmitButton);
//                Button cancelButton = dialog.findViewById(R.id.codecancelButton);
//                final TextView txtBarcodeSubmit = dialog.findViewById(R.id.txtBarcodeSubmit);
//                txtBarcodeSubmit.setText(new SessonManager(context).getBarcode());
//                dialog.show();
//
//                codeSubmitButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if(txtBarcodeSubmit.getText().toString().equals(barcode)){
//                            dialog.dismiss();
//                            hitApiWarehouse(phoneId, barcode, position);
//                        }else {
//                            Toast.makeText(context, "Barcode does not Match", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                });
//
//                cancelButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//    }
//
//    private void hitApiWarehouse(int phoneId, String code, final int pos) {
////        views.showProgress(context);
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Url.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
//
//        ApiInterface api = retrofit.create(ApiInterface.class);
//        Call<SubmitToWareHouseModel> call = api.hitSubmitWarehoueApi(Url.key, String.valueOf(phoneId), code);
//        call.enqueue(new Callback<SubmitToWareHouseModel>() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onResponse(Call<SubmitToWareHouseModel> call, Response<SubmitToWareHouseModel> response) {
////                views.hideProgress();
//                if (response.isSuccessful()) {
//                    SubmitToWareHouseModel model = response.body();
//
//                    if(model.getCode().equals("200")){
//                        views.showToast(context, model.getMsg());
//
//                        new SessonManager(context).setBarcode("");
//
//                        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        HomeFragment scanFragment = new HomeFragment();
//                        fragmentTransaction.replace(R.id.frame, scanFragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                       // removeItem(pos);
//                    }else {
//                        views.showToast(context, model.getMsg());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SubmitToWareHouseModel> call, Throwable t) {
////                views.hideProgress();
//                views.showToast(context, t.getMessage());
//            }
//        });
//    }
//
//    public void removeItem(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//
//    public class TotalHolder extends RecyclerView.ViewHolder {
//
//        Button scanButtonAll;
//        TextView txtBrandAll, txtModelAll, txtGBAll, txtNameAll,txtBarcodeAll,txtCategoryAll;
//        ImageView imgPhone;
//
//        public TotalHolder(@NonNull View itemView) {
//            super(itemView);
//            scanButtonAll = itemView.findViewById(R.id.scanButtonAll);
//            txtBrandAll = itemView.findViewById(R.id.txtBrandAll);
//            txtModelAll = itemView.findViewById(R.id.txtModelAll);
//            txtGBAll = itemView.findViewById(R.id.txtGBAll);
////            txtPriceAll = itemView.findViewById(R.id.txtPriceAll);
//            txtNameAll = itemView.findViewById(R.id.txtNameAll);
//            imgPhone = itemView.findViewById(R.id.imgPhone);
//            txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeAll);
//            txtCategoryAll = itemView.findViewById(R.id.txtCategoryAll);
//
//        }
//    }
//}
