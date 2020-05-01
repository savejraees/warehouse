package com.saifi.warehouse.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.saifi.warehouse.R;
import com.saifi.warehouse.fragmentQC.AllFragmentQC;
import com.saifi.warehouse.fragmentQC.FailFragmentQC;
import com.saifi.warehouse.fragmentQC.PassFragmentQC;
import com.saifi.warehouse.fragmentTotalPurchase.AllFragment;
import com.saifi.warehouse.fragmentTotalPurchase.DealerFragment;
import com.saifi.warehouse.fragmentTotalPurchase.FieldFragment;
import com.saifi.warehouse.fragmentTotalPurchase.ShopFragment;

public class QCFragment extends Fragment {

    View root;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_qc, container, false);
        tabLayout = root.findViewById(R.id.tabLayoutQC);
        frameLayout = root.findViewById(R.id.frameLayoutQc);


        fragment = new AllFragmentQC();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutQc, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new AllFragmentQC();
                        break;
                    case 1:
                        fragment = new PassFragmentQC();
                        break;
                    case 2:
                        fragment = new FailFragmentQC();
                        break;

                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayoutQc, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }
}
