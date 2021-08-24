package com.us.clique.networkUtils;

import com.us.clique.bottomNavigation.modle.CalenderResponceModle;
import com.us.clique.bottomNavigation.modle.ChatIndexModle;
import com.us.clique.bottomNavigation.modle.ChatingBodyResponceModle;
import com.us.clique.bottomNavigation.modle.ChatingMessagesModle;
import com.us.clique.bottomNavigation.modle.ChooseYourInterestModle;
import com.us.clique.bottomNavigation.modle.CloneExperinceModle;
import com.us.clique.bottomNavigation.modle.CreateExperiencesModle;
import com.us.clique.bottomNavigation.modle.EditProfileModle;
import com.us.clique.bottomNavigation.modle.EventsAroundYouDetailsModle;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.IpAddressModle;
import com.us.clique.bottomNavigation.modle.ProfileImageDeleteModle;
import com.us.clique.bottomNavigation.modle.ProfileModle;
import com.us.clique.bottomNavigation.modle.ReportPopupModle;
import com.us.clique.bottomNavigation.modle.RequestAcceptedModle;
import com.us.clique.bottomNavigation.modle.RequestToJoinModle;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.bottomNavigation.modle.StagingModle;
import com.us.clique.bottomNavigation.modle.YourExperienceResponseModle;
import com.us.clique.bottomNavigation.modle.YourInterestCategoriesModle;
import com.us.clique.copyPose.CopyPoseModle;
import com.us.clique.copyPose.RefreshPitctureModle;
import com.us.clique.forgotPassword.ForgotPasswordModle;
import com.us.clique.forgotPassword.NewPasswordModle;
import com.us.clique.forgotPassword.ResetOtpModle;
import com.us.clique.joinNow.JoinNowModle;
import com.us.clique.joinNowProfile.JoinNowProfileModle;
import com.us.clique.location.LocationAutoSuggestionModle;
import com.us.clique.location.LocationModle;
import com.us.clique.modle.ConnectAndRejectModle;
import com.us.clique.modle.EditeExperienceModle;
import com.us.clique.modle.EventsAroundYouModle;
import com.us.clique.modle.NotificationView;
import com.us.clique.people.PeoplePojo;
import com.us.clique.signIn.SignInModle;
import com.us.clique.signUp.SignUpModle;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Api {

    @POST("auth/signup")
    Call<SignUpModle> postSignUpResponse(@Header("Accesstoken") String access_token,@Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/signin")
    Call<SignInModle> postSigninResponse(@Header("Accesstoken") String access_token,@Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/forgot_password")
    Call<ForgotPasswordModle> postForgotPasswordResponse(@Header("Accesstoken") String access_token,@Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/forgot_password")
    Call<ResetOtpModle> postResetOtpResponse(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/forgot_password")
    Call<NewPasswordModle> postNewPasswordResponse(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @PUT("auth/user_details")
    Call<JoinNowModle> putJoinNowResponse(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @GET("settings/category_details")
    Call<ChooseYourInterestModle> GetCategoryResponse(@Header("Accesstoken") String access_token, @Header("category_id") String category_id,@Header("category_status") String category_status,@Header("view_type") String view_type);

    @PUT("auth/user_details")
    Call<JoinNowProfileModle> PutJoinProfile(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/signup")
    Call<SignInModle> PostFaceBookLogin(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("auth/signup")
    Call<SignInModle> PostGoogleLogin(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);


    @PUT("auth/user_details")
    Call<YourInterestCategoriesModle> PutYourInterestCategories(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @GET("settings/picture_details")
    Call<RefreshPitctureModle> GetAllRefreshPitcture(@Header("Accesstoken") String access_token, @Header("pic_id") String pic_id, @Header("pic_status") String pic_status, @Header("view_type") String view_type,@Header("limit") String limit);

    @PUT("auth/user_details ")
    Call<CopyPoseModle> PutCopyPose(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @PUT("auth/user_details ")
    Call<LocationModle> PutLocation(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @POST("event/event_details")
    Call<CreateExperiencesModle> PostCreateExperience(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @GET("geo/location_details")
    Call<LocationAutoSuggestionModle> GetLocationList(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("location_id") String location_id, @Header("location_status") String location_status, @Header("view_type") String view_type);

    @GET("auth/user_details")
    Call<ProfileModle> GetProfileDetails(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("userId") String userId, @Header("userStatus") String userStatus, @Header("view_type") String view_type, @Header("user_type") String user_type);

    @PUT("auth/user_details")
    Call<EditProfileModle> PutEditProfile(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Body JsonObject body);

    @GET("event/event_view")
    Call<YourExperienceResponseModle> GetYourExperince(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId,@Header("filter_type") String filter_type, @Header("event_status") String event_status, @Header("event_typ") String event_typ, @Header("event_view") String event_view, @Header("userId") String userId);

    @PUT("event/event_details ")
    Call<EditeExperienceModle> EditeYourExperince(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ,@Body JsonObject body);

    @POST("auth/getPublicIP")
    Call<IpAddressModle> PostIpAddress(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ);

    @GET("event/event_details")
    Call<EventsAroundYouModle> GetEvents(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId, @Header("event_status") String event_status, @Header("event_type") String event_type, @Header("event_view") String event_view, @Header("userId") String userId);

    @GET("event/event_details")
    Call<EventsAroundYouDetailsModle> GetEventsDetails(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId, @Header("event_status") String event_status, @Header("event_type") String event_type, @Header("event_view") String event_view, @Header("userId") String userId);

    @GET("event/event_details")
    Call<ExperinceModle> GetExperinces(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId, @Header("event_status") String event_status, @Header("event_type") String event_type, @Header("event_view") String event_view, @Header("userId") String userId);

    @GET("event/event_details")
    Call<ExperinceModle> GetExperincesDetails(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId, @Header("event_status") String event_status, @Header("event_type") String event_type, @Header("event_view") String event_view, @Header("userId") String userId);

    @POST("requests/join_details")
    Call<RequestToJoinModle> PostRequestToJoin(@Header("Accesstoken") String access_token,@Body JsonObject body);

    @GET("requests/join_details")
    Call<RequestsResponceModle> GetRequestsView(@Header("Accesstoken") String access_token, @Header("requesting_status") String requesting_status, @Header("userId") String userId, @Header("eventId") String eventId);

    @GET("requests/request_details")
    Call<ConnectAndRejectModle> GetConnectAndRejectView(@Header("Accesstoken") String access_token, @Header("following_status") String following_status, @Header("userId") String userId);


    @PUT("requests/join_details")
    Call<RequestAcceptedModle> PutRequestsAccepted(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ,@Body JsonObject body);

    @GET("requests/join_details")
    Call<RequestsResponceModle> GetOthersView(@Header("Accesstoken") String access_token, @Header("requesting_status") String requesting_status, @Header("userId") String userId, @Header("eventId") String eventId);

    @PUT("auth/user_details")
    Call<ProfileImageDeleteModle> DeleteProfilePic(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type,@Body JsonObject body);

    @PUT("event/event_details")
    Call<ExperinceModle> EditeExperince(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ, @Header("eventId") String eventId, @Header("event_status") String event_status, @Header("event_type") String event_type, @Header("event_view") String event_view, @Header("userId") String userId);

    @POST("event/event_view")
    Call<ReportPopupModle> PostReportPopup(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_typ,@Body JsonObject body );

    @GET("chat/chat_details ")
    Call<ChatIndexModle> GetChatIndexModle(@Header("Accesstoken") String access_token, @Header("chat_type") String chat_type, @Header("senderId") String senderId, @Header("recieverId") String recieverId, @Header("limit") String limit);

    @GET("chat/chat_details ")
    Call<ChatingBodyResponceModle> GetChatingBodyModle(@Header("Accesstoken") String access_token, @Header("chat_type") String chat_type, @Header("senderId") String senderId, @Header("recieverId") String recieverId, @Header("limit") String limit);

    @POST("chat/chat_details ")
    Call<ChatingMessagesModle> PostChatingMessages(@Header("Accesstoken") String access_token,@Header("senderId") String senderId, @Header("recieverId") String recieverId, @Header("Content-Type") String Content_Type,@Body JsonObject body);

    @POST("event/clone_events")
    Call<CloneExperinceModle> PostCloneExperince(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type, @Body JsonObject body);

    @GET("event/event_view")
    Call<CalenderResponceModle> GetCalender(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type,@Header("eventId") String eventId,@Header("event_status") String event_status,@Header("event_type") String event_type,@Header("event_view") String event_view,@Header("userId") String userId,@Header("filter_type") String filter_type);

    @GET("event/clone_events ")
    Call<ExperinceModle> GetSearch(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type, @Header("search_word") String search_word);

    @PUT("auth/user_activity")
    Call<NotificationView> UserOnline(@Header("Accesstoken") String access_token, @Body JsonObject body);

    @GET("event/event_view")
    Call<ExperinceModle> filter(@Header("Accesstoken") String access_token,
                                @Header("Content-Type") String Content_Type,
                                @Header("eventId") String eventId,
                                @Header("event_status") String event_status,
                                @Header("event_type") String event_type,
                                @Header("event_view") String event_view,
                                @Header("userId") String userId,
                                @Header("location") String location,
                                @Header("age") String age,
                                @Header("gender") String gender,
                                @Header("category_type") String category_type,
                                @Header("minimum_no") String minno,
                                @Header("maximum_no") String maxno,
                                @Header("event_date") String event_date,
                                @Header("event_time") String time
    );
    @PUT("settings/settings_details")
    Call<NotificationView> deleteaccount(@Header("Accesstoken") String access_token, @Body JsonObject body);

    @POST("settings/settings_details")
    Call<ExperinceModle> Settingdetails(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type, @Body JsonObject body);

    @GET("settings/settings_details")
    Call<NotificationView> Notificationdetails(@Header("Accesstoken") String access_token);

    @GET("event/event_view")
    Call<CalenderResponceModle> GetFilterCalender(@Header("Accesstoken") String access_token, @Header("Content-Type") String Content_Type,@Header("eventId") String eventId,@Header("event_status") String event_status,@Header("event_type") String event_type,@Header("event_view") String event_view,@Header("userId") String userId,@Header("filter_type") String filter_type);

    @GET("auth/user_details")
    Call<ServerResponse<PeoplePojo>>peopleview(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_type, @Header("userStatus") String user_Status, @Header("user_type") String user_type, @Header("view_type") String view_type, @Header("userId") String userId);

    @GET("auth/signup_details")
    Call<StagingModle> GetStaging(@Header("Accesstoken") String access_token, @Header("Content-Type") String content_type, @Header("view_type") String view_type);

    @POST("requests/people_details")
    Call<ServerResponse<String>>block(@Header("Accesstoken") String access_token,@Header("Content-Type") String Content_Type,@Body JsonObject body);

    @POST("requests/request_details")
    Call<ServerResponse<String>>request(@Header("Accesstoken") String access_token,@Header("Content-Type") String Content_Type,@Body JsonObject body);
}
