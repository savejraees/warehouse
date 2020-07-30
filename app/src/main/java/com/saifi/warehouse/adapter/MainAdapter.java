package com.saifi.warehouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saifi.warehouse.LoginAcivity;
import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.fragment.CustomerUsedFragment;
import com.saifi.warehouse.fragment.HomeFragment;
import com.saifi.warehouse.fragment.OpenBoxFragment;
import com.saifi.warehouse.fragment.QCFragment;
import com.saifi.warehouse.fragment.RefurbisedFragment;
import com.saifi.warehouse.fragment.RequestFragment;
import com.saifi.warehouse.fragment.ReturnFragment;
import com.saifi.warehouse.fragment.StoresFragment;
import com.saifi.warehouse.fragment.WarehouseFragment;
import com.saifi.warehouse.fragment.WarehouseTestFragment;
import com.saifi.warehouse.model.MainCatogryModel;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.TotalHolder> {

    Context context;
    ArrayList<MainCatogryModel> list;
    int index = -1;

    public MainAdapter(Context context, ArrayList<MainCatogryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_main, parent, false);
        return new MainAdapter.TotalHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.TotalHolder holder, final int position) {
        MainCatogryModel totalModel = list.get(position);
        holder.txtMAin.setText(totalModel.getCategoryNAme());
        Glide.with(context).load(totalModel.getImg()).into(holder.imgMAin);

        holder.linearLaoutMain.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
                if (index == 0) {

                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                            fragmentManager.popBackStack();
                        }
                    }

                    HomeFragment frag = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.frame, frag, frag.getTag()).commit();


                } else if (index == 1) {
                    RequestFragment frag = new RequestFragment();
                    replaceFragment(frag);
                } else if (index == 2) {
                    QCFragment frag = new QCFragment();
                    replaceFragment(frag);
                } else if (index == 3) {
                    OpenBoxFragment frag = new OpenBoxFragment();
                    replaceFragment(frag);
                } else if (index == 4) {
                    CustomerUsedFragment frag = new CustomerUsedFragment();
                    replaceFragment(frag);
                } else if (index == 5) {
                    RefurbisedFragment frag = new RefurbisedFragment();
                    replaceFragment(frag);
                } else if (index == 6) {
                    StoresFragment frag = new StoresFragment();
                    replaceFragment(frag);
                } else if (index == 7) {
                    WarehouseFragment frag = new WarehouseFragment();
                    replaceFragment(frag);
                }
//                else if (index == 8) {
//                    WarehouseTestFragment frag = new WarehouseTestFragment();
//                    replaceFragment(frag);
//                }
                else if (index == 8) {
                    ReturnFragment frag = new ReturnFragment();
                    replaceFragment(frag);
                } else if (index == 9) {
                    Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    new SessonManager(context).setToken("");
                    context.startActivity(new Intent(context, LoginAcivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else if (index == 10) {
                    WarehouseTestFragment frag = new WarehouseTestFragment();
                    replaceFragment(frag);
                }


            }
        });
        if (index == position) {
            holder.linearLaoutMain.setBackgroundColor(Color.parseColor("#DDA90C"));
        } else {
            holder.linearLaoutMain.setBackgroundColor(Color.parseColor("#066e8a"));

        }
    }

    public void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, frag, frag.getTag())
                .addToBackStack(null).commit();
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
