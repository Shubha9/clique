package com.us.clique.people.follow;

import androidx.lifecycle.MutableLiveData;

import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.networkUtils.ServerResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowRepo {
    FollowNavigator followNavigator;

    private Api api;
    public FollowRepo(){
        api = ApiClient.getClient().create(Api.class);
    }
    MutableLiveData<ServerResponse<String>> followMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ServerResponse<String>> request(String accesToken, JsonObject request) {

        followMutableLiveData.setValue(ServerResponse.loading(null));
        api.request(accesToken, "application/json",request).enqueue(new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        followMutableLiveData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));
                    }else {
                        followMutableLiveData.setValue(ServerResponse.error(response.body().getStatusMessage(), ""));
                    }
                } else {
                    switch (response.code()) {
                        case 400:
                            followMutableLiveData.setValue(ServerResponse.error("Bad request", null));
                            break;
                        case 404:
                            followMutableLiveData.setValue(ServerResponse.error("Not Found", null));
                            break;
                        case 403:
                            followMutableLiveData.setValue(ServerResponse.error("Forbidden", null));
                            break;
                        case 500:
                            followMutableLiveData.setValue(ServerResponse.error("Something went wrong", null));
                            break;
                        default:
                            followMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));
                            break;

                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse<String>>call, Throwable t) {
                followMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));

            }
        });
        return followMutableLiveData;

    }

}
