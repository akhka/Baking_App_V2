package com.example.bakingappv2.di.modules;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExecutorsModule {

    @Singleton
    @Provides
    AppExecutors provideExecutors() {
        return new AppExecutors(Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3),
                new AppExecutors.MainThreadExecutor());
    }

}
