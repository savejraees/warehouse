package com.saifi.warehouse.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.OpenBoxAdapter;
import com.saifi.warehouse.adapter.StoreTabDataAdapter;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Datum;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Status;
import com.saifi.warehouse.retrofitmodel.storeModel.StatusTabModel;
import com.saifi.warehouse.retrofitmodel.storeModel.StoreDatumTabModel;

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
public class StoresFragment extends Fragment implements RecyclerView.OnScrollChangeListener {

    public StoresFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    ArrayList<StoreDatumTabModel> listDatum;
    RecyclerView rvTab,rvTabData;
    StoreTabAdapter adapter;
    StoreTabDataAdapter storeTabDataAdapter;
    LinearLayoutManager layoutManager;

    ArrayList<RCO_Datum> listData = new ArrayList<>();
    ArrayList<RCO_Datum> listData2 = new ArrayList<>();
    ArrayList<String> listSpinner = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    SessonManager sessonManager;
    Call<RCO_Status> call;

    int ID_TabData;
    Spinner storeSpinner;
    static public int ID_storeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stores, container, false);
        views = new Views();
        rvTab = view.findViewById(R.id.rvTab);
        rvTabData = view.findViewById(R.id.rvTabData);
        storeSpinner = view.findViewById(R.id.storeSpinner);

        layoutManager = new GridLayoutManager(getContext(), 1);
        rvTabData.setLayoutManager(layoutManager);
        rvTabData.setOnScrollChangeListener(this);


        sessonManager = new SessonManager(getActivity());

        hitTabApi();

        storeTabDataAdapter = new StoreTabDataAdapter(getActivity(), listData2);
        rvTabData.setAdapter(storeTabDataAdapter);


        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ID_storeSpinner = (listDatum.get(i).getId());
                Log.d("statesda", String.valueOf(ID_storeSpinner));

                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void hitTabApi() {
        views.showProgress(getActivity());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<StatusTabModel> call = api.hitStatusTabApi(Url.key);
        call.enqueue(new Callback<StatusTabModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<StatusTabModel> call, Response<StatusTabModel> response) {
                views.hideProgress();
                if (response.isSuccessful()) {
                    StatusTabModel model = response.body();
                    listDatum = model.getData();

                    if (model.getCode().equals("200")) {
//                        listSpinner.add("Select Category");
                        for(int i=0;i<listDatum.size();i++){
                            listSpinner.add(listDatum.get(i).getBusinessLocation());
                        }
                        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listSpinner);
                        storeSpinner.setAdapter(aa);

                        views.showToast(getActivity(), model.getMsg());
                        rvTab.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        adapter = new StoreTabAdapter(getActivity(), listDatum);
                        rvTab.setAdapter(adapter);
                    } else {
                        views.showToast(getActivity(), model.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<StatusTabModel> call, Throwable t) {
                views.hideProgress();
                views.showToast(getActivity(), t.getMessage());
            }
        });
    }

    public class StoreTabAdapter extends RecyclerView.Adapter<StoreTabAdapter.TotalHolder> {

        Context context;
        ArrayList<StoreDatumTabModel> list;
        int index = -1;

        public StoreTabAdapter(Context context, ArrayList<StoreDatumTabModel> list) {
            this.context = context;
            this.list = list;

        }

        @NonNull
        @Override
        public StoreTabAdapter.TotalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_tab_store, parent, false);
            return new StoreTabAdapter.TotalHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(@NonNull final StoreTabAdapter.TotalHolder holder, final int position) {
            final StoreDatumTabModel totalModel = list.get(position);
            holder.txtTab.setText(totalModel.getBusinessLocation());

            holder.txtTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listData2.clear();
                    currentPage =1;

                    index = position;
                    notifyDataSetChanged();

                    ID_TabData = totalModel.getId();
                    Log.d("saklajsza",""+ID_TabData);
                    hitTabDataApi();

                }
            });

            if(index==-1){
                StoreDatumTabModel totalModel1 = list.get(0);
                listData2.clear();
                currentPage =1;

                index = 0;

                ID_TabData = totalModel1.getId();
                Log.d("saklajsza",""+ID_TabData);
                hitTabDataApi();
            }

            if (index == position) {
                holder.txtTab.setTextColor(Color.parseColor("#DDA90C"));
            } else {
                holder.txtTab.setTextColor(Color.parseColor("#FFFFFF"));

            }
        }



        @Override
        public int getItemCount() {
            return list.size();
        }


        public class TotalHolder extends RecyclerView.ViewHolder {

            TextView txtTab;


            public TotalHolder(@NonNull View itemView) {
                super(itemView);
                txtTab = itemView.findViewById(R.id.txtTab);

            }
        }
    }

    private void hitTabDataApi() {
        views.showProgress(getActivity());
        listData.clear();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

            call = api.hitStoreHistoryApi(Url.key, String.valueOf(currentPage), String.valueOf(ID_TabData));


        call.enqueue(new Callback<RCO_Status>() {
            @Override
            public void onResponse(Call<RCO_Status> call, Response<RCO_Status> response) {
                views.hideProgress();

                if (response.isSuccessful()) {
                    RCO_Status model = response.body();
                    totalPage = model.getTotalPages();

                    listData = model.getData();
                    listData2.addAll(listData);
                    storeTabDataAdapter.notifyDataSetChanged();

                    currentPage = currentPage + 1;


                } else {
                    views.showToast(getActivity(), String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<RCO_Status> call, Throwable t) {
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
        if (isLastItemDisplaying(rvTabData)) {
            if (currentPage <= totalPage && listData2.size() > 1) {
                hitTabDataApi();

//                storeTabDataAdapter = new StoreTabDataAdapter(getActivity(), listData2);
//                rvTabData.setAdapter(storeTabDataAdapter);
            }
        }
    }


}
