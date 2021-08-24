package com.us.clique.bottomNavigation.fragments.module;


import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;

public class BottomNavigationRepo {
    private Api api;

    public BottomNavigationRepo() {
        api = ApiClient.getClient().create(Api.class);
    }

    public void addReminder() {
    }
}
