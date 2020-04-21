package com.saifi.warehouse.fragmentTotalPurchase;

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
import android.widget.AbsListView;
import android.widget.Toast;

import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.AllAdapter;
import com.saifi.warehouse.constant.ApiInterface;
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
public class AllFragment extends Fragment implements RecyclerView.OnScrollChangeListener{

    public AllFragment() {
        // Required empty public constructor
    }

    Views views;
    View view;
    AllAdapter adapter;
    RecyclerView rvAll;
    LinearLayoutManager layoutManager;
    ArrayList<AllListDatum> listData = new ArrayList<>();
    ArrayList<AllListDatum> listData2 = new ArrayList<>();
    int currentPage =1;
    int totalPage;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_all, container, false);
        views = new Views();
        rvAll = view.findViewById(R.id.rvAll);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvAll.setLayoutManager(layoutManager);

        hitApi();

        rvAll.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new AllAdapter(getActivity(),listData2);
        //Adding adapter to recyclerview
        rvAll.setAdapter(adapter);

        return view;
    }

    private void hitApi() {
         views.showProgress(getActivity());
         listData.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_URL)
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<AllStatusModel> call = api.hitAllApi(Url.key, String.valueOf(currentPage), "3");

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
                        currentPage = currentPage+1;


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
            if(currentPage<=totalPage){
                hitApi();
            }

        }
    }
}
