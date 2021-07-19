package it.example.applicazioneufficiale.ui.charts.slider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import it.example.applicazioneufficiale.R;
import it.example.applicazioneufficiale.ui.charts.MovieDetails;


import java.util.List;

public class BannerMoviesPagerAdapter extends PagerAdapter {

    Context context;
    List<SliderThings> bannerMoviesList;

    public BannerMoviesPagerAdapter(Context context, List<SliderThings> bannerMoviesList) {
        this.context = context;
        this.bannerMoviesList = bannerMoviesList;
    }

    @Override
    public int getCount() {
        return bannerMoviesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_slider, null);

        ImageView bannerImage = view.findViewById(R.id.banner_image);

        //da qui in poi si usa glide per il fetching dell' immagine dall' url e il setting dell' image view
        Glide.with(context).load(bannerMoviesList.get(position).getImageUrl()).into(bannerImage);
        container.addView(view);

        bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MovieDetails.class);
                i.putExtra("movieId", bannerMoviesList.get(position).getId());
                i.putExtra("movieName", bannerMoviesList.get(position).getTitle());
                i.putExtra("movieImageUrl", bannerMoviesList.get(position).getImageUrl());
                context.startActivity(i);


            }
        });

        return view;

    }
}
