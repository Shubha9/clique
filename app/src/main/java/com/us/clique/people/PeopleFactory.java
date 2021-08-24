package com.us.clique.people;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PeopleFactory implements ViewModelProvider.Factory{
    PeopleRepo peopleRepo;

    public PeopleFactory(PeopleRepo peopleRepo) {
        this.peopleRepo = peopleRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PeopleViewModel.class)) {
            return (T) new PeopleViewModel(peopleRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
