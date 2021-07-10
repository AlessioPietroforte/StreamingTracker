package it.example.applicazioneufficiale.ui.film;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class FragmentFilmAdapter extends FragmentStateAdapter {
    public FragmentFilmAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new watchedFilmFragment();

            case 2:
                return new toWatchFilmFragment();

        }

        return new watchingFilmFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
