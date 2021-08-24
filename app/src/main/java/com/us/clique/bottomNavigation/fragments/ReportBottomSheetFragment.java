package com.us.clique.bottomNavigation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.FragmentBottomSheetReportBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class
ReportBottomSheetFragment extends BottomSheetBlockFragment{
    public static final String TAG = "ActionBottomDialog";
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentBottomSheetReportBinding binding;
    PeopelFragment peopelFragment ;
    BottomNavigationActivity bottomNavigationActivity;
    String text="Fake Profile";

    public static ReportBottomSheetFragment newInstance() {
        return new ReportBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        PeopelFragment peopelFragment= (PeopelFragment) getParentFragment();

        AndroidSupportInjection.inject(this);
        bottomNavigationActivity = (BottomNavigationActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_report, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setReportPopup(bottomNavigationViewModel);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        binding.btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog

                bottomNavigationActivity.getFrag("Report", text);
                dismiss();
            }


        });
        binding.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvFakeprofile.getText().toString();



            }
        });
        binding.ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvTxt.getText().toString();



            }
        });
        binding.ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvScam.getText().toString();


            }
        });
        binding.ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvSpeech.getText().toString();



            }
        });
        binding.ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvBumble.getText().toString();



            }
        });
        binding.ll7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvAge.getText().toString();



            }
        });
        binding.ll8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                text= binding.tvNotIntrest.getText().toString();



            }
        });
//        binding.ll8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // dismiss dialog
//                text= binding.tvTxt.getText().toString();
//
//
//
//            }
//        });
        return binding.getRoot();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }
}

