package com.us.clique.people.block;

import dagger.Module;
import dagger.Provides;

@Module
public class BlockModule {
    @Provides
    BlockRepo provideBlockRepository(){
        return new BlockRepo();
    }

    @Provides
    BlockFactory providesBlockFactory(BlockRepo blockRepo){
        return new BlockFactory(blockRepo);
    }
}
