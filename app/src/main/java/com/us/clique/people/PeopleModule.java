package com.us.clique.people;

import dagger.Module;
import dagger.Provides;

@Module
public class PeopleModule {
    @Provides
    PeopleRepo providePeopleRepository(){
        return new PeopleRepo();
    }

    @Provides
    PeopleFactory providesPeopleFactory(PeopleRepo peopleRepo){
        return new PeopleFactory(peopleRepo);
    }
}
