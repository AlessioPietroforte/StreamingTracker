package it.example.applicazioneufficiale.ui.series;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class FragmentSeriesAdapter extends FragmentStateAdapter {
    public FragmentSeriesAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new watchedSeriesFragment();

            case 2:
                return new toWatchSeriesFragment();

        }

        return new watchingSeriesFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

