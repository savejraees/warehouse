package com.saifi.warehouse.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabLayout;
import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.retrofitmodel.rcoModel.RCO_Datum;
import com.saifi.warehouse.retrofitmodel.storeModel.StatusTabModel;
import com.saifi.warehouse.retrofitmodel.storeModel.StoreDatumTabModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;

public class WarehouseTestFragment extends Fragment {


    View view;
    Views views;
    TabLayout tab;
    ViewPager viewPager;
    ArrayList<StoreDatumTabModel> listDatum;
    String fragmentName="";
    int IdFrag=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_warehouse_test, container, false);
        tab = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.frameLayout);
        views = new Views();
        hitTabApi();

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentName = listDatum.get(tab.getPosition()).getBusinessLocation();
                IdFrag = listDatum.get(tab.getPosition()).getId();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }



    public class PlansPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public PlansPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return DynamicTabFragment.newInstance(fragmentName,
                    IdFrag);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    private void hitTabApi() {
        views.showProgress(getActivity());
        Retrofit retrofit = new Retrofit.Builder() .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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

                        for (int k = 0; k <listDatum.size(); k++) {
                            tab.addTab(tab.newTab().setText("" + listDatum.get(k).getBusinessLocation()));
                        }

                        PlansPagerAdapter adapter = new PlansPagerAdapter
                                (getActivity().getSupportFragmentManager(), listDatum.size());
                        viewPager.setAdapter(adapter);
                        viewPager.setOffscreenPageLimit(1);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        views.showToast(getActivity(), model.getMsg());


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


}
