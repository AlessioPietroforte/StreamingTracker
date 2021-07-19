package it.example.applicazioneufficiale.charts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import it.example.applicazioneufficiale.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsActivity extends AppCompatActivity {

    private RoundedImageView imageBackdrop, imagePoster;
    private ImageView imagePosterBack;
    private TextView textTitle, textRuntime, textReleaseDate, textOverview, textlanguage, textStatus, textBudgetOrSeasons, textRevenueOrEpisodes,
            textPopularity, textTagline, textVoteCount, textVoteAverage, textHomepage;
    private ProgressBar loadingDetails;
    private NestedScrollView scrollView;
    private int id;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePosterBack = findViewById(R.id.imagePosterBack);
        imageBackdrop = findViewById(R.id.imageBackdrop);
        imagePoster = findViewById(R.id.imagePoster);
        textTitle = findViewById(R.id.textTitle);
        textRuntime = findViewById(R.id.textRuntime);
        textReleaseDate = findViewById(R.id.textReleaseDate);
        textOverview = findViewById(R.id.textOverview);
        textlanguage = findViewById(R.id.textlanguage);
        textStatus = findViewById(R.id.textStatus);
        textBudgetOrSeasons = findViewById(R.id.textBudgetOrSeasons);
        textRevenueOrEpisodes = findViewById(R.id.textRevenueOrEpisodes);
        textPopularity = findViewById(R.id.textPopularity);
        textTagline = findViewById(R.id.textTagline);
        textVoteCount = findViewById(R.id.textVoteCount);
        textVoteAverage = findViewById(R.id.textVoteAverage);
        textHomepage = findViewById(R.id.textHomepage);
        loadingDetails = findViewById(R.id.loadingDetails);
        scrollView = findViewById(R.id.scrollView);

        apiService = ApiClient.getClient().create(ApiService.class);

        id = getIntent().getIntExtra("id", 0);
        String tipe = getIntent().getStringExtra("tipe");
        switch (tipe) {
            case "movie":
                setMovieDetails();
                break;
            case "tv":
                setTVDetails();
                break;
        }

        findViewById(R.id.imageBackDetails).setOnClickListener(v -> onBackPressed());

        ToggleButton WatchedButton = (ToggleButton) findViewById(R.id.watchedButton);
        WatchedButton.setChecked(false);
        WatchedButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.watched_not));
        WatchedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    WatchedButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.watched));
                    Toast toast = Toast.makeText(getApplicationContext(), "Added to Watched", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    WatchedButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.watched_not));
                    Toast toast = Toast.makeText(getApplicationContext(), "Removed from Watched", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        ToggleButton toWatchButton = (ToggleButton) findViewById(R.id.toWatchButton);
        toWatchButton.setChecked(false);
        //toWatchButton.setButtonDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.to_not_watch));
        //toWatchButton.setId(R.id.toWatchButton);
        toWatchButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.to_not_watch));
        toWatchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toWatchButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.to_watch));
                    Toast toast = Toast.makeText(getApplicationContext(), "Added to toWatch", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    toWatchButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.to_not_watch));
                    Toast toast = Toast.makeText(getApplicationContext(), "Removed from toWatch", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        /*toWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isToWatch = readState();

                    if (isToWatch) {
                        toWatchButton.setBackgroundResource(R.drawable.to_not_watch);
                        isToWatch = false;
                        saveState(isToWatch);

                    } else {
                        toWatchButton.setBackgroundResource(R.drawable.to_watch);
                        isToWatch = true;
                        saveState(isToWatch);
                    }
            }
        });
    }
    private void saveState(boolean isToWatch){
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isToWatch);
        aSharedPreferencesEdit.commit();
    }
    private boolean readState() {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);*/
    }

    private void setMovieDetails() {
        loadingDetails.setVisibility(View.VISIBLE);
        Call<MovieDetails> call = apiService.getMovieDetails(String.valueOf(id), ChartsActivity.MYAPI_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                if (response.body() != null) {
                    loadingDetails.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    imagePosterBack.setVisibility(View.VISIBLE);
                    ImageAdapter.setPosterURL(imagePosterBack, response.body().getPosterPath());
                    ImageAdapter.setBackdropURL(imageBackdrop, response.body().getBackdropPath());
                    ImageAdapter.setPosterURL(imagePoster, response.body().getPosterPath());
                    textTitle.setText(response.body().getTitle());
                    setHTMLText(textRuntime, "Runtime", response.body().getRuntime() + " minutes");
                    setHTMLText(textReleaseDate, "Released on", response.body().getReleaseDate());
                    textOverview.setText(response.body().getOverview());
                    setHTMLText(textlanguage, "Language", response.body().getLanguage());
                    setHTMLText(textStatus, "Status", response.body().getStatus());

                    Double budget = Double.parseDouble(response.body().getBudget());
                    String budgetFormatted = NumberFormat.getCurrencyInstance(Locale.US).format(budget);
                    setHTMLText(textBudgetOrSeasons, "Budget", budgetFormatted);

                    Double revenue = Double.parseDouble(response.body().getRevenue());
                    String revenueFormatted = NumberFormat.getCurrencyInstance(Locale.US).format(revenue);
                    setHTMLText(textRevenueOrEpisodes, "Revenue", revenueFormatted);

                    setHTMLText(textPopularity, "Popularity", response.body().getPopularity());
                    setHTMLText(textTagline, "Tagline", response.body().getTagline());
                    setHTMLText(textVoteCount, "Vote Count", response.body().getVoteAverage());
                    setHTMLText(textVoteAverage, "Vote Average", response.body().getVoteCount());
                    setHTMLText(textHomepage, "Homepage", response.body().getHomepage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {
                loadingDetails.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred while loading details from " + id,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTVDetails() {
        loadingDetails.setVisibility(View.VISIBLE);
        Call<TVDetails> call = apiService.getTvDetails(String.valueOf(id), ChartsActivity.MYAPI_KEY);
        call.enqueue(new Callback<TVDetails>() {
            @Override
            public void onResponse(@NonNull Call<TVDetails> call,@NonNull Response<TVDetails> response) {
                if (response.body() != null) {
                    loadingDetails.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    imagePosterBack.setVisibility(View.VISIBLE);
                    ImageAdapter.setPosterURL(imagePosterBack, response.body().getPosterPath());
                    ImageAdapter.setBackdropURL(imageBackdrop, response.body().getBackdropPath());
                    ImageAdapter.setPosterURL(imagePoster, response.body().getPosterPath());
                    textTitle.setText(response.body().getName());

                    String runtime = Arrays.toString(response.body().getEpisodeRuntime());
                    runtime = runtime.replace("[", "");
                    runtime = runtime.replace("]", "");
                    runtime = runtime.replace(",", " &");
                    setHTMLText(textRuntime, "Runtime", runtime + " min");

                    textReleaseDate.setText(HtmlCompat.fromHtml("<font color='#FFCC00'>First air date</font> : " +
                            response.body().getFirstAirDate() + "<br/><font color='#FFCC00'>Last air date</font> : " +
                            response.body().getLastAirdate() , HtmlCompat.FROM_HTML_MODE_LEGACY));

                    textOverview.setText(response.body().getOverview());
                    //setHTMLText(textTitle,"*",response.body().getOverview());
                    setHTMLText(textlanguage, "Language", response.body().getOriginalLanguage());
                    setHTMLText(textStatus, "Status", response.body().getStatus());
                    setHTMLText(textBudgetOrSeasons, "Seasons", response.body().getNumberOfSeasons());
                    setHTMLText(textRevenueOrEpisodes, "Episodes", response.body().getNumberOfEpisodes());
                    setHTMLText(textPopularity, "Popularity", response.body().getPopularity());
                    setHTMLText(textTagline, "Tagline", response.body().getTagline());
                    setHTMLText(textVoteCount, "Vote Count", response.body().getVoteAverage());
                    setHTMLText(textVoteAverage, "Vote Average", response.body().getVoteCount());
                    setHTMLText(textHomepage, "Homepage", response.body().getHomepage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVDetails> call,@NonNull Throwable t) {
                loadingDetails.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred while loading details from " + id,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setHTMLText(TextView tv, String stringColored, String stringValue) {
        tv.setText(HtmlCompat.fromHtml("<font color='#FFCC00'>" + stringColored + "</font> : " + stringValue,
                HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}