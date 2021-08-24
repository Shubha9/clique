package com.us.clique.people.follow;

import dagger.Module;
import dagger.Provides;

@Module
public class FollowModule {
    @Provides
    FollowRepo provideFollowRepository(){
        return new FollowRepo();
    }

    @Provides
    FollowFactory providesFollowFactory(FollowRepo followRepo){
        return new FollowFactory(followRepo);
    }
}
