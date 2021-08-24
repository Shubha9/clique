package com.us.clique.people.follow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FollowFactory implements ViewModelProvider.Factory{
    FollowRepo followRepo;

    public FollowFactory(FollowRepo followRepo) {
        this.followRepo = followRepo ;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FollowViewModel.class)) {
            return (T) new FollowViewModel(followRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
