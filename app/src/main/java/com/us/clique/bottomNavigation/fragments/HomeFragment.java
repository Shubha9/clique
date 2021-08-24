package com.us.clique.bottomNavigation.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.activites.EventsArroundYouActivity;
import com.us.clique.activites.FiltersActivity;
import com.us.clique.adapter.TabAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.FragmentHomeBinding;


import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class HomeFragment  extends Fragment implements View.OnClickListener,ToolTipsManager.TipListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentHomeBinding binding;
    PeopelFragment peopelFragment;
    UserSessionManager session;
    SharedPreferences preferences;
    LinearLayout linearLayout;
    int position = ToolTip.POSITION_BELOW;
    int align = ToolTip.ALIGN_RIGHT;
    int align2 = ToolTip.ALIGN_LEFT;
    ToolTipsManager toolTipsManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setHomeFragment(bottomNavigationViewModel);
linearLayout=view.findViewById(R.id.fragment_home);
        binding.tab.setTabTextColors(Color.parseColor("#A6C1E8"), Color.parseColor("#0053CB"));
        // binding.tab.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
//        binding.tab.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        session = new UserSessionManager(getApplicationContext());
        init();
        binding.rlFilter.setOnClickListener(this);
        binding.rlMessages.setOnClickListener(this);
        binding.tvExp.setOnClickListener(this);
        preferences = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        toolTipsManager=new ToolTipsManager(this);

        String tooltipMsg = "Search for\n" + " experiences";
        View view_txt = view.findViewById(R.id.rl_filter);
        ToolTip.Builder builder = new ToolTip.Builder(getContext(), view_txt,linearLayout, tooltipMsg, position);
        builder.setAlign(align);
        builder.setGravity(ToolTip.GRAVITY_CENTER);
        builder.setBackgroundColor(getResources().getColor(R.color.blue));
        toolTipsManager.show(builder.build());

//        new SimpleTooltip.Builder(getContext())
//                .anchorView(binding.tvPep)
//                .text("Tooltip")
//                .textColor(Color.WHITE)
//                .gravity(Gravity.BOTTOM)
//                .transparentOverlay(true)
//                .arrowColor(Color.parseColor("#0053CB"))
//                .backgroundColor(Color.parseColor("#0053CB"))
//                .onDismissListener(new SimpleTooltip.OnDismissListener() {
//                    @Override
//                    public void onDismiss(SimpleTooltip tooltip) {
//                        System.out.println("dismiss " + tooltip);
//                    }
//                })
//                .onShowListener(new SimpleTooltip.OnShowListener() {
//                    @Override
//                    public void onShow(SimpleTooltip tooltip) {
//                        System.out.println("show " + tooltip);
//                    }
//                })
//                .build()
//                .show();
        return binding.getRoot();
    }

    private void init(){
        peopelFragment =new PeopelFragment();
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(new ExperiencesFragment(),"Experience");
        tabAdapter.addFragment(peopelFragment,"People");
        binding.viewPager.setAdapter(tabAdapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        if(session.getUserProfileCLiked())
            binding.viewPager.setCurrentItem(1);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_filter:
                Intent i = new Intent(getContext(), FiltersActivity.class);
                startActivity(i);
                break;
            case R.id.rl_messages:
                Intent j = new Intent(getContext(), EventsArroundYouActivity.class);
                startActivity(j);
                break;

        }
    }

    public void getPeopleFrag(String block, String text) {
        peopelFragment.callApi(block,text);
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {

    }
}
