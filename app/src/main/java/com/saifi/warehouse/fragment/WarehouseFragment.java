package com.saifi.warehouse.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saifi.warehouse.MainActivity;
import com.saifi.warehouse.R;
import com.saifi.warehouse.adapter.AllAdapterQC;
import com.saifi.warehouse.adapter.WarehouseAdapter;
import com.saifi.warehouse.constant.ApiInterface;
import com.saifi.warehouse.constant.ScanFragment;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.constant.Url;
import com.saifi.warehouse.constant.Views;
import com.saifi.warehouse.fragmentQC.AllFragmentQC;
import com.saifi.warehouse.model.WarehouseSpinnerModel;
import com.saifi.warehouse.retrofitmodel.qcModel.AllQCDatum;
import com.saifi.warehouse.retrofitmodel.qcModel.StatusAllQC;
import com.saifi.warehouse.retrofitmodel.storeModel.StatusTabModel;
import com.saifi.warehouse.retrofitmodel.storeModel.StoreDatumTabModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saifi.warehouse.constant.SSlHandshake.getUnsafeOkHttpClient;


@RequiresApi(api = Build.VERSION_CODES.M)
public class WarehouseFragment extends Fragment implements RecyclerView.OnScrollChangeListener {

    View view;
    Views views;
    ArrayList<WarehouseSpinnerModel> listDatum=new ArrayList<>();
    ArrayList<String> listDatum2=new ArrayList<>();
    Spinner warehouseSpinner;
    static public String ID_warehouseSpinner;

    RecyclerView rvAll;
    LinearLayoutManager layoutManager;
    ArrayList<AllQCDatum> listData = new ArrayList<>();
    ArrayList<AllQCDatum> listData2 = new ArrayList<>();
    int currentPage = 1;
    int totalPage;
    SessonManager sessonManager;
    Call<StatusAllQC> call;
    EditText edtsearchAll;
    ImageView imgScanAll;
    TextView txtClear;
    WarehouseAdapter adapter;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_warehouse, container, false);
        warehouseSpinner = view.findViewById(R.id.warehouseSpinner);
        views = new Views();



        rvAll = view.findViewById(R.id.rvWarehouse);
        edtsearchAll = view.findViewById(R.id.edtsearchWarehouse);
        imgScanAll = view.findViewById(R.id.imgScanWarehse);
        txtClear = view.findViewById(R.id.txtClearwareHouse);
        layoutManager = new GridLayoutManager(getContext(), 1);
        rvAll.setLayoutManager(layoutManager);
        sessonManager = new SessonManager(getActivity());

        hitSpinnerApi();
        spinnerClick();



        rvAll.setOnScrollChangeListener(this);
        //initializing our adapter
        adapter = new WarehouseAdapter(getActivity(), listData2);
        //Adding adapter to recyclerview
        rvAll.setAdapter(adapter);

        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WarehouseFragment scanFragment = new WarehouseFragment();
                fragmentTransaction.replace(R.id.frame, scanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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

    private void spinnerClick() {
        warehouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ID_warehouseSpinner = (listDatum.get(i).getId());
                Log.d("statessda", String.valueOf(ID_warehouseSpinner));

                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#5F0619"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
            if (currentPage <= totalPage && listData2.size() > 1) {
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
        //   listDataSearch.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(Url.BASE_URL).client(getUnsafeOkHttpClient().build())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);


        if(((MainActivity)getActivity()).barcode.equals("")||((MainActivity)getActivity()).barcode.isEmpty()){

            call = api.hitAllQCApi(Url.key, String.valueOf(currentPage), "8");
        }else
        {

            currentPage =1;
            call = api.hitAllQCApiSearch(Url.key, String.valueOf(currentPage), "8",((MainActivity) getActivity()).barcode);
        }

        call.enqueue(new Callback<StatusAllQC>() {
            @Override
            public void onResponse(Call<StatusAllQC> call, retrofit2.Response<StatusAllQC> response) {
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

    private void  hitSpinnerApi() {
        views.showProgress(getActivity());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, Url.BASE_URL+"getbusiness", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("responseState", response);
                try {
                    views.hideProgress();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    listDatum2.add("Warehouse");
                    WarehouseSpinnerModel model = new WarehouseSpinnerModel();
                    model.setBusiness_location("Warehouse");
                    model.setId("");
                    listDatum.add(model);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new WarehouseSpinnerModel();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        model.setId(jsonObject1.getString("id"));
                        model.setBusiness_location(jsonObject1.getString("business_location"));
                        model.setCdate("cdate");
                        model.setStatus("status");
                        listDatum.add(model);
                        listDatum2.add(jsonObject1.getString("business_location"));
                    }


                    ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listDatum2);
                    warehouseSpinner.setAdapter(aa);

                    hitApi();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                views.hideProgress();
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", Url.key);
                return hashMap;
            }
        };
        queue.add(request);

    }

}
