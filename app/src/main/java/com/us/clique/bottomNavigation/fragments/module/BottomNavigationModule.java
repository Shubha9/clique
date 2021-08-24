package com.us.clique.bottomNavigation.fragments.module;


import dagger.Module;
import dagger.Provides;

@Module
public class BottomNavigationModule {

    @Provides
    BottomNavigationRepo provideBottomNavigationRepository() {
        return new BottomNavigationRepo()
                ;
    }

    @Provides
    BottomNavigationFactory provideBottomNavigationFactory(BottomNavigationRepo bottomNavigationRepo) {
        return new BottomNavigationFactory(bottomNavigationRepo);
    }
}