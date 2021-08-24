package com.us.clique.bottomNavigation.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.JsonObject;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.adapter.PeopleAdapter;
import com.us.clique.adapter.PeopleRvAdapter;
import com.us.clique.bottomNavigation.fragments.BottomSheetBlockFragment;
import com.us.clique.bottomNavigation.fragments.ReportBottomSheetFragment;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.FragmentPeopleBinding;
import com.us.clique.networkUtils.ServerResponse;
import com.us.clique.people.PeopleFactory;
import com.us.clique.people.PeoplePojo;
import com.us.clique.people.PeopleViewModel;
import com.us.clique.people.block.BlockFactory;
import com.us.clique.people.block.BlockPojo;
import com.us.clique.people.block.BlockViewModel;
import com.us.clique.people.follow.FollowFactory;
import com.us.clique.people.follow.FollowPojo;
import com.us.clique.people.follow.FollowViewModel;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;




public class PeopelFragment extends Fragment implements View.OnClickListener,ToolTipsManager.TipListener {
    Context context;
    PeoplePojo peoplePojo;
    BlockPojo blockPojo;
    FollowPojo followPojo;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentPeopleBinding binding;
    @Inject
    PeopleFactory peopleFactory;
    PeopleViewModel peopleViewModel;
    BlockViewModel blockViewModel;
    @Inject
    BlockFactory blockFactory;
    @Inject
    FollowFactory followFactory;
    FollowViewModel followViewModel;
    boolean isVisible;
    PeopleRvAdapter peopleRvAdapter;
    PeopleAdapter peopleAdapter;
    String access_token,userid,peopleid;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    BottomNavigationActivity bottomNavigationActivity;
    ArrayList<PeoplePojo> peopleModel = new ArrayList<>();
    List<PeoplePojo.User> catArrayList = new ArrayList<>();
    View view;
    UserSessionManager session;
    int pos=0;
    String text="Fake Profile";
    int position = ToolTip.POSITION_BELOW;
    int align = ToolTip.ALIGN_RIGHT;
    int align2 = ToolTip.ALIGN_LEFT;
    ToolTipsManager toolTipsManager;
    SharedPreferences preferences;
    RelativeLayout relativeLayout;
    RecyclerView.LayoutManager mLayoutManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        bottomNavigationActivity = (BottomNavigationActivity) getActivity();
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_people, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setPeopleFragment(bottomNavigationViewModel);
        peopleViewModel = new ViewModelProvider(this, peopleFactory).get(PeopleViewModel.class);
        blockViewModel = new ViewModelProvider(this, blockFactory).get(BlockViewModel.class);
        followViewModel = new ViewModelProvider(this, followFactory).get(FollowViewModel.class);
        binding.rlPopup.setVisibility(View.GONE);
        binding.rlBlock.setOnClickListener(this);
        binding.rlReport.setOnClickListener(this);
        binding.ivFollowReq.setOnClickListener(this);
        session = new UserSessionManager(bottomNavigationActivity);

        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userid=  session.getUserId();
        getAcessToken();
        preferences = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        toolTipsManager=new ToolTipsManager(this);
//
//        String tooltipMsg = "Search for\n" + " experiences";
//        View view_txt = view.findViewById(R.id.iv_follow_req);
//        ToolTip.Builder builder = new ToolTip.Builder(getContext(), view_txt,binding.rlMain, tooltipMsg, position);
//        builder.setAlign(align);
//        builder.setGravity(ToolTip.GRAVITY_CENTER);
//        builder.setBackgroundColor(getResources().getColor(R.color.blue));
//        toolTipsManager.show(builder.build());

        binding.ivDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisible) {
                    binding.rlPopup.setVisibility(View.VISIBLE);
                    isVisible = true;
                } else {
                    binding.rlPopup.setVisibility(View.GONE);
                    isVisible = false;
                }
            }
        });

        if (binding.recyclerPeople.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.recyclerPeople.getLayoutManager();
            binding.recyclerPeople.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        loading = true;
                    }
                }
            });
        }
        return binding.getRoot();
    }

    private void peoples(PeoplePojo peoplePojo) {
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.recyclerPeople.setLayoutManager(mLayoutManager);
        peopleModel.add(new PeoplePojo());
        peopleModel.add(new PeoplePojo());
        peopleModel.add(new PeoplePojo());

        PeopleAdapter adapter = new PeopleAdapter(context, (ArrayList<PeoplePojo.User>) peoplePojo.getUser(), bottomNavigationActivity.context);
        adapter.notifyDataSetChanged();
        binding.recyclerPeople.setAdapter(adapter);
        binding.recyclerPeople.setOnFlingListener(null);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerPeople);
       //RecyclerView.LayoutManager mLayoutManagersub = new GridLayoutManager(getContext(),2, RecyclerView.HORIZONTAL, false);
     //   binding.recyclerInterest.setLayoutManager(new GridLayoutManager(context, 2));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    //    String[] separated = peoplePojo.getUser().get(0).getUserInterest().split(",");
        for (int k=0; k<peoplePojo.getUser().get(0).getSubCat().size();k++) {
            peoplePojo.getUser().get(0).getSubCat().get(k).setCommon(false);
        }
            for(int i=0;i<peoplePojo.getUser().get(0).getLoginUserSubCat().size();i++) {
            for (int k=0; k<peoplePojo.getUser().get(0).getSubCat().size();k++){
                if(peoplePojo.getUser().get(0).getLoginUserSubCat().get(i).getSkSubCategoryId().equals(peoplePojo.getUser().get(0).getSubCat().get(k).getSkSubCategoryId())){
                    peoplePojo.getUser().get(0).getSubCat().get(k).setCommon(true);
                    break;
                }else{
//                    peoplePojo.getUser().get(0).getSubCat().get(k).setCommon(false);
                }
            }
        }

        binding.recyclerInterest.setLayoutManager(staggeredGridLayoutManager);
        PeopleRvAdapter adaptersub = new PeopleRvAdapter(bottomNavigationActivity, (ArrayList<PeoplePojo.SubCat>) peoplePojo.getUser().get(0).getSubCat(),(ArrayList<PeoplePojo.SubCat>) peoplePojo.getUser().get(0).getLoginUserSubCat());
        binding.recyclerInterest.setAdapter(adaptersub);


        binding.recyclerPeople.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLayoutManager);
                    pos = mLayoutManager.getPosition(centerView);

                    binding.tvRose.setText(peoplePojo.getUser().get(pos).getName());
                    binding.tvAge.setText(String.valueOf(peoplePojo.getUser().get(pos).getAge()));
                    binding.tvKm.setText(String.valueOf(peoplePojo.getUser().get(pos).getDistance()) + " km");
                    binding.tvContent.setText(peoplePojo.getUser().get(pos).getBioData());
                    if (peoplePojo.getUser().get(pos).getCopyposestatus() == null) {
                        binding.ivVery.setVisibility(View.GONE);
                    }else if (peoplePojo.getUser().get(pos).getCopyposestatus() == 0) {
                        binding.ivVery.setVisibility(View.GONE);
                    } else if (peoplePojo.getUser().get(pos).getCopyposestatus() == 1) {
                        binding.ivVery.setVisibility(View.VISIBLE);
                    }
                    if(peoplePojo.getUser().get(pos).getFollowDetails().size()!=0){
                        if(peoplePojo.getUser().get(pos).getFollowDetails().get(0)!=null)
                        if (peoplePojo.getUser().get(pos).getFollowDetails().get(0).getFollowingStatus().equalsIgnoreCase("Request")) {
                            binding.ivFollowReq.setVisibility(View.GONE);
                            binding.ivAddedFollow.setVisibility(View.VISIBLE);
                        } else if (peoplePojo.getUser().get(pos).getFollowDetails().get(0).getFollowingStatus().equalsIgnoreCase("Accepted")) {
                            binding.ivAddedFollow.setVisibility(View.GONE);
                            binding.ivFollowReq.setVisibility(View.GONE);
                        } else {
                            binding.ivAddedFollow.setVisibility(View.GONE);
                            binding.ivFollowReq.setVisibility(View.VISIBLE);
                        }
                    }else{
                        binding.ivAddedFollow.setVisibility(View.GONE);
                        binding.ivFollowReq.setVisibility(View.VISIBLE);
                    }
                    if (peoplePojo.getUser().get(pos).getUserInterest() != null) {
                        for (int k=0; k<peoplePojo.getUser().get(pos).getSubCat().size();k++) {
                            peoplePojo.getUser().get(pos).getSubCat().get(k).setCommon(false);
                        }
//                        String[] separated = peoplePojo.getUser().get(pos).getUserInterest().split(",");
                        for (int i = 0; i < peoplePojo.getUser().get(pos).getLoginUserSubCat().size(); i++) {
                            for (int k = 0; k < peoplePojo.getUser().get(pos).getSubCat().size(); k++) {

                                if (peoplePojo.getUser().get(pos).getLoginUserSubCat().get(i).getSkSubCategoryId().equals(peoplePojo.getUser().get(pos).getSubCat().get(k).getSkSubCategoryId())) {
                                    peoplePojo.getUser().get(pos).getSubCat().get(k).setCommon(true);
                                    break;
                                } else {
                                }

                            }
                        }
                        adaptersub.updateList(peoplePojo.getUser().get(pos).getSubCat(), (ArrayList<PeoplePojo.SubCat>) peoplePojo.getUser().get(pos).getLoginUserSubCat());
                    }

                }

            }
        });
    }
    public void showBottomSheetShare() {
        BottomSheetBlockFragment addPhotoBottomDialogFragment =
                BottomSheetBlockFragment.newInstance();
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                BottomSheetBlockFragment.TAG);
    }
    public void showBottomSheetReport() {
        ReportBottomSheetFragment reportBottomSheetFragment =
                ReportBottomSheetFragment.newInstance();
        reportBottomSheetFragment.show(getActivity().getSupportFragmentManager(),
                ReportBottomSheetFragment.TAG);
    }
    public void getAcessToken() {
        peopleViewModel.callPeopleApi(access_token);
        observeApi();
    }

    public void observeApi() {
        peopleViewModel.getPeople().observe(bottomNavigationActivity, this::peopleviews);

    }
    private void peopleviews(ServerResponse<PeoplePojo> peoplePojoServerResponse) {
        switch (peoplePojoServerResponse.statusType) {

            case LOADING:
                bottomNavigationActivity.showProgressDialog(bottomNavigationActivity);
                break;

            case SUCCESS:
                bottomNavigationActivity.dismissProgressDialog();
                peoplePojo = peoplePojoServerResponse.getData();
                for(int i=0;i<peoplePojo.getUser().size();i++){
                    if (peoplePojo.getUser().get(i).getPeopleblockstatus().equals("1")) {
                        peoplePojo.getUser().remove(i);
                    }
                }

                if (peoplePojo.getUser().get(0).getCopyposestatus() == null) {
                    binding.ivVery.setVisibility(View.GONE);
                }else if (peoplePojo.getUser().get(0).getCopyposestatus() == 0) {
                    binding.ivVery.setVisibility(View.GONE);
                } else if (peoplePojo.getUser().get(0).getCopyposestatus() == 1) {
                    binding.ivVery.setVisibility(View.VISIBLE);
                }

                if(peoplePojo.getUser().get(pos).getFollowDetails().size()!=0){
                    if(peoplePojo.getUser().get(pos).getFollowDetails().get(0)!=null)
                    if (peoplePojo.getUser().get(0).getFollowDetails().get(0).getFollowingStatus().equalsIgnoreCase("Request")) {
                        binding.ivFollowReq.setVisibility(View.GONE);
                        binding.ivAddedFollow.setVisibility(View.VISIBLE);
                    } else if (peoplePojo.getUser().get(0).getFollowDetails().get(0).getFollowingStatus().equalsIgnoreCase("Accepted")) {
                        binding.ivAddedFollow.setVisibility(View.GONE);
                        binding.ivFollowReq.setVisibility(View.GONE);
                    } else {
                        binding.ivAddedFollow.setVisibility(View.GONE);
                        binding.ivFollowReq.setVisibility(View.VISIBLE);
                    }

                }else {
                    binding.ivAddedFollow.setVisibility(View.GONE);
                    binding.ivFollowReq.setVisibility(View.VISIBLE);
                }
                peoples(peoplePojo);
                break;

            case ERROR:
                bottomNavigationActivity.dismissProgressDialog();
                break;
        }
    }

    public void getBlock(JsonObject type) {
        blockViewModel.callBlock(access_token,type);
        block();
    }

    public void block() {
        blockViewModel.getPeople().observe(bottomNavigationActivity, this::block);

    }
    private void block(ServerResponse<String> blockPojoServerResponse) {
        switch (blockPojoServerResponse.statusType) {

            case LOADING:
                bottomNavigationActivity.showProgressDialog(bottomNavigationActivity);
                break;

            case SUCCESS:
                bottomNavigationActivity.dismissProgressDialog();
//                blockPojo = blockPojoServerResponse.getData();
                Toast.makeText(getContext(),blockPojoServerResponse.getStatusMessage().toString(),Toast.LENGTH_LONG).show();
                binding.rlPopup.setVisibility(View.GONE);
                break;

            case ERROR:
                bottomNavigationActivity.dismissProgressDialog();
                binding.rlPopup.setVisibility(View.GONE);
                Toast.makeText(getContext(),blockPojoServerResponse.getStatusMessage(),Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void getFollowRequest() {
        JsonObject jsonObject=new JsonObject();

        jsonObject.addProperty("following_status","Request");
        jsonObject.addProperty("request_follow_id",peoplePojo.getUser().get(pos).getUserid());
        jsonObject.addProperty("request_user_id",userid);
        jsonObject.addProperty("request_type","Follow");
        getBlock(jsonObject);
        followViewModel.callrequest(access_token,jsonObject);
        request();
    }

    public void request() {
        followViewModel.getPeople().observe(bottomNavigationActivity, this::requestFollow);

    }
    private void requestFollow(ServerResponse<String> followPojoServerResponse) {
        switch (followPojoServerResponse.statusType) {

            case LOADING:
                bottomNavigationActivity.showProgressDialog(bottomNavigationActivity);
                break;

            case SUCCESS:
                bottomNavigationActivity.dismissProgressDialog();
                Toast.makeText(getContext(),followPojoServerResponse.getStatusMessage().toString(),Toast.LENGTH_LONG).show();
                binding.ivFollowReq.setVisibility(View.GONE);
                binding.ivAddedFollow.setVisibility(View.VISIBLE);
                break;

            case ERROR:
                bottomNavigationActivity.dismissProgressDialog();
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_block:
              showBottomSheetShare();
                break;

            case R.id.rl_report:
                showBottomSheetReport();
                break;
            case R.id.iv_follow_req:
                getFollowRequest();
                break;
        }
    }

    public void callApi(String type, String text) {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("popup_type",type);
        jsonObject.addProperty("popup_reason",text);
        jsonObject.addProperty("people_id",peoplePojo.getUser().get(pos).getUserid());
        jsonObject.addProperty("userId",userid);
        getBlock(jsonObject);
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {

    }
}