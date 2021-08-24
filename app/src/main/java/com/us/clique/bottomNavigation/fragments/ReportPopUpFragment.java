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
import com.us.clique.adapter.ExperiencesAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.ReportInterface;
import com.us.clique.databinding.FragmentReportPopupBinding;


import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ReportPopUpFragment extends Fragment implements View.OnClickListener {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentReportPopupBinding binding;
    ExperiencesFragment experiencesFragment;
    ReportInterface mCallBack;
    ExperiencesAdapter experiencesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_popup, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setReportPoupFragment(bottomNavigationViewModel);
       /* binding.rlReport.setOnClickListener(this);
        binding.rlNotIntrest.setOnClickListener(this);*/
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
/*
        switch (v.getId()){
            case R.id.rl_report:
     */
/* *//*
*/
/*          ExperiencesFragment fragment = new ExperiencesFragment();
                fragment.callReportFragment();
*//*
*/
/*
                ExperiencesFragment frag=(ExperiencesFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.frag_experince);
                frag.myMethod();



                // mCallBack.callReport();

                this.experiencesFragment.callReportFragment();
*//*

               */
/* FragmentManager fm = getFragmentManager();
                ExperiencesFragment fragm = (ExperiencesFragment) fm.findFragmentById(R.id.frag_experince);
                fragm.callReportFragment(v);*//*

             */
/*   FragmentTransaction tx = fragmentManager.beginTransation();
                tx.replace( R.id.report, new ReportFragment() ).addToBackStack( "tag" ).commit();
*//*

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_experince, new ReportFragment())
                        .commit();

              */
/*  ExperiencesFragment frag=(ExperiencesFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.frag_experince);
                frag.callReportFragment(v);
*//*

//                experiencesFragment.callReportFragment(v);

                break;

            case R.id.rl_notIntrest:
//                Fragment mF = getParentFragment();
//// double check
//                if (mF instanceof  ExperiencesFragment) {
//                    getFragmentManager().beginTransaction()
//                            .add(((ExperiencesFragment)getParentFragment()).getView().findViewById(R.id.benefitContainer).getId()
//                                    , new CorporateFragment())
//                            .setCustomAnimations(R.anim.slide_in_left, R.anim.do_nothing)
//                            .addToBackStack("benefit")
//                            .commit();
//                }
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_experince, new BlockFragment())
                        .commit();

                break;
        }
*/
    }


}
