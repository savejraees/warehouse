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
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.StatusAllQC;

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
public class CustomerUsedFragment extends Fragment implements RecyclerView.OnScrollChangeListener, AdapterView.OnItemSelectedListener {

    public CustomerUsedFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    OpenBoxAdapter adapter;
    RecyclerView rvCustomerUsed;
    LinearLayoutManager layoutManager;
    ArrayList<AllQCDatum> listData = new ArrayList<>();
    ArrayList<AllQCDatum> listData2 = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    SessonManager sessonManager;
    Call<StatusAllQC> call;
    EditText edtsearchCustomerUsed;
    ImageView imgScanCustomerUsed;
    TextView txtClear;

    Spinner customerSpinner;
    String[] spinnerData = {"Select Category","store", "warehouse"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_customer_used, container, false);

        views = new Views();
        rvCustomerUsed = view.findViewById(R.id.rvCustomerUsed);
        edtsearchCustomerUsed = view.findViewById(R.id.edtsearchCustomerUsed);
        imgScanCustomerUsed = view.findViewById(R.id.imgScanCustomerUsed);
        txtClear = view.findViewById(R.id.txtClearCustomerUsed);
        customerSpinner = view.findViewById(R.id.customerSpinner);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvCustomerUsed.setLayoutManager(layoutManager);
        sessonManager = new SessonManager(getActivity());

        customerSpinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        customerSpinner.setAdapter(aa);

        hitApi();

        rvCustomerUsed.setOnScrollChangeListener(this);
        adapter = new OpenBoxAdapter(getActivity(), listData2);
        rvCustomerUsed.setAdapter(adapter);

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CustomerUsedFragment scanFragment = new CustomerUsedFragment();
                fragmentTransaction.replace(R.id.frame, scanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        imgScanCustomerUsed.setOnClickListener(new View.OnClickListener() {
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
        if (isLastItemDisplaying(rvCustomerUsed)) {
            if (currentPage <= totalPage && listData2.size() > 1) {
                hitApi();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //  sessonManager.setBarcode();
        edtsearchCustomerUsed.setText(((MainActivity) getActivity()).barcode);

        if(edtsearchCustomerUsed.getText().toString().equals("")){
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

        //////////// for Enable the button of submit after upload image
        new AllQCDatum().setImgEnable(false);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        if(((MainActivity)getActivity()).barcode.equals("")||((MainActivity)getActivity()).barcode.isEmpty()){

            call = api.hitOpenBoxApi(Url.key, String.valueOf(currentPage), "customerUsed");
        }else
        {

            currentPage =1;
            call = api.hitOpenBoxApiSearch(Url.key, String.valueOf(currentPage), "customerUsed",((MainActivity) getActivity()).barcode);
        }

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
        //  spinnerValueOpenBox = spinnerData[i];
        if(i==0){
            new OpenBoxFragment().spinnerValueOpenBox = "Select Category";
        }
        else if(i==1){
            new OpenBoxFragment().spinnerValueOpenBox = "store";
        }
        else if(i==2){
            new OpenBoxFragment().spinnerValueOpenBox = "warehouse";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
