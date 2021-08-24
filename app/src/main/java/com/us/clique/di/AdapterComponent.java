package com.us.clique.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mounesh on 3-jun-21.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AdapterComponent {
}
