package com.us.clique.di;


/**
 * Created by junaid on 20-Oct-20.
 * <p>
 * Binds all sub-components within the app.
 */

import com.us.clique.bottomNavigation.TestActivity;
import com.us.clique.bottomNavigation.fragments.ExperiencesFragment;
import com.us.clique.bottomNavigation.fragments.HomeFragment;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Binds all sub-components within the app.
 */

@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract HomeFragment homeFragment ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract TestActivity testActivity ();

    @ContributesAndroidInjector(modules = BottomNavigationModule.class)
    abstract ExperiencesFragment experiencesFragment ();

}
