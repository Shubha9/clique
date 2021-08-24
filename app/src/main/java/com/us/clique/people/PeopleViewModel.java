package com.us.clique.people;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.us.clique.networkUtils.ServerResponse;


public class PeopleViewModel extends ViewModel {
    private PeopleRepo peopleRepo;
    public MutableLiveData<ServerResponse<PeoplePojo>> people;
    PeopleNavigator peopleNavigator;

    public PeopleViewModel(PeopleRepo peopleRepo) {
        this.peopleRepo = peopleRepo;
    }
    public void setPeopleNavigator(PeopleNavigator peopleNavigator){
        this.peopleNavigator = peopleNavigator;
    }

    public void callPeopleApi(String access_token){
        people = peopleRepo.peopleview(access_token);
    }
    public LiveData<ServerResponse<PeoplePojo>> getPeople(){
        return people;
    }

}
