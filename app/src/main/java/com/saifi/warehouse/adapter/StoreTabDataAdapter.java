package com.saifi.warehouse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.fragment.OpenBoxFragment;
import com.saifi.warehouse.fragment.OpenBoxImageFragment;
import com.saifi.warehouse.fragment.StoresFragment;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Datum;
import com.saifi.warehouse.retrofitmodel.storeModel.StoreDatumTabModel;

import java.util.ArrayList;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

public class StoreTabDataAdapter extends RecyclerView.Adapter<StoreTabDataAdapter.TotalHolder> {

    Context context;
    ArrayList<RCO_Datum> list;
    Views views = new Views();

    public StoreTabDataAdapter(Context context, ArrayList<RCO_Datum> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public StoreTabDataAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_open_box, parent, false);
        return new StoreTabDataAdapter.TotalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreTabDataAdapter.TotalHolder holder, int position) {
        final RCO_Datum totalModel = list.get(position);
        holder.txtBrandAllQC.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan());
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName());

        if(totalModel.getSaleAmount()==0){
            holder.submit.setVisibility(View.GONE);
        }
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                int phoneId = totalModel.getId();
                int pos = holder.getAdapterPosition();

                Log.d("askdjdsa", phoneId + " " + pos + " " + new StoresFragment().ID_storeSpinner);
                        hitApiCheck(phoneId, pos);
            }
//                }

        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hitApiCheck(int phoneId, final int pos) {
        views.showProgress(context);
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build()).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<SubmitQCModel> call = api.hitSubmitStore(Url.key, String.valueOf(phoneId), String.valueOf(new StoresFragment().ID_storeSpinner));
        call.enqueue(new Callback<SubmitQCModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<SubmitQCModel> call, Response<SubmitQCModel> response) {
                views.hideProgress();
                if (response.isSuccessful()) {
                    SubmitQCModel model = response.body();

                    if (model.getCode().equals("200")) {
                        views.showToast(context, model.getMsg());

                        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        StoresFragment scanFragment = new StoresFragment();
                        fragmentTransaction.replace(R.id.frame, scanFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
//                        removeItem(pos);
                    } else {
                        views.showToast(context, model.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<SubmitQCModel> call, Throwable t) {
                views.hideProgress();
                views.showToast(context, t.getMessage());
            }
        });
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TotalHolder extends RecyclerView.ViewHolder {

        Button submit, selectOpenBox;
        TextView txtBrandAllQC, txtModelAll, txtGBAll, txtNameAll, txtBarcodeAll, txtCategoryAll;


        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            submit = itemView.findViewById(R.id.submitOpenBox);
            selectOpenBox = itemView.findViewById(R.id.selectOpenBox);
            selectOpenBox.setVisibility(View.GONE);
            txtBrandAllQC = itemView.findViewById(R.id.txtBrandOpenBox);
            txtModelAll = itemView.findViewById(R.id.txtModelOpenBox);
            txtGBAll = itemView.findViewById(R.id.txtGBOpenBox);
            txtNameAll = itemView.findViewById(R.id.txtNameOpenBox);
            txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeOpenBox);
            txtCategoryAll = itemView.findViewById(R.id.txtCategoryOpenBox);


        }
    }
}

