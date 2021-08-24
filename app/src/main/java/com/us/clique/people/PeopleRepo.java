package com.us.clique.people;

import androidx.lifecycle.MutableLiveData;

import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.networkUtils.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleRepo {
    PeopleNavigator peopleNavigator;

    private Api api;
    public PeopleRepo(){
        api = ApiClient.getClient().create(Api.class);
    }
    MutableLiveData<ServerResponse<PeoplePojo>> peopleMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ServerResponse<PeoplePojo>> peopleview(String accesToken) {
        peopleMutableLiveData.setValue(ServerResponse.loading(null));
        api.peopleview(accesToken, "application/json","1","user","People","All").enqueue(new Callback<ServerResponse<PeoplePojo>>() {
            @Override
            public void onResponse(Call<ServerResponse<PeoplePojo>> call, Response<ServerResponse<PeoplePojo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        peopleMutableLiveData.setValue(ServerResponse.success(response.body().getData(), ""));
                    }else {
                        peopleMutableLiveData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                    }
                } else {
                    switch (response.code()) {
                        case 400:
                            peopleMutableLiveData.setValue(ServerResponse.error("Bad request", null));
                            break;
                        case 404:
                            peopleMutableLiveData.setValue(ServerResponse.error("Not Found", null));
                            break;
                        case 403:
                            peopleMutableLiveData.setValue(ServerResponse.error("Forbidden", null));
                            break;
                        case 500:
                            peopleMutableLiveData.setValue(ServerResponse.error("Something went wrong", null));
                            break;
                        default:
                            peopleMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));
                            break;

                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse<PeoplePojo>>call, Throwable t) {
                peopleMutableLiveData.setValue(ServerResponse.error("Internal Server Error", null));

            }
        });
        return peopleMutableLiveData;

    }

}
