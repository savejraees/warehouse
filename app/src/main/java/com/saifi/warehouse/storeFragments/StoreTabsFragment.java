package com.saifi.warehouse.storeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saifi.warehouse.R;
import com.saifi.warehouse.constant.SessonManager;
import com.saifi.warehouse.fragment.StoresFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreTabsFragment extends Fragment {

    int id=0;
    public static StoreTabsFragment newInstance(int id) {
        StoreTabsFragment fragment = new StoreTabsFragment();
        fragment.setData(id);
        return fragment;
    }
    public void setData(int id) {

        this.id = id;

    }

     View view;
    SessonManager sessonManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_tabs, container, false);
//        sessonManager = new SessonManager(getActivity());
//        tabId = sessonManager.getTabId();

//        Toast.makeText(getActivity(), "" + tabId, Toast.LENGTH_SHORT).show();
        Log.d("lpokmm",""+id);
        return view;
    }
}
