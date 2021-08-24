package com.us.clique.people.block;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.us.clique.networkUtils.ServerResponse;
import com.google.gson.JsonObject;


public class BlockViewModel extends ViewModel {
    private BlockRepo blockRepo;
    public MutableLiveData<ServerResponse<String>> block;
    public MutableLiveData<ServerResponse<String>> type;
    BlockNavigator blockNavigator;

    public BlockViewModel(BlockRepo blockRepo) {
        this.blockRepo = blockRepo;
    }
    public void setBlockNavigator(BlockNavigator blockNavigator){
        this.blockNavigator = blockNavigator;
    }

    public void callBlock(String access_token, JsonObject type){
        block = blockRepo.block(access_token,type);
    }
    public LiveData<ServerResponse<String>> getPeople(){
        return block;
    }

}
