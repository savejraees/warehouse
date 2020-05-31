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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
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
public class FieldFragment extends Fragment implements RecyclerView.OnScrollChangeListener {

    public FieldFragment() {
        // Required empty public constructor
    }

    View view;
    Views views;

    RecyclerView rvField;
    String barcodeScan = null;
    LinearLayoutManager layoutManager;
    FieldAdapter adapter;
    ArrayList<AllListDatum> listData = new ArrayList<>();
    ArrayList<AllListDatum> listData2 = new ArrayList<>();
    Call<AllStatusModel> call;
    int currentPage = 1;
    int totalPage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_field, container, false);
        rvField = view.findViewById(R.id.rvField);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvField.setLayoutManager(layoutManager);
        views = new Views();

        hitApi();

        rvField.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new FieldAdapter(getActivity(), listData2);
        //Adding adapter to recyclerview
        rvField.setAdapter(adapter);

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

        call = api.hitAllApi(Url.key, String.valueOf(currentPage), "3", "Field Purchase");

        call.enqueue(new Callback<AllStatusModel>() {
            @Override
            public void onResponse(Call<AllStatusModel> call, Response<AllStatusModel> response) {
                views.hideProgress();

                if (response.isSuccessful()) {
                    AllStatusModel model = response.body();
                    totalPage = model.getTotalPages();

                    views.showToast(getActivity(), model.getMsg());
                    listData = model.getData();
                    listData2.addAll(listData);
                    adapter.notifyDataSetChanged();

                    currentPage = currentPage + 1;
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
        if (isLastItemDisplaying(rvField)) {
            if (currentPage < totalPage) {
                hitApi();
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        barcodeScan = ((MainActivity) getActivity()).barcode;

    }

    public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.TotalHolder> {

        Context context;
        ArrayList<AllListDatum> list;
        Views views = new Views();

        public FieldAdapter(Context context, ArrayList<AllListDatum> list) {
            this.context = context;
            this.list = list;

        }

        @NonNull
        @Override
        public FieldAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_all, parent, false);
            return new FieldAdapter.TotalHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull final FieldAdapter.TotalHolder holder, int position) {
            final AllListDatum totalModel = list.get(position);
            holder.txtBrandAll.setText(totalModel.getBrandName());
            holder.txtModelAll.setText(totalModel.getModelName());
            holder.txtGBAll.setText(totalModel.getGb());
            holder.txtNameAll.setText("Purchased By(" + totalModel.getName() + ")");
            holder.txtBarcodeAll.setText("Barcode no : " + totalModel.getBarcodeScan());
            holder.txtCategoryAll.setText("Purchase Category : " + totalModel.getPurchaseCatName());

            holder.scanButtonAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int phoneId = totalModel.getId();
                    final int position = holder.getAdapterPosition();
                    final String barcode = totalModel.getBarcodeScan();

                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ScanFragment scanFragment = new ScanFragment();
                    fragmentTransaction.add(R.id.frame, scanFragment);
                    fragmentTransaction.commit();

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.warehouse_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    Button codeSubmitButton = dialog.findViewById(R.id.codeSubmitButton);
                    Button cancelButton = dialog.findViewById(R.id.codecancelButton);
                    //  final TextView txtBarcodeSubmit = dialog.findViewById(R.id.txtBarcodeSubmit);
                    dialog.show();

                    codeSubmitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(barcodeScan==null){
                                Toast.makeText(getActivity(), "not a Valid barcode", Toast.LENGTH_SHORT).show();
                            }
                            else if (barcodeScan.equals(barcode)) {
                                dialog.dismiss();
                                hitApiWarehouse(phoneId, barcode, position);
                            } else {
                                Toast.makeText(getActivity(), "Barcode does not Match", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            ((MainActivity)getActivity()).barcode="";
                        }
                    });

                }
            });

        }

        private void hitApiWarehouse(int phoneId, String code, final int pos) {
            views.showProgress(context);
            Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build()).addConverterFactory(GsonConverterFactory.create()).build();

            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<SubmitToWareHouseModel> call = api.hitSubmitWarehoueApi(Url.key, String.valueOf(phoneId), code);
            call.enqueue(new Callback<SubmitToWareHouseModel>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<SubmitToWareHouseModel> call, Response<SubmitToWareHouseModel> response) {
                    views.hideProgress();
                    if (response.isSuccessful()) {
                        SubmitToWareHouseModel model = response.body();

                        if (model.getCode().equals("200")) {
                            views.showToast(context, model.getMsg());
                            ((MainActivity)getActivity()).barcode="";
//                            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            DealerFragment scanFragment = new DealerFragment();
//                            fragmentTransaction.replace(R.id.frameLayout, scanFragment);
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
                            removeItem(pos);
                        } else {
                            views.showToast(context, model.getMsg());
                        }

                    }
                }

                @Override
                public void onFailure(Call<SubmitToWareHouseModel> call, Throwable t) {
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

            Button scanButtonAll;
            TextView txtBrandAll, txtModelAll, txtGBAll, txtNameAll, txtBarcodeAll, txtCategoryAll;
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

}
