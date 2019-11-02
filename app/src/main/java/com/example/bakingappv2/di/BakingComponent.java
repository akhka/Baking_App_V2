package com.example.bakingappv2.di;

import com.example.bakingappv2.data.network.RecipeIntentService;
import com.example.bakingappv2.ui.fragments.RecipeFragment;
import com.example.bakingappv2.widget.ListWidgetService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RoomModule.class, NetworkModule.class, ExecutorsModule.class, ContextModule.class})
public interface BakingComponent {

    void inject(ListWidgetService.ListRemoteViewsFactory fragment);

    void inject(RecipeIntentService service);

    void inject(RecipeIntentService service)

}
