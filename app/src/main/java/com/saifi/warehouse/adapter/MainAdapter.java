package com.saifi.warehouse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.fragment.CustomerUsedFragment;
import com.saifi.warehouse.fragment.HomeFragment;
import com.saifi.warehouse.fragment.OpenBoxFragment;
import com.saifi.warehouse.fragment.QCFragment;
import com.saifi.warehouse.fragment.RefurbisedFragment;
import com.saifi.warehouse.fragment.RequestFragment;
import com.saifi.warehouse.fragment.ReturnFragment;
import com.saifi.warehouse.fragment.StoresFragment;
import com.saifi.warehouse.fragment.WarehouseFragment;
import com.saifi.warehouse.model.MainCatogryModel;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.TotalHolder> {

    Context context;
    ArrayList<MainCatogryModel> list;
    int index =-1;

    public MainAdapter(Context context, ArrayList<MainCatogryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_main,parent,false);
        return new MainAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.TotalHolder holder, final int position) {
        MainCatogryModel totalModel = list.get(position);
        holder.txtMAin.setText(totalModel.getCategoryNAme());
        Glide.with(context).load(totalModel.getImg()).into(holder.imgMAin);

        holder.linearLaoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
                if(index==0){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    HomeFragment frag = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.frame,frag,frag.getTag()).commit();
                }
                else if(index==1){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    RequestFragment frag = new RequestFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==2){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    QCFragment frag = new QCFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==3){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    OpenBoxFragment frag = new OpenBoxFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==4){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    CustomerUsedFragment frag = new CustomerUsedFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==5){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    RefurbisedFragment frag = new RefurbisedFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==6){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    StoresFragment frag = new StoresFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==7){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    WarehouseFragment frag = new WarehouseFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
               else if(index==8){
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    ReturnFragment frag = new ReturnFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame,frag,frag.getTag())
                            .addToBackStack(null).commit();
                }
            }
        });
        if(index==position){
            holder.linearLaoutMain.setBackgroundColor(Color.parseColor("#DDA90C"));
        }else{
            holder.linearLaoutMain.setBackgroundColor(Color.parseColor("#066e8a"));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TotalHolder extends RecyclerView.ViewHolder {

        TextView txtMAin;
        ImageView imgMAin;
        LinearLayout linearLaoutMain;
        public TotalHolder(@NonNull View itemView) {
            super(itemView);
            txtMAin = itemView.findViewById(R.id.txtMAin);
            imgMAin = itemView.findViewById(R.id.imgMAin);
            linearLaoutMain = itemView.findViewById(R.id.linearLaoutMain);

//            linearLaoutMain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//               if(getAdapterPosition()==0){
//                   FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
//                    HomeFragment frag = new HomeFragment();
//                    fragmentManager.beginTransaction().add(R.id.frame,frag,frag.getTag()).commit();
//               }
//                }
//            });

        }
    }
}
