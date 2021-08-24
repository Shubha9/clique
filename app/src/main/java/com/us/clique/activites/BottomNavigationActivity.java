package com.us.clique.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.us.clique.R;
import com.us.clique.bottomNavigation.fragments.CreateExperienceFragment;
import com.us.clique.bottomNavigation.fragments.HomeFragment;
import com.us.clique.bottomNavigation.fragments.MessagesFragment;
import com.us.clique.bottomNavigation.fragments.ProfileFragment;
import com.us.clique.bottomNavigation.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ToolTipsManager.TipListener {
    BottomNavigationView bottomNavigation;
    public Context  context ;
    private static final int HEIGHT_INDICATOR = 4;
    HomeFragment homeFragment;
    SharedPreferences preferences;
    RelativeLayout relativeLayout;
    ToolTipsManager toolTipsManager;
    TextView textView,textView1,textView2,textView3,textView4,textView5;
    int position = ToolTip.POSITION_ABOVE;
    int align = ToolTip.ALIGN_RIGHT;
    int align2 = ToolTip.ALIGN_LEFT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        context= getApplicationContext();

            bottomNavigation.setSelectedItemId(R.id.navigation_home);

        preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        bottomNavigation.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        relativeLayout = findViewById(R.id.relative_bottom_menu);

        toolTipsManager = new ToolTipsManager(this);

//        View view_txt = findViewById(R.id.navigation_home);
//        String tooltipMsg = "Search for experiences";
////        if (preferences.getBoolean("firstStart", true)) {
//        ToolTip.Builder builder = new ToolTip.Builder(this, view_txt, relativeLayout,
//                tooltipMsg, ToolTip.POSITION_ABOVE);
//
////        toolTipsManager.findAndDismiss(view_txt);
//            builder.setAlign(ToolTip.ALIGN_RIGHT);
//            builder.setGravity(ToolTip.GRAVITY_CENTER);
//
//            builder.setBackgroundColor(getResources().getColor(R.color.blue));
//            //        builder.setTextAppearance(R.font.manrope_bold);
//            toolTipsManager.show(builder.build());

        bottomNavigation.setItemIconTintList(null);
        checkconnection(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       // Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                 homeFragment=new HomeFragment();
//                checkconnection(this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,homeFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
               /* Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.navigation_search:
//                checkconnection(this);
                SearchFragment searchFragment=new SearchFragment();
                FragmentTransaction transactionsearch = getSupportFragmentManager().beginTransaction();
                transactionsearch.replace(R.id.fragment_container,searchFragment); // give your fragment container id in first parameter
                transactionsearch.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionsearch.commit();
                return true;
            case R.id.navigation_add:
//                checkconnection(this);
                CreateExperienceFragment createExperienceFragment =new CreateExperienceFragment();
                FragmentTransaction transactionadd = getSupportFragmentManager().beginTransaction();
                transactionadd.replace(R.id.fragment_container, createExperienceFragment); // give your fragment container id in first parameter
                transactionadd.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionadd.commit();
                return true;
            case R.id.navigation_messges:
//                checkconnection(this);
                MessagesFragment messagesFragment=new MessagesFragment();
                FragmentTransaction transactionmes = getSupportFragmentManager().beginTransaction();
                transactionmes.replace(R.id.fragment_container,messagesFragment); // give your fragment container id in first parameter
                transactionmes.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionmes.commit();
                return true;
            case R.id.navigation_profile:
//                checkconnection(this);
                ProfileFragment profeleFragment=new ProfileFragment();
                FragmentTransaction transactionprof= getSupportFragmentManager().beginTransaction();
                transactionprof.replace(R.id.fragment_container,profeleFragment); // give your fragment container id in first parameter
                transactionprof.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionprof.commit();
                return true;
        }

        return false;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (preferences.getBoolean("firstStart", true)) {
            View view_txt = findViewById(R.id.navigation_search);
            String tooltipMsg = "Search for experiences";
            ToolTip.Builder builder = new ToolTip.Builder(this, view_txt, relativeLayout, tooltipMsg, ToolTip.POSITION_ABOVE);
            builder.setAlign(ToolTip.ALIGN_RIGHT);
            builder.setGravity(ToolTip.GRAVITY_CENTER);
            builder.setBackgroundColor(getResources().getColor(R.color.blue));
            toolTipsManager.show(builder.build());

            View view_txt2 = findViewById(R.id.navigation_add);
            String tooltipMsg2 = "create a post that others\n" + " can see for an activity you\n" + " want to do";
//        toolTipsManager.findAndDismiss(view_txt2);
            ToolTip.Builder builder2 = new ToolTip.Builder(this, view_txt2, relativeLayout, tooltipMsg2, ToolTip.POSITION_ABOVE);
            builder2.setAlign(ToolTip.ALIGN_LEFT);
            builder2.setGravity(ToolTip.GRAVITY_CENTER);
            builder2.setBackgroundColor(getResources().getColor(R.color.blue));
            //        builder.setTextAppearance(R.font.manrope_bold);
            toolTipsManager.show(builder2.build());
//
//            View view_txt3 = findViewById(R.id.navigation_add);
//            String tooltipMsg3 = "Experiences \n"+" posted by others\n"+"you might like";
////        toolTipsManager.findAndDismiss(view_txt3);
//            ToolTip.Builder builder3 = new ToolTip.Builder(this, view_txt3, relativeLayout, tooltipMsg2, ToolTip.POSITION_ABOVE);
//            builder3.setAlign(ToolTip.ALIGN_LEFT);
//            builder3.setOffsetX(20);
//            builder3.setOffsetY(50);
//            builder3.setGravity(ToolTip.GRAVITY_CENTER);
//            builder3.setBackgroundColor(getResources().getColor(R.color.blue));
//            //        builder.setTextAppearance(R.font.manrope_bold);
//            toolTipsManager.show(builder2.build());
//            SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("firstStart", true);
//            editor.apply();
//
//        }else{}

    }
    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {

    }


    @Override
    public void finishAffinity() {
        super.finishAffinity();
    }


    @Override
    public void onBackPressed() {
        //finishAffinity();

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            super.onBackPressed();
          finishAffinity();
        }else {
            String value=getIntent().getStringExtra("ch_interest");
            if(value!=null) {
                if (value.equals("ch_interest")) {
                    bottomNavigation.setSelectedItemId(R.id.navigation_profile);
                    ProfileFragment profeleFragment = new ProfileFragment();
                    FragmentTransaction transactionprof = getSupportFragmentManager().beginTransaction();
                    transactionprof.replace(R.id.fragment_container, profeleFragment); // give your fragment container id in first parameter
                    transactionprof.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transactionprof.commit();
                } else {
                    bottomNavigation.setSelectedItemId(R.id.navigation_home);
                }
            }else{
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
            }

        }
    }
    public void goHome() {
        //Following code will set the icon of the bottom navigation to active
        MenuItem homeItem = bottomNavigation.getMenu().getItem(0);
        bottomNavigation.setSelectedItemId(homeItem.getItemId());
        getSupportFragmentManager().popBackStackImmediate();

        //To delete all entries from back stack immediately one by one.
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackEntry; i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        //To navigate to the Home Fragment
        final HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction myFragmentTransaction = getSupportFragmentManager().beginTransaction();
        myFragmentTransaction.replace(R.id.fragment_container, homeFragment, "HomeFrag Tag");
        myFragmentTransaction.commit();
    }
    public void getFrag(String block, String text) {
        homeFragment.getPeopleFrag(block,text);
    }


}