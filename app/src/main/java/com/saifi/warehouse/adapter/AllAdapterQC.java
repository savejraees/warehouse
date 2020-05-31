package com.saifi.warehouse.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

public class AllAdapterQC extends RecyclerView.Adapter<AllAdapterQC.TotalHolder> {

    Context context;
    ArrayList<AllQCDatum> list;
    Views views = new Views();
    String status_code = "";


    public AllAdapterQC(Context context, ArrayList<AllQCDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_qc_all, parent, false);
        return new TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TotalHolder holder, int position) {
        final AllQCDatum totalModel = list.get(position);
        holder.txtBrandAllQC.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
//        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );

        holder.radioShop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)holder.radioShop.findViewById(i);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {

                    if(checkedRadioButton.getText().toString().equals("QC Pass")){
                        status_code = "5";
                    }
                    else if(checkedRadioButton.getText().toString().equals("QC Fail")){
                        status_code = "6";
                    }

                }

            }
        });

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int phoneId = totalModel.getId();
                int pos = holder.getAdapterPosition();

                if(holder.radioPass.isChecked()==false && holder.radioFail.isChecked()==false){
                    Toast.makeText(context, "Please Select QC Pass Or Fail", Toast.LENGTH_SHORT).show();
                    status_code ="";

                }else {
                    hitApiCheck(phoneId,pos);
                }

            }
        });

    }

    private void hitApiCheck(int phoneId,  final int pos) {
        views.showProgress(context);
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build()).addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<SubmitQCModel> call = api.hitCheckQC(Url.key, String.valueOf(phoneId), status_code);
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
            radioPass = itemView.findViewById(R.id.radioPass);
            radioFail = itemView.findViewById(R.id.radioFail);

        }
    }
}

