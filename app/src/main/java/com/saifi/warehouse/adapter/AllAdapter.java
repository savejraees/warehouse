package com.saifi.warehouse.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saifi.warehouse.R;
import com.saifi.warehouse.retrofitmodel.AllListDatum;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.TotalHolder> {

    Context context;
    ArrayList<AllListDatum> list;


    public AllAdapter(Context context, ArrayList<AllListDatum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_all, parent, false);
        return new TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TotalHolder holder, int position) {
        final AllListDatum totalModel = list.get(position);
        holder.txtBrandAll.setText(totalModel.getBrandName());
        holder.txtModelAll.setText(totalModel.getModelName());
        holder.txtGBAll.setText(totalModel.getGb());
        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
        holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
        holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
        holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class TotalHolder extends RecyclerView.ViewHolder {

        Button scanButtonAll;
        TextView txtBrandAll, txtModelAll, txtGBAll, txtPriceAll, txtNameAll,txtBarcodeAll,txtCategoryAll;
        ImageView imgPhone;

        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            scanButtonAll = itemView.findViewById(R.id.scanButtonAll);
            txtBrandAll = itemView.findViewById(R.id.txtBrandAll);
            txtModelAll = itemView.findViewById(R.id.txtModelAll);
            txtGBAll = itemView.findViewById(R.id.txtGBAll);
            txtPriceAll = itemView.findViewById(R.id.txtPriceAll);
            txtNameAll = itemView.findViewById(R.id.txtNameAll);
            imgPhone = itemView.findViewById(R.id.imgPhone);
            txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeAll);
            txtCategoryAll = itemView.findViewById(R.id.txtCategoryAll);
        }
    }
}
