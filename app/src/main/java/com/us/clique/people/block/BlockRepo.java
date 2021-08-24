package com.us.clique.people.block;

import androidx.lifecycle.MutableLiveData;

import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.networkUtils.ServerResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlockRepo {
    BlockNavigator peopleNavigator;

    private Api api;
    public BlockRepo(){
        api = ApiClient.getClient().create(Api.class);
    }
    MutableLiveData<ServerResponse<String>> genderMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ServerResponse<String>> block(String accesToken, JsonObject type) {

        genderMutableLiveData.setValue(ServerResponse.loading(null));
        api.block(accesToken, "application/json",type).enqueue(new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        genderMutableLiveData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));
                    }else {
                        genderMutableLiveData.setValue(ServerResponse.error(response.body().getStatusMessage(), ""));
                    }
                } else {
                    switch (response.code()) {
                        case 400:
                            genderMutableLiveData.setValue(ServerResponse.error("Bad request", null));
                            break;
                        case 404:
                            genderMutableLiveData.setValue(ServerResponse.error("Not Found", null));
                            break;
                        case 403:
                            genderMutableLiveData.setValue(ServerResponse.error("Forbidden", null));
                            break;
                        case 500:
                            genderMutableLiveData.setValue(ServerResponse.error("Something went wrong", null));
                            break;
                        default:
                            genderMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));
                            break;

                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse<String>>call, Throwable t) {
                genderMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));

            }
        });
        return genderMutableLiveData;

    }

}
