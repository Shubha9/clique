package com.us.clique.people.block;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BlockFactory implements ViewModelProvider.Factory{
    BlockRepo blockRepo;

    public BlockFactory(BlockRepo blockRepo) {
        this.blockRepo = blockRepo ;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BlockViewModel.class)) {
            return (T) new BlockViewModel(blockRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
