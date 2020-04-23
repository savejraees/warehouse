package com.saifi.warehouse.fragmentTotalPurchase;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.AllAdapter;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.AllListDatum;
import com.saifi.warehouse.retrofitmodel.AllStatusModel;

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

        hitApi();

        rvAll.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new AllAdapter(getActivity(), listData2);
        //Adding adapter to recyclerview
        rvAll.setAdapter(adapter);

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtsearchAll.getText().clear();
                ((MainActivity)getActivity()).barcode ="";
               // edtsearchAll.setHint("Search Here");
            }
        });
        imgScanAll.setOnClickListener(new View.OnClickListener() {
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

    private void hitApi() {
        views.showProgress(getActivity());
        listData.clear();
     //   listDataSearch.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        if(((MainActivity)getActivity()).barcode.equals("")||((MainActivity)getActivity()).barcode.isEmpty()){
            call = api.hitAllApi(Url.key, String.valueOf(currentPage), "3", "all");
        }else
        {
            currentPage =1;
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

                    if(listData2.size()>8){
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
            if (currentPage <= totalPage && listData2.size()>5) {
                hitApi();
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        edtsearchAll.setText(((MainActivity) getActivity()).barcode);
        if(((MainActivity) getActivity()).barcode.equals("")||((MainActivity) getActivity()).barcode.isEmpty()||((MainActivity) getActivity()).barcode==null){
        }else {
            listData2.clear();
            hitApi();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Toast.makeText(getActivity(), "pause", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).barcode="";
    }

    private void hitApiSearch() {
        views.showProgress(getActivity());
      //  listDataSearch.clear();
        currentPage =1;

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        call = api.hitAllApiSearch(Url.key, String.valueOf(currentPage), "3", "all",((MainActivity) getActivity()).barcode);


        call.enqueue(new Callback<AllStatusModel>() {
            @Override
            public void onResponse(Call<AllStatusModel> call, Response<AllStatusModel> response) {
                views.hideProgress();

                if (response.isSuccessful()) {

                    AllStatusModel model = response.body();
//                    totalPage = model.getTotalPages();

                    Toast.makeText(getActivity(), ""+model.getMsg(), Toast.LENGTH_SHORT).show();

                 //   listDataSearch = model.getData();

                    layoutManager = new GridLayoutManager(getContext(), 1);
                    rvAll.setLayoutManager(layoutManager);
                 //   searchAdapeter = new AllAdapter(getActivity(), listDataSearch);
                    rvAll.setAdapter(searchAdapeter);


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

}
