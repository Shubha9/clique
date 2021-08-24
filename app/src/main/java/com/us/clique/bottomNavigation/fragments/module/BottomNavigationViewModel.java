package com.us.clique.bottomNavigation.fragments.module;

import androidx.lifecycle.ViewModel;

public class BottomNavigationViewModel extends ViewModel {
    private BottomNavigationRepo bottomNavigationRepo;
    BottomNavigationNavigator bottomNavigationNavigator;


    public void add(){
        bottomNavigationRepo.addReminder();
    }

    public BottomNavigationViewModel(BottomNavigationRepo bottomNavigationRepo) {
        this.bottomNavigationRepo = bottomNavigationRepo;
    }
    public void setNavigator(BottomNavigationNavigator bottomNavigationNavigator) {
        this.bottomNavigationNavigator = bottomNavigationNavigator;
    }

    public void onClickLamda(){
        bottomNavigationNavigator.goToNext();
    }

}
