package com.saifi.warehouse.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FailQCAdapter extends RecyclerView.Adapter<FailQCAdapter.TotalHolder> {

    Context context;
    ArrayList<AllQCDatum> list;
    Views views = new Views();


    public FailQCAdapter(Context context, ArrayList<AllQCDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FailQCAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_qc_fail, parent, false);
        return new FailQCAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final FailQCAdapter.TotalHolder holder, int position) {
        final AllQCDatum totalModel = list.get(position);
        holder.txtBrandFailQC.setText(totalModel.getBrandName());
        holder.txtModelFailQC.setText(totalModel.getModelName());
        holder.txtGBFailQC.setText(totalModel.getGb());
        holder.txtNameFailQC.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeFailQC.setText("Barcode no : " + totalModel.getBarcodeScan() );
        holder.txtCategoryFailQC.setText("Purchase Category : " + totalModel.getPurchaseCatName() );
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int phoneId = totalModel.getId();
                int pos = holder.getAdapterPosition();

                hitApiCheck(phoneId,pos);

            }
        });

    }

    private void hitApiCheck(int phoneId,  final int pos) {
        views.showProgress(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Url.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<SubmitQCModel> call = api.hitCheckQC(Url.key, String.valueOf(phoneId), "5");
        call.enqueue(new Callback<SubmitQCModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<SubmitQCModel> call, Response<SubmitQCModel> response) {
                views.hideProgress();
                if (response.isSuccessful()) {
                    SubmitQCModel model = response.body();

                    if(model.getCode().equals("200")){
                        views.showToast(context, model.getMsg());

                        removeItem(pos);
                    }else {
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

        Button submit;
        TextView txtBrandFailQC, txtModelFailQC, txtGBFailQC, txtNameFailQC,txtBarcodeFailQC,txtCategoryFailQC;


        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            submit = itemView.findViewById(R.id.submitButtonFailQC);
            txtBrandFailQC = itemView.findViewById(R.id.txtBrandFailQC);
            txtModelFailQC = itemView.findViewById(R.id.txtModelFailQC);
            txtGBFailQC = itemView.findViewById(R.id.txtGBFailQC);
//            txtPriceAll = itemView.findViewById(R.id.txtPriceAll);
            txtNameFailQC = itemView.findViewById(R.id.txtNameFailQC);
            txtBarcodeFailQC = itemView.findViewById(R.id.txtBarcodeFailQC);
            txtCategoryFailQC = itemView.findViewById(R.id.txtCategoryFailQC);


        }
    }
}

