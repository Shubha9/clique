package com.us.clique.bottomNavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.us.clique.R;
import com.us.clique.bottomNavigation.fragments.AddFragment;
import com.us.clique.bottomNavigation.fragments.HomeFragment;
import com.us.clique.bottomNavigation.fragments.MessagesFragment;
import com.us.clique.bottomNavigation.fragments.ProfileFragment;
import com.us.clique.bottomNavigation.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
        }

    }
  /*  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            HomeFragment homeFragment=new HomeFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_home,homeFragment); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();

                            return true;
                        case R.id.navigation_search:
                            SearchFragment searchFragment=new SearchFragment();
                            FragmentTransaction transactionsearch = getSupportFragmentManager().beginTransaction();
                            transactionsearch.replace(R.id.fragment_search,searchFragment); // give your fragment container id in first parameter
                            transactionsearch.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transactionsearch.commit();
                            return true;
                        case R.id.navigation_add:
                            AddFragment addFragment=new AddFragment();
                            FragmentTransaction transactionadd = getSupportFragmentManager().beginTransaction();
                            transactionadd.replace(R.id.fragment_add,addFragment); // give your fragment container id in first parameter
                            transactionadd.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transactionadd.commit();
                            return true;
                        case R.id.navigation_messges:
                            MessagesFragment messagesFragment=new MessagesFragment();
                            FragmentTransaction transactionmes = getSupportFragmentManager().beginTransaction();
                            transactionmes.replace(R.id.fragment_messages,messagesFragment); // give your fragment container id in first parameter
                            transactionmes.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transactionmes.commit();
                            return true;
                        case R.id.navigation_profile:
                            ProfileFragment profeleFragment=new ProfileFragment();
                            FragmentTransaction transactionprof= getSupportFragmentManager().beginTransaction();
                            transactionprof.replace(R.id.fragment_profile,profeleFragment); // give your fragment container id in first parameter
                            transactionprof.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transactionprof.commit();
                            return true;
                    }
                    return false;
                }
            };*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       // Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                HomeFragment homeFragment=new HomeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,homeFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

                return true;
            case R.id.navigation_search:
                SearchFragment searchFragment=new SearchFragment();
                FragmentTransaction transactionsearch = getSupportFragmentManager().beginTransaction();
                transactionsearch.replace(R.id.fragment_container,searchFragment); // give your fragment container id in first parameter
                transactionsearch.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionsearch.commit();
                return true;
            case R.id.navigation_add:
                AddFragment addFragment=new AddFragment();
                FragmentTransaction transactionadd = getSupportFragmentManager().beginTransaction();
                transactionadd.replace(R.id.fragment_container,addFragment); // give your fragment container id in first parameter
                transactionadd.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionadd.commit();
                return true;
            case R.id.navigation_messges:
                MessagesFragment messagesFragment=new MessagesFragment();
                FragmentTransaction transactionmes = getSupportFragmentManager().beginTransaction();
                transactionmes.replace(R.id.fragment_container,messagesFragment); // give your fragment container id in first parameter
                transactionmes.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionmes.commit();
                return true;
            case R.id.navigation_profile:
                ProfileFragment profeleFragment=new ProfileFragment();
                FragmentTransaction transactionprof= getSupportFragmentManager().beginTransaction();
                transactionprof.replace(R.id.fragment_container,profeleFragment); // give your fragment container id in first parameter
                transactionprof.addToBackStack(null);  // if written, this transaction will be added to backstack
                transactionprof.commit();
                return true;
        }
/*
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_add:
                fragment = new AddFragment();
                break;

            case R.id.navigation_messges:
                fragment = new MessagesFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;

        }
*/

      //  return loadFragment(fragment);
    //}
   /* private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }*/
        return false;
    }
}