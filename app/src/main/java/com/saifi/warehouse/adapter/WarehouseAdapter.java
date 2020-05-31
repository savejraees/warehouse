package com.saifi.warehouse.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
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
import com.saifi.warehouse.fragment.StoresFragment;
import com.saifi.warehouse.fragment.WarehouseFragment;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.TotalHolder> {

    Context context;
    ArrayList<AllQCDatum> list;
    Views views = new Views();
    String status_code = "";


    public WarehouseAdapter(Context context, ArrayList<AllQCDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WarehouseAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_qc_all, parent, false);
        return new WarehouseAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final WarehouseAdapter.TotalHolder holder, int position) {
        final AllQCDatum totalModel = list.get(position);
        holder.txtBrandAllQC.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
//        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );

        if(totalModel.getSaleAmount()==0){
            holder.submit.setVisibility(View.GONE);
        }

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int phoneId = totalModel.getId();
                int pos = holder.getAdapterPosition();

//                Log.d("askdjdsa", phoneId + " " + pos + " " + new WarehouseFragment().ID_warehouseSpinner);
                    hitApiCheck(phoneId,pos);


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hitApiCheck(int phoneId, final int pos) {
        views.showProgress(context);
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build()).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<SubmitQCModel> call = api.hitSubmitStore(Url.key, String.valueOf(phoneId), String.valueOf(new WarehouseFragment().ID_warehouseSpinner));
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

        Button submit;
        TextView txtBrandAllQC, txtModelAll, txtGBAll, txtNameAll,txtBarcodeAll,txtCategoryAll;
        RadioGroup radioShop;
        RadioButton radioPass,radioFail;

        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            submit = itemView.findViewById(R.id.submitButtonAllQC);
            txtBrandAllQC = itemView.findViewById(R.id.txtBrandAllQC);
            txtModelAll = itemView.findViewById(R.id.txtModelAllQC);
            txtGBAll = itemView.findViewById(R.id.txtGBAllQC);
//            txtPriceAll = itemView.findViewById(R.id.txtPriceAll);
            txtNameAll = itemView.findViewById(R.id.txtNameAllQC);
            txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeAllQC);
            txtCategoryAll = itemView.findViewById(R.id.txtCategoryAllQC);
            radioShop = itemView.findViewById(R.id.radioShop);
            radioShop.setVisibility(View.GONE);
            radioPass = itemView.findViewById(R.id.radioPass);
            radioFail = itemView.findViewById(R.id.radioFail);

        }
    }
}

