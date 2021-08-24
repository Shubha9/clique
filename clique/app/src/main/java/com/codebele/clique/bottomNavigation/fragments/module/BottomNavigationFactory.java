package com.us.clique.bottomNavigation.fragments.module;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BottomNavigationFactory implements ViewModelProvider.Factory {
    BottomNavigationRepo bottomNavigationRepo ;

    public BottomNavigationFactory(BottomNavigationRepo bottomNavigationRepo) {
        this.bottomNavigationRepo = bottomNavigationRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BottomNavigationViewModel.class)) {
            return (T) new BottomNavigationViewModel(bottomNavigationRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
