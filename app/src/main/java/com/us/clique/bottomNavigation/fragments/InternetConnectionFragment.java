package com.us.clique.bottomNavigation.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.us.clique.R;
import com.us.clique.activites.BaseActivity;
import com.us.clique.databinding.FragmentInternetConnectionBinding;

public class InternetConnectionFragment extends DialogFragment implements View.OnClickListener {
    FragmentInternetConnectionBinding binding;
Context context;
    BaseActivity baseActivity = new BaseActivity();

    public InternetConnectionFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_internet_connection, container, false);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        context = getContext();
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.ivOnBackPress.setOnClickListener(this);
        binding.btRefresh.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_refresh:
                dismiss();
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
                break;
            case R.id.iv_onBackPress:
                dismiss();
                break;
        }
    }
}