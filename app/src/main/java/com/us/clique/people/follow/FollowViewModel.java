package com.us.clique.people.follow;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.us.clique.networkUtils.ServerResponse;
import com.google.gson.JsonObject;


public class FollowViewModel extends ViewModel {
    private FollowRepo followRepo;
    public MutableLiveData<ServerResponse<String>> request;
    FollowNavigator followNavigator;

    public FollowViewModel(FollowRepo followRepo) {
        this.followRepo = followRepo;
    }
    public void setFollowNavigator(FollowNavigator followNavigator){
        this.followNavigator = followNavigator;
    }

    public void callrequest(String access_token, JsonObject type){
        request = followRepo.request(access_token,type);
    }
    public LiveData<ServerResponse<String>> getPeople(){
        return request;
    }

}
