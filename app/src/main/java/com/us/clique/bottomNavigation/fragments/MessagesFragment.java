package com.us.clique.bottomNavigation.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.us.clique.R;
import com.us.clique.adapter.MessagesTabAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.FragmentMessagesBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MessagesFragment  extends Fragment {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentMessagesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setMessagesFragment(bottomNavigationViewModel);
        binding.tab.setTabTextColors(Color.parseColor("#A6C1E8"), Color.parseColor("#0053CB"));
        binding.tab.setSelectedTabIndicatorColor(Color.parseColor("#0153CB"));



        init();
        return binding.getRoot();
    }

    private void init(){
        MessagesTabAdapter tabAdapter = new MessagesTabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(new ChatsFragment(),"Chats");
        tabAdapter.addFragment(new RequestsFragment(),"Requests");
        binding.viewPager.setAdapter(tabAdapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
