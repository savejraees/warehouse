package com.saifi.warehouse.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saifi.warehouse.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RefurbisedFragment extends Fragment {

    public RefurbisedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refurbised, container, false);
    }
}
