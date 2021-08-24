package com.us.clique.bottomNavigation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.adapter.ReportAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.fragments.module.ReportModel;
import com.us.clique.databinding.FragmentReportBinding;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ReportFragment extends Fragment implements View.OnClickListener{
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentReportBinding binding;
    ReportAdapter reportAdapter;
    ArrayList<ReportModel> reportModels = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setReportFragment(bottomNavigationViewModel);
        //initRecycler();
        return binding.getRoot();
    }


    public  void onBackFragmentClose(){
       // ((ReportFragment)getParentFragment()).CloseFragment();
        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onBackPress:
                onBackFragmentClose();
                break;
        }
    }
}
