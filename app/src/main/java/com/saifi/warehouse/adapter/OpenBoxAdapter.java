package com.saifi.warehouse.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.fragment.OpenBoxFragment;
import com.saifi.warehouse.fragmentQC.PassFragmentQC;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenBoxAdapter extends RecyclerView.Adapter<OpenBoxAdapter.TotalHolder> {

    Context context;
    ArrayList<AllQCDatum> list;
    Views views = new Views();
    String status_code = "";


    public OpenBoxAdapter(Context context, ArrayList<AllQCDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OpenBoxAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_open_box, parent, false);
        return new OpenBoxAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final OpenBoxAdapter.TotalHolder holder, int position) {
        final AllQCDatum totalModel = list.get(position);
        holder.txtBrandAllQC.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan());
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName());


        holder.submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (new OpenBoxFragment().spinnerValueOpenBox.equalsIgnoreCase("Select Category")) {
                    Toast.makeText(context, "Please Select Category", Toast.LENGTH_SHORT).show();
                } else {
                    int phoneId = totalModel.getId();
                    int pos = holder.getAdapterPosition();
                    Toast.makeText(context, "" + new OpenBoxFragment().spinnerValueOpenBox, Toast.LENGTH_SHORT).show();

                    //  hitApiCheck(phoneId,pos);
                }

            }
        });

        holder.uploadOpenBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(context, ""+totalModel.getImgEnable(), Toast.LENGTH_SHORT).show();
                       if(totalModel.getImgEnable()==false){
                           totalModel.setImgEnable(true);
                           holder.submit.setVisibility(View.VISIBLE);
                           holder.uploadOpenBox.setVisibility(View.GONE);
                       }
            }
        });



    }

    private void hitApiCheck(int phoneId, final int pos) {
        views.showProgress(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Url.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<SubmitQCModel> call = api.hitCheckQC(Url.key, String.valueOf(phoneId), status_code);
        call.enqueue(new Callback<SubmitQCModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<SubmitQCModel> call, Response<SubmitQCModel> response) {
                views.hideProgress();
                if (response.isSuccessful()) {
                    SubmitQCModel model = response.body();

                    if (model.getCode().equals("200")) {
                        views.showToast(context, model.getMsg());

                        removeItem(pos);
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

        Button submit, uploadOpenBox;
        TextView txtBrandAllQC, txtModelAll, txtGBAll, txtNameAll, txtBarcodeAll, txtCategoryAll;


        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            submit = itemView.findViewById(R.id.submitOpenBox);
            submit.setVisibility(View.GONE);
            uploadOpenBox = itemView.findViewById(R.id.uploadOpenBox);
            txtBrandAllQC = itemView.findViewById(R.id.txtBrandOpenBox);
            txtModelAll = itemView.findViewById(R.id.txtModelOpenBox);
            txtGBAll = itemView.findViewById(R.id.txtGBOpenBox);
            txtNameAll = itemView.findViewById(R.id.txtNameOpenBox);
            txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeOpenBox);
            txtCategoryAll = itemView.findViewById(R.id.txtCategoryOpenBox);


        }
    }
}

