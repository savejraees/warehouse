package com.saifi.warehouse.fragmentTotalPurchase;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.fragment.HomeFragment;
import com.saifi.warehouse.retrofitmodel.AllListDatum;
import com.saifi.warehouse.retrofitmodel.AllStatusModel;
import com.saifi.warehouse.retrofitmodel.SubmitToWareHouseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class AllFragment extends Fragment implements RecyclerView.OnScrollChangeListener {

    public AllFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    AllAdapter adapter,searchAdapeter;
    RecyclerView rvAll;
    LinearLayoutManager layoutManager;
    ArrayList<AllListDatum> listData = new ArrayList<>();
    ArrayList<AllListDatum> listData2 = new ArrayList<>();
   // ArrayList<AllListDatum> listDataSearch = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    EditText edtsearchAll;
    Call<AllStatusModel> call;
    TextView txtClear;
    ImageView imgScanAll;
    boolean scanAdapterFragment =false;
    int scanValue=1;
    SessonManager sessonManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_all, container, false);
        views = new Views();
        edtsearchAll = view.findViewById(R.id.edtsearchAll);
        rvAll = view.findViewById(R.id.rvAll);
        txtClear = view.findViewById(R.id.txtClear);
        imgScanAll = view.findViewById(R.id.imgScanAll);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvAll.setLayoutManager(layoutManager);
        sessonManager = new SessonManager(getActivity());

        hitApi();

        rvAll.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new AllAdapter(getActivity(), listData2);
        //Adding adapter to recyclerview
        rvAll.setAdapter(adapter);

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                edtsearchAll.getText().clear();
//                ((MainActivity)getActivity()).barcode ="";

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AllFragment scanFragment = new AllFragment();
                fragmentTransaction.replace(R.id.frameLayout, scanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        imgScanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData2.clear();

                scanAdapterFragment =true;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ScanFragment scanFragment = new ScanFragment();
                fragmentTransaction.add(R.id.frame, scanFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void hitApi() {
        views.showProgress(getActivity());
        listData.clear();
     //   listDataSearch.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        if(((MainActivity)getActivity()).barcode.equals("")||((MainActivity)getActivity()).barcode.isEmpty()){
            call = api.hitAllApi(Url.key, String.valueOf(currentPage), "3", "all");

            scanValue=1;
        }else
        {
            scanAdapterFragment =false;
            currentPage =1;
            scanValue=0;
            call = api.hitAllApiSearch(Url.key, String.valueOf(currentPage), "3", "all",((MainActivity) getActivity()).barcode);
        }


        call.enqueue(new Callback<AllStatusModel>() {
            @Override
            public void onResponse(Call<AllStatusModel> call, Response<AllStatusModel> response) {
                views.hideProgress();

                if (response.isSuccessful()) {
                    AllStatusModel model = response.body();
                    totalPage = model.getTotalPages();

                    listData = model.getData();
                    listData2.addAll(listData);
                    adapter.notifyDataSetChanged();

                    if(listData2.size()>2){
                        currentPage = currentPage + 1;
                    }else {
                        currentPage =1;
                    }


                } else {
                    views.showToast(getActivity(), String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<AllStatusModel> call, Throwable t) {
                views.hideProgress();
                views.showToast(getActivity(), t.getMessage());
            }
        });
    }


    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        if (isLastItemDisplaying(rvAll)) {
            if (currentPage < totalPage && listData2.size()>1) {
                hitApi();
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
      //  sessonManager.setBarcode();
        edtsearchAll.setText(((MainActivity) getActivity()).barcode);

        if(edtsearchAll.getText().toString().equals("")){
        }else {
            listData2.clear();

            if(scanAdapterFragment==true){
                hitApi();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ((MainActivity) getActivity()).barcode="";
    }


    public class AllAdapter extends RecyclerView.Adapter<AllAdapter.TotalHolder> {

        Context context;
        ArrayList<AllListDatum> list;
        Views views = new Views();

        public AllAdapter(Context context, ArrayList<AllListDatum> list) {
            this.context = context;
            this.list = list;

        }

        @NonNull
        @Override
        public AllAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_all, parent, false);
            return new AllAdapter.TotalHolder(view);

        }

        @Override
        public void onBindViewHolder(final AllAdapter.TotalHolder holder, int position) {
            final AllListDatum totalModel = list.get(position);
            holder.txtBrandAll.setText(totalModel.getBrandName());
            holder.txtModelAll.setText(totalModel.getModelName());
            holder.txtGBAll.setText(totalModel.getGb());
//        holder.txtPriceAll.setText("₹"+(totalModel.getPurchaseAmount())+"/-");
            holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
            holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan() );
            holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName() );

            holder.scanButtonAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int phoneId = totalModel.getId();
                    final int position = holder.getAdapterPosition();
                    final String barcode = totalModel.getBarcodeScan();

                    if(scanValue==1){
                        MyThread1 t1=new MyThread1();
                        t1.start();
                    }


                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.warehouse_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    Button codeSubmitButton = dialog.findViewById(R.id.codeSubmitButton);
                    Button cancelButton = dialog.findViewById(R.id.codecancelButton);
//                    final TextView txtBarcodeSubmit = dialog.findViewById(R.id.txtBarcodeSubmit);

                    dialog.show();
                    codeSubmitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            txtBarcodeSubmit.setText(edtsearchAll.getText().toString());

                            if(edtsearchAll.getText().toString().equals(barcode)){
                                dialog.dismiss();
                                hitApiWarehouse(phoneId, barcode, position);
                            }else {
                                Toast.makeText(context, "Barcode does not Match", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            HomeFragment scanFragment = new HomeFragment();
                            fragmentTransaction.replace(R.id.frame, scanFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            dialog.dismiss();
                        }
                    });

                }
            });

        }

        private void hitApiWarehouse(int phoneId, String code, final int pos) {
//        views.showProgress(context);
            Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build()).addConverterFactory(GsonConverterFactory.create()).build();

            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<SubmitToWareHouseModel> call = api.hitSubmitWarehoueApi(Url.key, String.valueOf(phoneId), code);
            call.enqueue(new Callback<SubmitToWareHouseModel>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<SubmitToWareHouseModel> call, Response<SubmitToWareHouseModel> response) {
//                views.hideProgress();
                    if (response.isSuccessful()) {
                        SubmitToWareHouseModel model = response.body();

                        if(model.getCode().equals("200")){
                            views.showToast(context, model.getMsg());

                            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            HomeFragment scanFragment = new HomeFragment();
                            fragmentTransaction.replace(R.id.frame, scanFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            // removeItem(pos);
                        }else {
                            views.showToast(context, model.getMsg());
                        }

                    }
                }

                @Override
                public void onFailure(Call<SubmitToWareHouseModel> call, Throwable t) {
//                views.hideProgress();
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

            Button scanButtonAll;
            TextView txtBrandAll, txtModelAll, txtGBAll, txtNameAll,txtBarcodeAll,txtCategoryAll;
            ImageView imgPhone;

            public TotalHolder(@NonNull View itemView) {
                super(itemView);
                scanButtonAll = itemView.findViewById(R.id.scanButtonAll);
                txtBrandAll = itemView.findViewById(R.id.txtBrandAll);
                txtModelAll = itemView.findViewById(R.id.txtModelAll);
                txtGBAll = itemView.findViewById(R.id.txtGBAll);
//            txtPriceAll = itemView.findViewById(R.id.txtPriceAll);
                txtNameAll = itemView.findViewById(R.id.txtNameAll);
                imgPhone = itemView.findViewById(R.id.imgPhone);
                txtBarcodeAll = itemView.findViewById(R.id.txtBarcodeAll);
                txtCategoryAll = itemView.findViewById(R.id.txtCategoryAll);

            }
        }
    }

    class MyThread1 extends Thread{

        public void run(){
            synchronized (this){
                try{
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ScanFragment scanFragment = new ScanFragment();
                    fragmentTransaction.add(R.id.frame, scanFragment);
                    fragmentTransaction.commit();
                }catch (Exception e){
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

}
