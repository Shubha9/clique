package com.us.clique.di;


/**
 * Created by mounesh on 3-june-21.
 * <p>
 * Binds all sub-components within the app.
 */

import com.us.clique.activites.AboutActivity;
import com.us.clique.activites.BrowserActivity;
import com.us.clique.activites.CalenderActivity;
import com.us.clique.activites.CalenderExperienceActivity;
import com.us.clique.activites.ChooseInterestsActivity;
import com.us.clique.activites.EditeExperienceActivity;
import com.us.clique.activites.EditeProfileActivity;
import com.us.clique.activites.EventsAroundYouDetailsActivity;
import com.us.clique.activites.EventsArroundYouActivity;
import com.us.clique.activites.EventsDetailsActivity;
import com.us.clique.activites.Filters1Activity;
import com.us.clique.activites.FiltersActivity;
import com.us.clique.activites.HelpActivity;
import com.us.clique.activites.LoginActivity;
import com.us.clique.activites.NotificationActivity;
import com.us.clique.activites.SettingsActivity;
import com.us.clique.activites.YourExperienceActivity;
import com.us.clique.activites.YourExperienceDetailsActivity;
import com.us.clique.bottomNavigation.fragments.BlockFragment;
import com.us.clique.bottomNavigation.fragments.BottomSheetBlockFragment;
import com.us.clique.bottomNavigation.fragments.ChatsFragment;
import com.us.clique.bottomNavigation.fragments.CreateExperienceFragment;
import com.us.clique.bottomNavigation.fragments.ExperiencesFragment;
import com.us.clique.bottomNavigation.fragments.HomeFragment;
import com.us.clique.bottomNavigation.fragments.MessagesFragment;
import com.us.clique.bottomNavigation.fragments.PeopelFragment;
import com.us.clique.bottomNavigation.fragments.ProfileFragment;
import com.us.clique.bottomNavigation.fragments.ReportBottomSheetFragment;
import com.us.clique.bottomNavigation.fragments.ReportFragment;
import com.us.clique.bottomNavigation.fragments.ReportPopUpFragment;
import com.us.clique.bottomNavigation.fragments.RequestsFragment;
import com.us.clique.bottomNavigation.fragments.SearchFragment;
import com.us.clique.bottomNavigation.fragments.ShareFragment;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationModule;
import com.us.clique.copyPose.CopyPoseActivity;
import com.us.clique.forgotPassword.ForgotPwdActivity;
import com.us.clique.forgotPassword.Forgot_pwd_2Activity;
import com.us.clique.forgotPassword.NewPwdActivity;
import com.us.clique.joinNow.JoinNowActivity;
import com.us.clique.joinNowProfile.JoinNowProfileActivity;
import com.us.clique.landingScreen.FirstscreenActivity;
import com.us.clique.landingScreen.FrameActivity;
import com.us.clique.location.LocationActivity;
import com.us.clique.people.PeopleModule;
import com.us.clique.people.block.BlockModule;
import com.us.clique.people.follow.FollowModule;
import com.us.clique.signIn.SignInActivity;
import com.us.clique.signUp.SignUpActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract HomeFragment homeFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract BrowserActivity browserActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ExperiencesFragment experiencesFragment ();

    @ContributesAndroidInjector(modules = {BottomNavigationModule.class, PeopleModule.class, BlockModule.class, FollowModule.class})
    abstract PeopelFragment peopelFragment();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract EventsArroundYouActivity eventsArroundYouActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract EventsDetailsActivity eventsDetailsActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract FiltersActivity filtersActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract Filters1Activity filters1Activity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ChooseInterestsActivity chooseInterestsActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract CreateExperienceFragment addFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ProfileFragment profileFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract SettingsActivity settingsActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract CalenderActivity calenderActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract EditeExperienceActivity editeExperienceActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ReportPopUpFragment reportPopUpFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ReportFragment reportFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract YourExperienceActivity yourExperienceActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract BlockFragment blockFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ShareFragment shareFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract SearchFragment searchFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract MessagesFragment messagesFragment ();


    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ChatsFragment chatsFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract RequestsFragment requestsFragment ();

//    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
//    abstract ChatingActivity chatingActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract EditeProfileActivity editeProfileActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract SignUpActivity signUpActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract SignInActivity signInActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract Forgot_pwd_2Activity forgot_pwd_2Activity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ForgotPwdActivity forgotPwdActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract NewPwdActivity newPwdActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract JoinNowActivity joinNowActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract LoginActivity loginActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract FirstscreenActivity firstscreenActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract FrameActivity frameActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract JoinNowProfileActivity  joinNowProfileActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract CopyPoseActivity copyPoseActivity ();



    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract BottomSheetBlockFragment  bottomSheetBlockFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ReportBottomSheetFragment reportBottomSheetFragment ();


    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract NotificationActivity notificationActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract HelpActivity helpActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract AboutActivity aboutActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract LocationActivity locationActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract EventsAroundYouDetailsActivity eventsAroundYouDetailsActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract YourExperienceDetailsActivity yourExperienceDetailsActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract CalenderExperienceActivity calenderExperienceActivity ();


}
