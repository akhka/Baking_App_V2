package com.example.bakingappv2;

import android.app.Application;
import android.content.Context;

import com.example.bakingappv2.di.BakingComponent;
import com.example.bakingappv2.di.modules.ContextModule;
import com.example.bakingappv2.di.modules.RoomModule;

public class BakingApp extends Application {

    private BakingComponent bakingComponent;

    public static BakingComponent getComponent(Context context) {
        return ((BakingApp) context.getApplicationContext()).bakingComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bakingComponent = DaggerBakingComponent.builder()
                .contextModule(new ContextModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

}
