package com.us.clique.activites;
import android.content.Context;
import android.widget.Toast;

import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.modle.NotificationView;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OnlineUserStatus {
    Context context;
    public OnlineUserStatus(Context context) {
        this.context = context;
    }
    public static void API()
    {
        String access_token = "", userId = "";
        UserSessionManager session;
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type","UserActivity");
        Call<NotificationView> call = api.UserOnline(access_token, body);
        call.enqueue(new Callback<NotificationView>() {
            @Override
            public void onResponse(Call<NotificationView> call, Response<NotificationView> response) {

               // Toast.makeText(getApplicationContext(),"Online",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<NotificationView> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Off Online",Toast.LENGTH_LONG).show();
                // showToast(getApplicationContext(), t.toString());

            }
        });
    }
}
