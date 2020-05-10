package com.saifi.warehouse.fragment;

import android.os.Build;
import android.os.Bundle;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.OpenBoxAdapter;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Datum;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Status;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class RefurbisedFragment extends Fragment implements RecyclerView.OnScrollChangeListener, AdapterView.OnItemSelectedListener {

    public RefurbisedFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    OpenBoxAdapter adapter;
    RecyclerView rvRefurbish;
    LinearLayoutManager layoutManager;
    ArrayList<RCO_Datum> listData = new ArrayList<>();
    ArrayList<RCO_Datum> listData2 = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    SessonManager sessonManager;
    Call<RCO_Status> call;
    EditText edtsearchRefurbish;
    ImageView imgScanRefurbish;
    TextView txtClear;

    Spinner refurbishSpinner;
    String[] spinnerData = {"Select Category", "Open Box", "Refurbisd", "QC Fail", "Customer Used"};
    public String fragmentType = "Refubised";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_refurbised, container, false);
        views = new Views();
        rvRefurbish = view.findViewById(R.id.rvRefurbish);
        edtsearchRefurbish = view.findViewById(R.id.edtsearchRefurbish);
        imgScanRefurbish = view.findViewById(R.id.imgScanRefurbish);
        txtClear = view.findViewById(R.id.txtClearRefurbish);
        refurbishSpinner = view.findViewById(R.id.refurbishSpinner);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvRefurbish.setLayoutManager(layoutManager);
        sessonManager = new SessonManager(getActivity());

        refurbishSpinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        refurbishSpinner.setAdapter(aa);

        hitApi();

        rvRefurbish.setOnScrollChangeListener(this);
        adapter = new OpenBoxAdapter(getActivity(), listData2,fragmentType);
        rvRefurbish.setAdapter(adapter);

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RefurbisedFragment scanFragment = new RefurbisedFragment();
                fragmentTransaction.replace(R.id.frame, scanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        imgScanRefurbish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData2.clear();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ScanFragment scanFragment = new ScanFragment();
                fragmentTransaction.add(R.id.frame, scanFragment);
                fragmentTransaction.commit();
            }
        });


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
        if (isLastItemDisplaying(rvRefurbish)) {
            if (currentPage <= totalPage && listData2.size() > 1) {
                hitApi();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //  sessonManager.setBarcode();
        edtsearchRefurbish.setText(((MainActivity) getActivity()).barcode);

        if(edtsearchRefurbish.getText().toString().equals("")){
        }else {
            listData2.clear();
            hitApi();

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ((MainActivity) getActivity()).barcode="";
    }


    private void hitApi() {
        views.showProgress(getActivity());
        listData.clear();
        
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        if(((MainActivity)getActivity()).barcode.equals("")||((MainActivity)getActivity()).barcode.isEmpty()){

            call = api.hitOpenBoxApi(Url.key, String.valueOf(currentPage), "refurbish");
        }else
        {
            currentPage =1;
            call = api.hitOpenBoxApiSearch(Url.key, String.valueOf(currentPage), "refurbish",((MainActivity) getActivity()).barcode);
        }

        call.enqueue(new Callback<RCO_Status>() {
            @Override
            public void onResponse(Call<RCO_Status> call, Response<RCO_Status> response) {
                views.hideProgress();

                if (response.isSuccessful()) {
                    RCO_Status model = response.body();
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
            public void onFailure(Call<RCO_Status> call, Throwable t) {
                views.hideProgress();
                views.showToast(getActivity(), t.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //  spinnerValueOpenBox = spinnerData[i];
        if(i==0){
            new OpenBoxFragment().spinnerValueOpenBox = "Select Category";
        }
        else if(i==1){
            new OpenBoxFragment().spinnerValueOpenBox = "OpenBox";
        }
        else if(i==2){
            new OpenBoxFragment().spinnerValueOpenBox = "Refurbised";
        }
        else if(i==3){
            new OpenBoxFragment().spinnerValueOpenBox = "qcfail";
        }
        else if(i==4){
            new OpenBoxFragment().spinnerValueOpenBox = "CustomerUsed";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
