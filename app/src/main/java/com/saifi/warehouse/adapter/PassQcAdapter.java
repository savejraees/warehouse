package com.saifi.warehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;

import java.util.ArrayList;

public class PassQcAdapter extends RecyclerView.Adapter<PassQcAdapter.TotalHolder> {

    Context context;
    ArrayList<AllQCDatum> list;
    Views views = new Views();


    public PassQcAdapter(Context context, ArrayList<AllQCDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PassQcAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_qc_all, parent, false);
        return new PassQcAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PassQcAdapter.TotalHolder holder, int position) {
        final AllQCDatum totalModel = list.get(position);
        holder.txtBrandAllQC.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
//        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );


    }


//    public void removeItem(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TotalHolder extends RecyclerView.ViewHolder {

        Button submit;
        TextView txtBrandAllQC, txtModelAll, txtGBAll, txtNameAll,txtBarcodeAll,txtCategoryAll;
        RadioGroup radioShop;

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

        }
    }
}

