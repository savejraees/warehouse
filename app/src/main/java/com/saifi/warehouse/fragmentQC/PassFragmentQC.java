package com.saifi.warehouse.fragmentQC;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.AllAdapterQC;
import com.saifi.warehouse.adapter.PassQcAdapter;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.StatusAllQC;

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
public class PassFragmentQC extends Fragment implements RecyclerView.OnScrollChangeListener, AdapterView.OnItemSelectedListener {

    public PassFragmentQC() {
        // Required empty public constructor
    }

    Views views;
    View view;
    PassQcAdapter adapter;
    RecyclerView rvAll;
    LinearLayoutManager layoutManager;
    ArrayList<AllQCDatum> listData = new ArrayList<>();
    ArrayList<AllQCDatum> listData2 = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    SessonManager sessonManager;
    Call<StatusAllQC> call;
    Spinner qcPassSpinner;
    String[] spinnerData = {"Select Category","Open Box", "Refurbisd", "QC Fail", "Customer Used"};
    public static String spinnerValue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qc_pass, container, false);
        views = new Views();
        rvAll = view.findViewById(R.id.rvAllPass);
        qcPassSpinner = view.findViewById(R.id.qcPassSpinner);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvAll.setLayoutManager(layoutManager);
        sessonManager = new SessonManager(getActivity());

        qcPassSpinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        qcPassSpinner.setAdapter(aa);

        hitApi();

        rvAll.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new PassQcAdapter(getActivity(), listData2);
        //Adding adapter to recyclerview
        rvAll.setAdapter(adapter);
        return view;
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
            if (currentPage <= totalPage && listData2.size() > 1) {
                hitApi();
            }

        }
    }

    private void hitApi() {
        views.showProgress(getActivity());
        listData.clear();
        //   listDataSearch.clear();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                 .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        call = api.hitAllQCApi(Url.key, String.valueOf(currentPage), "5");
        call.enqueue(new Callback<StatusAllQC>() {
            @Override
            public void onResponse(Call<StatusAllQC> call, Response<StatusAllQC> response) {
                views.hideProgress();

                if (response.isSuccessful()) {
                    StatusAllQC model = response.body();
                    totalPage = model.getTotalPages();

                    listData = model.getData();
                    listData2.addAll(listData);
                    adapter.notifyDataSetChanged();

                    currentPage = currentPage + 1;


                } else {
                    views.showToast(getActivity(), String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<StatusAllQC> call, Throwable t) {
                views.hideProgress();
                views.showToast(getActivity(), t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

      //  spinnerValue = spinnerData[i];
        if(i==0){
            spinnerValue = "Select Category";
        }
        else if(i==1){
            spinnerValue = "OpenBox";
        }
        else if(i==2){
            spinnerValue = "Refurbised";
        }
        else if(i==3){
            spinnerValue = "qcfail";
        }
        else if(i==4){
            spinnerValue = "CustomerUsed";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
