package com.saifi.warehouse.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.saifi.warehouse.model.ImageModel;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.SubmitQCModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenBoxSubAdapter extends RecyclerView.Adapter<OpenBoxSubAdapter.ListViewHolder> {

    ArrayList<ImageModel> list;
    Context context;

    public OpenBoxSubAdapter(ArrayList<ImageModel> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public OpenBoxSubAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_image, null);

        return new OpenBoxSubAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenBoxSubAdapter.ListViewHolder holder, final int position) {
        final ImageModel imageModel = list.get(position);
        holder.image.setImageBitmap(imageModel.getImageMobile());
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
            }
        });
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView close;

        public ListViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ivGallery);
            close = itemView.findViewById(R.id.badge_view);
        }

    }
}

