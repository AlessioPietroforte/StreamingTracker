package it.example.applicazioneufficiale.ui.charts;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import it.example.applicazioneufficiale.R;
import it.example.applicazioneufficiale.ui.charts.slider.BannerMoviesPagerAdapter;
import it.example.applicazioneufficiale.ui.charts.slider.SliderThings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChartsActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView rvMoviePopular, rvMovieNowPlaying, rvMovieTopRated, rvMovieUpcoming;
    private ProgressBar loadingMoviePopular, loadingMovieNowPlaying, loadingMovieTopRated, loadingMovieUpcoming;
    private ProgressBar loadingMainMoviePopular, loadingMainMovieNowPlaying, loadingMainMovieTopRated, loadingMainMovieUpcoming;
    private MovieAdapter moviePopularAdapter, movieNowPlayingAdapter, movieUpcomingAdapter, movieTopRatedAdapter;
    private final List<MovieResult> moviePopularResults = new ArrayList<>();
    private final List<MovieResult> movieNowPlayingResults = new ArrayList<>();
    private final List<MovieResult> movieUpcomingResults = new ArrayList<>();
    private final List<MovieResult> movieTopRatedResults = new ArrayList<>();
    private int currentPageMoviePopular = 1, currentPageMovieNowPlaying = 1,
            currentPageMovieUpcoming = 1, currentPageMovieTopRated = 1;
    private int totalPagesMoviePopular = 1, totalPagesMovieNowPlaying = 1, totalPagesMovieUpcoming =1,
            totalPagesMovieTopRated = 1;

    private RecyclerView rvTvPopular, rvTvTopRated, rvTvOnAir;
    private ProgressBar loadingTvPopular, loadingTvTopRated, loadingTvOnAir;
    private ProgressBar loadingMainTvPopular, loadingMainTvTopRated, loadingMainTvOnAir;
    private TVAdapter tvPopularAdapter, tvTopRatedAdapter, tvOnAirAdapter;
    private final List<TVResult> tvPopularResults = new ArrayList<>();
    private final List<TVResult> tvTopRatedResults = new ArrayList<>();
    private final List<TVResult> tvOnAirResults = new ArrayList<>();
    private int currentPageTVPopular = 1, currentPageTVTopRated = 1, currentPageTVOnAir = 1;
    private int totalPagesTVPopular = 1, totalPagesTVTopRated = 1, totalPagesTVOnAir = 1;

    BannerMoviesPagerAdapter bannerMoviesPagerAdapter;
    ViewPager bannerMoviesViewPager;
    List<SliderThings> bannerMoviesList;
    TabLayout tabLayout;



    public static final String MYAPI_KEY = "81dfc491e1ac2fcf353f3abe036aaeda";

    public static final String LANGUAGE = "en-US";

    private String searchType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        Retrofit retrofit = ApiClient.getClient();
        apiService = retrofit.create(ApiService.class);

        setSlider();
        setPopularMovies();
        setNowPlayingMovies();
        setUpcomingMovies();
        setTopRatedMovies();

        setPopularTV();
        setTopRatedTV();
        setOnAirTV();

        findViewById(R.id.imageSearch).setOnClickListener(v -> dialogSearch());

    }


    private void setSlider() {
        tabLayout = findViewById(R.id.tab_indicator);


        bannerMoviesList = new ArrayList<>();
        //static banner
        bannerMoviesList.add(new SliderThings(1, "test1", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUTExMVFhUWGRsYFxgYGBgXGBcXFxgXFxcYGBgYHiggGBolGxgXITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGxAQGy0lICUtLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAKgBLAMBIgACEQEDEQH/xAAbAAABBQEBAAAAAAAAAAAAAAADAAIEBQYBB//EAEcQAAEDAgMEBwQIBAQDCQAAAAEAAhEDIQQSMQVBUWEGEyIycYGRQqGx8BRSYnKCssHRI5KiwiQz4fEHU2MVFjRDc4PS4vL/xAAbAQACAwEBAQAAAAAAAAAAAAADBAECBQAGB//EADcRAAEDAgQCCgEEAQMFAAAAAAEAAhEDIQQSMUFRcQUiYYGRobHB0fATFDJC4fEjM7IGJGKCwv/aAAwDAQACEQMRAD8A8UGvJTA+BrrZQw1Kpa0qhEpqnUNMEo8KVhqGZr4F2Nz/AIVHb71ZbCxIp1hmALXDK8cjp70F5OUwtPCNY6s0P0NvGw847lXuJHz9ZCJUvG0ixxpn2dPDco5CkERZCrMc1xadteaEZTmPXSutYrFADTNlIpiVNpbFqObnADWHRz3NYDxylxGbylRKIi8SrA4V9RvWucXHhBhg3cgOQQr7LTpsDgARJTXbEqgS3q6nKm9rz6AyfJVrxBgp1VhabDKi1ZIBfd0Tpu3dqbmL+YVmgm6FWdSacgBB7bj0BHh4KIWJQiArhUyglo2TIXIRAF0NuuldkTWtSy3R2MTmUrqpejtoExCikJZVLNEprmRuXZwuOHcNVFASIRXC+iYWq0oJZCbCbkRA1KF0quSdUwMXcqJCWVdmVvxoOVJwRY80nBdKqWWQg1IhPAXV0qMgQw1PTgF1wA18+S6VYMgSh5U1rxN96TnEmAm0mXPJW2QiesA3j98k5wXI73h/cFx7kXDmMxO4e/M2F2gXCHOj7oUhYEDg717CBU1Rmulp/H8G/sgViJUtVMQZg8kwEjimm/GV0ncuAwERJHgjYd+5HUIOU9pkITxBlO4V2YFvBWlWi6q1hPfFmu+s37X2m/3KFWwhFxdqsK789LMzs93Pl+vmaPQ5syA7FkhwJvx+d6VaXDTSV6LFU6LiM85i0HNx15cO/e4kwRRJMQrBuysgzVXBreHecf0+KB1rpnMZXKjydTmcrOLjoYS1JtCnLnNLjtNh3xcnsU2hQzgZGe0Ghg77swJB9xUpuJq0h1TabGZ3RD9Wlpibai/NC2FTNSoKYfkLnNOfe2LEj8JdZH2li82UUw3LTGUT2nOhxcXmdHEkk/IVRqivrOa3MDHKPjs+m6q8bhSHO6wgAbwLE8rCB5JmzQ55gm0ZQLwJt+vvVhjKzKrQXUhTqC/ZGVjxvlujTb50UHZRcHHs7wOQB4pmnOyysR+Ox/z4/wBKJCai1BcjmUMtKGEYhIFdASaxdLV0rg0xdS8DQL3BoEk2AG9aqps/D4ZoNbtPF4Gk7munX1Gu9A2PRFDDOxAMO7rZjsz3neMR6+l70F2SKkV6svc4y3NeBxAO8oBBe6Jsn3VRQpgxJP26zlfbEOtg2xumie7uvqTG8lOc/DV9KZpO0kGGk+fdvy9NV7pSwjQ2MqwPTzo41zHVKbctQXkWJ5FFNEDRItx7y64HKB7LyvGYR1N5a4XHzKjOatFUeK+HzmM1Mhh3SCCRaTv+Ko3thVmLFNZA9ge3QqHlSfaDuRyEwhElLOp2smCfD54JlQmR+X7Xte9GIXIUgqrqciJXITXtsfBPKWniVAKlwG6jtfxIXOuCeyhLqltD+pThwA8kQwlGtqRwuRodjCH1rokCOe/yTm0yJzFzZ732uCLjqQDQ60uvr/agFzneHtHe5cLiQrPaWPyukmBHfyiLd0Tc6FjdZCfIuuck17lOqCOqExyK8AW3xJ8Zt7l2lTmTuDS79B/UgA3PgPiFIuquBa2SNdPO4+eaPh+75O/KEB4Nrbkeiez/ADfkQJKkan7xVah6jfuwTSOPz7kxwXTdclXSpK4pmCdu9P1UMJ7XkGeChzZEK9GpkeHH6Ff7MqNa/K/uv7DuU6H1Q6lAtcW/VXQwOEjuuGZWGJoSxtQveXEZT3bEEi/kAfNIl0Feup0i+nl1i45HX2I71WARCGRdHcAuBsqZSxZNld9GAGipULZIGUeLgRb3DzUqvjXUv4LcrYu8ADU3MnXSPRRtgw4ZC4iXTGgfDT+oA80DE1G9svgyTqbkk6zvNvehAS8px4DaA9VX18S57szr8Tyncg4OqzrCXyGnUCfAG3DXyXWtfUOVgkWE6DzOgRvoYa7K89q1hfXmmx1dFjfj/Im02NYe3c2OXxve4+Si/wDaDYtTYb/VGnInl4pmKh+Jynuh0WEbyL/uvStjdF8O+nDqe7wQuradU1UqPbmDDABjTWF5tlDmmAA7UhswRO7mLKNC2/SXo5TwwFSmCInODo5psd3EhYrLClxBAIVaGcyHbbrT1GdZQwtKJa8vzX0cyS7+kfBXmzNjYh1XIcGypTIAa8k2FrzlJEcAQudFsGx9EB4v1depTdIhr+7lPjlHqVtuiW1s1MNJtHn4KtEgGCidIU3Ppy3Y+Uf5Pen7NpPbQdh5PeDA4uJjMLjNfSY1WG20TQqNpihXDnGC/rHOmRv7RB8CIWibsqsahe3Ftg1g91EHcN0z56K+6S7RaxjtDGiZDgAscsJMC33VeQUaWWtiMLBIc11RkRua6oPKR7ys3UJm63uPwr6WNcZa0nBvNz2g4Ui93h2SR5HgsJV1Q6rcr4T2AeX0SZ3QlaYHYZee28UgdCQ5x/lbcecIWy6PeqnSmJAtJdeIB1jU8gr7YvWOgxUa1o/zIIPEAhwhw8B56yKTsmXQAJFzoo+M6N0WMl2KDXXjM3sujgWkkeYWbxFAsMEg8C0yCOIO9es7N2J1bX1ajGgGXCRLhOkACfJZDpBXZXztaILLyW5Y5TvBHHkpZJsl3OgySPhZIBMKI1NKlXIshB0Ekal2ZdqPkzoeScCBr89lyd2ZV5Q8pLYkaz38eaY8z2jq7emZkSplQHEKRdCqHKdQmOd8+qCTJRarwfH/AHRMHSBN9Bd33R3kSYEpMMNSoGAhOrEtYG8e2fflH934lEbqfD9kTFVsxLvrIdHU+B+ClogKteoH1QBoLDlp568yUal3fX8hQKm753lHoaH59hyj1N3zvK4alcbtb3+yYVxFyEgJvUlXkJUsdsFwFcARTSSFLmuzBSKbuCv+jeMY1rg5mZwu20jKdQfD+4qyc11Ulga1oLc4ADWiWiSQBA0krJ0H9W5rudxyWlq9oA6HVrhayQxDYdPH1XrOicQ6phzT3ZAji3+9O4SozKDjZoKaaQGv8rf7kaq6pHacXJuEw7qj2sYC5xIa0DUk2AQwSmnhjbAHv+P7TMxOnz4K+wHRd1ZnW1XBlEXdUqac8gPe+HNP2hskYQtc8tc9ulMiW1Hb4IdJA8I46rmN2tVr1aXWmGhwhg7rYPvPwUzC4MNTqi+lzcX7OPNTMbsgUMK+pLgI7DTGcl1ml0WbAOgvYXGiy1DBuD2tHjPhzWn6SbVZUYWNfmIiRBGUscOOt5H4VR4PSod2XTXdOilzzCrQoNLgTf4Ez6J2F2c2pD84bmqlsi4Bytc2Y8SDzG9a/YmwH06lMms4F7g2GmzxvOXQcd687weIOXKT2Q7PbUEgAn3D0XoTqNSrTommx1QMh4ex9RrgYEtJYx0BS6QYSTi14DhrvtdQukuz6zGvfUrF4cIAzEC5boyY0n3cFjTuWm6XbQNSoG6ZR3QSQ06AAnWLmftLPAKHO0TGHpQzmZ++C03RPbDaYbRfIDiRmJENFRjqbi6dwDp9VocMzI6pRdmBMsdByuE2MEaW3rHbM2Y55BPZZIBdBIEnQRqeXwU7b20H5KNWmxzT1bWm5JbllpDjGvZuOKEJLhCbc1ga5rtIJPZH9EzN1qa3RDIwVGktFiHNqPDhF7jPB9EKviDXrNaCdQSTuDbucRpAAJWTodKMYWhuo8FZ7BGIq53OAZTF3ne+L5Sdzd8b/Cx0mCDLtl5OvWL7D0iy50u24xwmn/mVC4l1uyxwyBvJwZmaRzWHcVOx9JzbkGDcRoQdCOI5qAWpUuLjmdqtsUWUWCnTuBvx7eB+FbYNrn0msY5re0RcakwWyYNoDhu7y0OwsK7E4ZpaSKhe4EwcjchETGkysjhK2T1B8xp5ahbPo9hRmqtbnbTq5Xsc0uEEjtDMCqGApcwuE9l+775rcMpNfhslWTlGU3ImLahYmnsum04hha9oflILB2gQbQd0XJ5BT8f0iNA9XVsAdToeBUHaWLpV2T1o7RAgOI1Inum/mrsmRCSrNABJWAeILgNxj05JjKbnd0E+CJWOZ5vq438Tqtts3otSdRz1ZmJDZytaDcTxdGvOVJICKQcqwFRhGoI8fNMKu9rUadIks7swWg5gRN9e6b6qorsykjUbjxBEhFFxKTzXIUcpEiOaTkIuVgEJzoXAQfh8Uep2Wc3/AJG/u78qbg8PmeG+vJvtH0TMfXzuJGmjeTR3VOroVW/6dA1DqbD3Phbv7FHJ0TqQufA/BNnTzTqWp8D8EVIM/cO5SMJo7w/seFGqjl8yVJw+h00/sehFqoP3HuTDhNFn/t6poqW5ppqpzgIQi1WACA5zhuu5ylnPFNhIq0IeY8U8u4+i0Gw6+ZmU6s+DtPQ/os4FL2fierqB27R3gfn3IVannZG6f6Nxf6fEBztDY8jv3G/KeK05C1nQqm2myvjKgnq2kM+9Bk+Ibb8SyMcVeDHxhjQiJiCPazEF+bwkAeBWa3ivaVmlzMo315b/AB3qFicW57nVapmo7j7I3AcoVWapmfqqz20wZ+zyHoqwhSDxUPEANbYJpfNSoeN/X/dEovygkO7W4A33/rHOJQHt3+XpE/ok4SB2AOBG/wAUXUpIkgQOJ8ygUGEkEd8bvrf6q6pVq1CnnDqjWugQCRrxHkfGVJ6OV6TagdVHaAJaYkExYuHEa8/HV9Fr2uqsqy4PnNvzHVrx6+8K8yYS4oloVPlvqrDZuFacz3nsggEb3FxsB5Ak8gonVEADfoPK2iuaVAtoZagIE5iDAc4EDQHTu6xvtNwl6jcpgrUpkOaCPv33UylWLm5nBoBBDG9kFrZhzmguBAaHOiJM+BVfsPa9KvUdRLA0SctgA7STAsLgmBxRMfinvoZaerhB1acjZgU2lxzDWcpJ3niMVg6nU1mu3Aj59LJrD0AWSdVhdI411KsGNEDfv285J3PEa+w7O2G1jSHDwPEFS9qMFDA1iB7Dj5kGFYdHyK1FpJkbncQRIUD/AIgVAzA1I3ljR5vaD/TKK0SVl1SWgryzYOJZldRqzkzEscL9Wd9vqn54iTi9mBgBBY9p0IhwIGpA1jdJhVexqIcXOd3Gy53MTAaObnENHit10a6c0eo6mpQpioPbDQQYtlcDqIsOECLJmrhm1ROhS+C6VqYM5YzM4cJ4HbwI7JusU6hOjTmRMfQcIEu8JMNG4RxV3tPpR1GKOWjQ7Dwf8tsZTBeCIggguGm9La3S6o51R3UYRzWPALBSywJID2lpBHaADtQSW24r/o3t/kPNaZ6cw9SA6k4TvLfp8lUfQ8Q6mHFtV1PmHEEb8p3eIsoNQcBAnQagcF6xs/brcVgxWawMzS1zZmHMAaRPhBHIhef7aptfLm2c0uzR7QBt5xbyHJULZtuEWm+OuB1T6fdVQwvQeoH0dteo+o+acZG6Nz0wASCYEE681gqjdOdxzCusDtgCkxjjZkzaYk2Me0LxB4IEEpqoG5Sh42lSFBpyw4tgiwygWuB7XiqDG0spDd4aPhb3QpG1sawuLad2zMwGz5NsByTa7HVSagaSTd4AmCbyI0b8NEw0ENWTmBfHZChOq+LvvJrH8lx6Lg6GdwaPaKiwEog/I+oANTbxRT2KRO+r2W/db3vXRVDyrPbVdpqEM7rOyyeDd/nqqlEpC0nf76JbpGoDU/G02bb5PeZ7oT+CezXyPwK4xEYBPkfgVcpRjbg9oT8Odfn2XoMI9F14+bNd+6FA4qNyiuE02jhPsgpqeQmK6WK6uwkEoXKITUk6U1coK0uyMRnpwe8zsu+77P7fhVtjG3AHstHz6rK7Gr5aoB0d2T56H1grU40y9yzMQzI/mvd9EYj8+E62reqfUHvET2yg9dm1N/ihON00NRWkfP8A+ULROgl1nWUarw+Z+QENgupZj5/2TTCuHJZ1ETMrUbJ2dSfSkZHuA9nMCBreYuOKLQxMZaAc4FxDWhzcwkmOydw5gBZzAbQNJ0xLfabMB0TE23SrvBbac4VnNYGltGo6e89ziwspjNA/8x7NAOC4BznKHuaxhJExcf3xVJ/3gh730qdItpgEOeyc5L2tu0QACMxjWAfK62J0zDq7PpFGgBLO5TbTY3tBxcWNEF1x2jpHrg6pDcjD3QZJA718sgbwALeJRjUcS90DQuInQEiw4wSPILRFFsaLy78fVc85zP8Ag8I4q16d7SfXxRfLmtb3GyBkLbOiPtN1ULEURVph8tDoEyQA8wMxHBwOo8DyEKm01GRaQTme4wAHEHMT4yI1M71b4FmQAMyuiZc+mDrlPZY6RaNXCb6BHY06LOrVszi47rSdEemrMNhzQqZ6jmklopCYB1BLovmnS11I2/typjaQptweIaMwdMXIAcI7sAXmb6KHsbHFlakMQ5/Ul0EsJpDtAtGYUsoiSDIjSDMrQdPNh0KVKmabQ2o6pHWOc5xa0ghxBcTuNjxhQWAPFrlVFV7qRvYLI0sEaTGUzRqMDn5i5xBzOaxzmNs0QBDjF5JHAEZjZrXNrsa8EOztkEQ6SRqDpqtMzYYAnqxcfVEkc1zaRNFrXVDJF6bHdoyPqzdgB1II8zZMuYdUiyoJgbrNfSjWlzpc894wL6CTw3D0RsXiyagcWkEgB5tDps71HH9JMaGsFRzCQC0dl2oPWMMAiA4W1t4IIxUkk70KeKPHDRb7odWyYXE0iYDXteJ/6jHNcPJ1EeTggYECWTEHM587m92T5NPqonRJ+ZzmSC2ozt3gtyhrgb2PdIItYkqHtckOc0G1piwNuCTq9SpK9LgMtXA5BqCR43/+lCLgWwPZJj7puPfPqjbPZ22yJDjljiHSD5ASZ5KJhxeOXwV5s9oYx9UjtPDms5NIyyPEW8PFBIujMnLA2n5VfixSJApAEDeLZ3fsFJw2JdR7ji0mSY3xY+ShtqAREWsFGr15d5EeqOw5dFn1GZ7kKPi6pcTMEkyTvUmiOrpPq7z/AA2effd/L8UOhhzUe1rRJcQAOJJgI/Sqo0VOpYQW0hkkaOcO+4cZdMcoVH9chvij4c/p6bqx1AhvM2nu15wqB5lMSSTCwiZunncn0/0PwQyn0BfyPwKgq7LvHMKRhybcP/q5BRKGo+dzlyPmyrujj9g7/ZMcY3oTikuKwS7nSuwnEJEQmlSoNlwhNRAExcoIXFrMFiusphx7ws7nv/qWUVrsDEZamQ6VOz5+z+34kDEMzMnhda3QuJ/DiQw6P6p5/wAfO3er1rEzIpT6duajU3uucmU6arNBle0qMDCAQk0IRCkEJjQpBQ3s2QCFPLnMwlQtbJqljA63ZA/iRzJIb6eCiOCkbSrfw6dOIFJpqSfbqVHARz7LAeWU+R6V3LOxrSKcDU+gBJ9FQ4yto36ojyHzPmgGpLmHQGWk+NnegKlV6Ydr8+ChYZgzhhNi4f6+5aJ0XlagJq20kQpzMKWkRIjTUGePJW+A2g2QK7f/AHGCHfiZZrhzEHjmQ3uDHdXUbmaNIOV4H2XQfQhw5IWI2ecrqlF3WMaMzhEVGN3uc28tEiXNJAm+VTSJiVGODA/KAtpTwLKlNpa8PZ7Lm34SDvB4gwfcrLG43r+ppvDf4QeBvksFMNcfJw8xKw/QraJFcsk5ajXA8JYDUBjj2SJ+0eavfpJ650aX94H/AMEwOss5wygxoVM23tdmHaLB9RwljdwEkZn74kERqY3arBYyq6q81Kjszjv+AAFgBwCmdL6v8ZvKm38zz+qHszDsFMV6pa6SRTpTqW2LqsXDJ0aDLoNwBfpVQIEoDMECwuPdM5eZ0kfZB37yI4xVUKAvxmPRXWNxpdJJk+Q5AACwAEAAWAEBUXWXPCVVwCs0m6utgV3039ZTaXlpAcwCS5j2vDoG8xaPtKbtmnFRwnU68VV7ExZp1mPGoIIHNhD2j+ZrR5q76RUQ2vUaNASW/dBOU+kJLGCMp5r0XQLpFVh/8T6g+30KpZSAaXE6nKPiT8FJx+KtA0sAOQ3KLWPZi/zCDiXygTcLQPUY4cUOpUQwE0o1CkSbK5MCUm0F7oCn7Nf1Talfe0ZWf+od/kJPjCzlV8lXW33ZMtHdTF/vnX9lQlWoCRmO6X6WqZXCg3RuvPfzt3LiJk8ENPFQo6yWxuulhT6LTfwPwKLkMWPuG9dZTM6jQ6+Hgq5kw2iQ4WO3D5SwzTPh+xTHgzopVCmRDszZ/wBHHggVWEHUfy+XDkqZhKZdQLabbHfh2dqjFq61pXS9OkFElJZWk2TSdRNkMomVItCmVxYShEriSSlBSAXU1JcuW6wWMFWm1+/R33va/fzT22VD0ZxMPNI6Pu37wk+8fALQVGrFr0/xvLdl9M6MxZxeFbVnrCzuY18bHkUMuhCqFcYX+0B+6QneIKgCFdzy4b+CERdP2+8h5YfYOXypjKPV/WnzUrAtAdmcDlaC4wY0sL7u0QqHEVZn3fdFh7k5hmyZWL0m/IznP355qHVfCbhaZOYjUCR4zP6FNqmVZ0aOVoG/f95N1n5QvP4HDmrUJ2HvYfPchYnHdZDjrEHyTsNWE5frAg7oaRDj4wSPNSMVhwaOcM7QcGyN8ibjiOI81DwtMDxV6LwRZBx1F4f1r9vl7I/R4lmKozr1jWkfeOQj3kLUV2Zaw4lpv5Gyyr3xiZH/ADQ4ebg4fFaLauI/xQ5Zx7gmmLMeLQqrpg2K4+439VB6zJTYDoQT4EkkeRHp5qZ0sqzWB/6bP1UavBa0H6jB/Q1RuVTYKBiaphRSwgwdVY4OkCcpBJnyDQC4nmYEeZ3wh7Wb2g76w+HyEBz+uGp2nhyaDqvAgeP95fFDpuiCNRceIuFq9oO6ynRqie1TaDyLG5QPGA134ljmuWp2FXD8M+kSc1N2dgI9lw7cHkQLcJVcS3NTPZdNdDVPx4toP8gW+MEeYA71X1eCi1lKxIuor7pJpm63cQIkIQCvtgPbSLqzgD1YJaDvqRDPQ38lT06ckQpO13ZWtpD2e077yh5zENC7DsFOm+s7YW5nT57lT4+tmcSdSoaJVN0NPNECF5es8veXFKFxJJShI5qO4n1T6bzJudD8CgjRFod7fofgVUphjnFwudRui4Z54n1+y8rgrP8ArH1T6DP1/I5NNLmP5QqWlMtFT8bYnfQ8uKirgT8srppnkiSksp4Ia4jdQdEvo7uS7MFP4nn+JQCuKSMK7knDBniFGZvFWGGqn+JURJEeyENWQSCLFGp1C0gjUGR5LcYeuKjG1B7Q8O17Tf5lgVpei2Ku6kd/ab4jUel/IpTGU8zMw29F6L/pvG/ixBou0f8A8hp46eCuHtQoUh67h6eZ19Bc7rC5vu4eazW3XtKwDblRdqVurpdXcOdBd9yxA8z/AGrPVHKy2piS9xcQ6+lhp628N2m5VT1q0G5QvGdI1S98lH2fRl0nRva8/ZViQmYSllY3n2j+nuRqbTwS1Z+Zy0cBh/x0Q0i5ue/4FucqzcwNwDnHV1Vobzyi/l2lmwYV30orBrKdJp7NORPEi7iPxExyhUOHY57gALbzGg4+CboCBdY3SVRrngNvb1v7puJd/iPxsHplafgrzG/+IJ3S73lUuzB1mKp279ZpjkXgn3K3Jmp+GfO0/FO01g1flVPSJ/8AF8GM97A79V3GP7R5W9BCZt5n+Ie37jfRjAm4l0vf94/ErgblV4K42ThooVKx4BjfEl7nf0s96rdoU5ZP1b/h3LQ125MFQZ9Yueec5Wt9O36lVBbMjis+u+Ks8I/v1Xp+j8OHYLKf5T8DzAVIxhO8DmVY7FxQo1RUILgAWujg4FpIJtoVC6mDBaSRxsFI3QY8B+6daQ5eeq030yDofQj4KtNpU8riPtT6qCG3U41M9NjjrGU/eFvhB803CYYvdlWWf9OWnZexA/VFtRo/cAfH4NlYbGwEtdVNm025p56NHjP6rObSqySVuek1I4Wgyho54D38pHYDvw3/ABLz3FOU4cScxQumHinRbSaba/35W7FDKaukrifXkUkkkly5E3fPJOaYPl+iYNF11iuV5hGwzr2tZ3uaUB7pOiLhzfyd+UoMqN1Z3+2B2n0CKDzTZPFdKbC5cSUUVCumo7igLijKFP5TESfFSBUdxXc7/rKKUl2UfQpFcjc+JRKgPGUMhKVxWQSZKSNQrljg4atMjxQUly5ri0gtMEXHMaLfisHNDhoW5gu4yrkoE76hyj7ohzveW+9U/RjFZmmkd12/dPeb6wfMqy6SDuN/5YDT4vGb1BlvosltLLVLeH0L6I/HfqMC2q3Vwv2Efu8D5XVHUqHimYalmcJ0F3WTHlWGApQ2frfl9lO1HhjLLz9Cka+IAdoLnu+THdKM5TKdd9Gh9JaQ0SadKYJqaio5sizWmLggzM2KFh6ZzNuBBmToALlx5AAk+CDi9ofSarMjXNo0GBtJhJORo01JuXEmZ3Dgl6QEFx2TvSFWoHNpsME3PG3tueMKrxONDjOW5M353On770ynTfUBM9kEA8JOYi34T6J+0cM0PDW2MCeGY3AEaWLUytRrUQQQWtMXF2mJi4tvPO6eY4WXnauaTm8tFOwzWUe0TBGhntcLb/SPFdZjWFxNxmECfKZju/PiqQmbzKLimwKd5lknlJIj3I7XEaJCo0FStptcahqz2i7Mbb+Mfog1XZyXiAT3gLC+scB8F3CYoSBUmJF98cP2URtYh2YQL6bvDmFJIQwNitrj6pdQoWMCQ20aOINjcGQqhysNluNbCb5ovJAiwY5rLzyINvthV9ULMxAIqmd7r2PRrg/BMjaQeYPwQeRCgY9gkGNde12ez9lCY5TK9LMI37nfaUHB0y97WA3cYnhxMchfyTOHdbksnpKnFSY/d67+frqr/ZmEP0d7j9YPaPsnrGF3hmYR5LXf8PNn0zVdUq/5dPtu8GxHvVXhMKTUqtFqeRlEXmHDI6mAOTabvHtbyp21MSMJgxSFqlTtuO8MEhjfO7vNqUxn+7b6Vs9Dn/tHMJgiRyabk/8AIc44rP8ATjavX4io/i4n1NljKpUzGVpJUB5TFBmRsLE6UxIq1LC2g5CwTCuLoC4mFkpLoXEYNC6VZrZSERzSAC6AFyyqixyRaIE+TvylBACNh238nbvslBBCjdXP7BpqfRq4ndYkkrIGYjRczrudJJdC7OUMrhSSUqi4kkkuXJJJJLlym7NxXV1WPuQ0iQN7faHpK1O0Je+o6ZzDNqIh5DmkcrghJJL1mjMDzW50RVdkqU9te+Pe08gqfC4frKgbFu877oVu+m6dF1JJV3nPC9T0bhmfpy/cn0++ZQtoksw7hEGo4N/CO073ADwcVTYbGFhls87xI4JJJugAaYlec6UeWYx4G0AeAPuU3D1gDLu0d3I8eZXovQqpTrvbnytplj21iSBIqNcGtBdo41GsAjWYGqSSmu0Zc24WaKjgwtGkH0XBsajiKb676LL1qgENDBkpUxVqHOyHG5y3J0KPg/8AhC2tRa9uJex+QFwNPO3OHOY5rTLTq22+4SSS4e5pgHgidIU2tqWA1doANHuA07AO1ea4jZobXdRFUHLUdTzZTByuLc0TpaYQsXg2sE5iT4DhPokktAkh7RxWeGjI4rX9EmAYnEYdvZY7DOgHUua1lQE8T7o8FWVW3ckkk8Tow9nx8r0fQg/06w4O9j8KM4JtKmKdQPBGZ5GQeZzT5geqSSmhrCv0hAYHbhw+PfxXqjsC1tKm4DsNpipmMSQ+nSrBx4nLWxLQPsQvMuk21XVqjnnefTgByAskkhME1DKBTeWYIkakws3VfKEUkloBeecZKQTm6pJKVUKQS3l6Jda35CSSrklG/UuGw8/lOFZqQrt4FJJRkCuMU/sRKNcTodD+UqECF1JSGgaKKlVz2DNxPsv/2Q=="));
        bannerMoviesList.add(new SliderThings(2, "test2", "https://www.spetteguless.it/wp-content/uploads/2021/02/cruella-cover.jpg"));
        bannerMoviesList.add(new SliderThings(3, "test3", "https://malatidicinema.it/wp-content/uploads/2021/05/La-notte-del-giudizio-per-sempre.jpg"));
        bannerMoviesList.add(new SliderThings(4, "test4", "https://www.oaplus.it/wp-content/uploads/2021/01/maxresdefault.jpg"));

        setBannerMoviesPagerAdapter(bannerMoviesList);
    }
    private void setBannerMoviesPagerAdapter(List<SliderThings> bannerMoviesList) {

        bannerMoviesViewPager = findViewById(R.id.bannerViewPager);
        bannerMoviesPagerAdapter = new BannerMoviesPagerAdapter(this, bannerMoviesList);
        bannerMoviesViewPager.setAdapter(bannerMoviesPagerAdapter);
        tabLayout.setupWithViewPager(bannerMoviesViewPager);

        Timer sliderTimer = new Timer();
        sliderTimer.scheduleAtFixedRate(new AutoSlider(), 3000, 5000);
        tabLayout.setupWithViewPager(bannerMoviesViewPager, true);


    }

    class AutoSlider extends TimerTask {

        @Override
        public void run() {
            ChartsActivity.this.runOnUiThread(() -> {

                if (bannerMoviesViewPager.getCurrentItem() < bannerMoviesList.size() - 1) {
                    bannerMoviesViewPager.setCurrentItem(bannerMoviesViewPager.getCurrentItem() + 1);
                } else {
                    bannerMoviesViewPager.setCurrentItem(0);


                }

            });
        }
    }



    private void setPopularMovies() {
        rvMoviePopular = findViewById(R.id.rvMoviePopular);
        moviePopularAdapter = new MovieAdapter(moviePopularResults, this);
        loadingMoviePopular = findViewById(R.id.loadingMoviePopular);
        loadingMainMoviePopular = findViewById(R.id.loadingMainMoviePopular);
        loadingMainMoviePopular.setVisibility(View.VISIBLE);
        getPopularMovies();
        rvMoviePopular.setAdapter(moviePopularAdapter);

        rvMoviePopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMoviePopular.canScrollHorizontally(1)) {
                    loadingMoviePopular.setVisibility(View.VISIBLE);
                    if (currentPageMoviePopular <= totalPagesMoviePopular) {
                        currentPageMoviePopular += 1;
                        getPopularMovies();
                    }
                }
            }
        });
    }

    private void getPopularMovies() {
        Call<MovieResponse> call = apiService.getPopularMovies(MYAPI_KEY, LANGUAGE, currentPageMoviePopular);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    totalPagesMoviePopular = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMoviePopular.setVisibility(View.GONE);
                        loadingMoviePopular.setVisibility(View.GONE);
                        int oldCount = moviePopularResults.size();
                        moviePopularResults.addAll(response.body().getResults());
                        moviePopularAdapter.notifyItemChanged(oldCount, moviePopularResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadingMainMoviePopular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred while loading popular movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNowPlayingMovies() {
        rvMovieNowPlaying = findViewById(R.id.rvMovieNowPlaying);
        movieNowPlayingAdapter = new MovieAdapter(movieNowPlayingResults, this);
        loadingMovieNowPlaying = findViewById(R.id.loadingMovieNowPlaying);
        loadingMainMovieNowPlaying = findViewById(R.id.loadingMainMovieNowPlaying);
        loadingMainMovieNowPlaying.setVisibility(View.VISIBLE);
        getNowPlayingMovies();
        rvMovieNowPlaying.setAdapter(movieNowPlayingAdapter);

        rvMovieNowPlaying.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieNowPlaying.canScrollHorizontally(1)) {
                    loadingMovieNowPlaying.setVisibility(View.VISIBLE);
                    if (currentPageMovieNowPlaying <= totalPagesMovieNowPlaying) {
                        currentPageMovieNowPlaying += 1;
                        getNowPlayingMovies();
                    }
                }
            }
        });
    }

    private void getNowPlayingMovies() {
        Call<MovieResponse> call = apiService.getNowPlayingMovies(MYAPI_KEY, LANGUAGE, currentPageMovieNowPlaying);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    totalPagesMovieNowPlaying = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieNowPlaying.setVisibility(View.GONE);
                        loadingMovieNowPlaying.setVisibility(View.GONE);
                        int oldCount = movieNowPlayingResults.size();
                        movieNowPlayingResults.addAll(response.body().getResults());
                        movieNowPlayingAdapter.notifyItemChanged(oldCount, movieNowPlayingResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadingMainMovieNowPlaying.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading now playing movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpcomingMovies() {
        rvMovieUpcoming = findViewById(R.id.rvUpcomingMovie);
        movieUpcomingAdapter = new MovieAdapter(movieUpcomingResults, this);
        loadingMovieUpcoming = findViewById(R.id.loadingMovieUpcoming);
        loadingMainMovieUpcoming = findViewById(R.id.loadingMainMovieUpcoming);
        loadingMainMovieUpcoming.setVisibility(View.VISIBLE);
        getUpcomingMovies();
        rvMovieUpcoming.setAdapter(movieUpcomingAdapter);

        rvMovieUpcoming.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieUpcoming.canScrollHorizontally(1)) {
                    loadingMovieUpcoming.setVisibility(View.VISIBLE);
                    if (currentPageMovieUpcoming <= totalPagesMovieUpcoming) {
                        currentPageMovieUpcoming += 1;
                        getUpcomingMovies();
                    }
                }
            }
        });
    }

    private void getUpcomingMovies() {
        Call<MovieResponse> call = apiService.getUpcomingMovies(MYAPI_KEY, LANGUAGE, currentPageMovieUpcoming);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    totalPagesMovieUpcoming = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieUpcoming.setVisibility(View.GONE);
                        loadingMovieUpcoming.setVisibility(View.GONE);
                        int oldCount = movieUpcomingResults.size();
                        movieUpcomingResults.addAll(response.body().getResults());
                        movieUpcomingAdapter.notifyItemChanged(oldCount, movieUpcomingResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadingMainMovieUpcoming.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading upcoming movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopRatedMovies() {
        rvMovieTopRated = findViewById(R.id.rvMovieTopRated);
        movieTopRatedAdapter = new MovieAdapter(movieTopRatedResults, this);
        loadingMovieTopRated = findViewById(R.id.loadingMovieTopRated);
        loadingMainMovieTopRated = findViewById(R.id.loadingMainMovieTopRated);
        loadingMainMovieTopRated.setVisibility(View.VISIBLE);
        getTopRatedMovies();
        rvMovieTopRated.setAdapter(movieTopRatedAdapter);

        rvMovieTopRated.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvMovieTopRated.canScrollHorizontally(1)) {
                    loadingMovieTopRated.setVisibility(View.VISIBLE);
                    if (currentPageMovieTopRated <= totalPagesMovieTopRated) {
                        currentPageMovieTopRated += 1;
                        getTopRatedMovies();
                    }
                }
            }
        });
    }

    private void getTopRatedMovies() {
        Call<MovieResponse> call = apiService.getTopRatedMovies(MYAPI_KEY, LANGUAGE, currentPageMovieTopRated);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    totalPagesMovieTopRated = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainMovieTopRated.setVisibility(View.GONE);
                        loadingMovieTopRated.setVisibility(View.GONE);
                        int oldCount = movieTopRatedResults.size();
                        movieTopRatedResults.addAll(response.body().getResults());
                        movieTopRatedAdapter.notifyItemChanged(oldCount, movieTopRatedResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                loadingMainMovieTopRated.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading top rated movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPopularTV() {
        rvTvPopular = findViewById(R.id.rvTvPopular);
        tvPopularAdapter = new TVAdapter(tvPopularResults, this);
        loadingTvPopular = findViewById(R.id.loadingTvPopular);
        loadingMainTvPopular = findViewById(R.id.loadingMainTvPopular);
        loadingMainTvPopular.setVisibility(View.VISIBLE);
        getPopularTV();
        rvTvPopular.setAdapter(tvPopularAdapter);

        rvTvPopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvPopular.canScrollHorizontally(1)) {
                    loadingTvPopular.setVisibility(View.VISIBLE);
                    if (currentPageTVPopular <= totalPagesTVPopular) {
                        currentPageTVPopular += 1;
                        getPopularTV();
                    }
                }
            }
        });
    }

    private void getPopularTV() {
        Call<TVResponse> call = apiService.getTvPopular(MYAPI_KEY, LANGUAGE, currentPageTVPopular);
        call.enqueue(new Callback<TVResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVResponse> call, @NonNull Response<TVResponse> response) {
                if (response.body() != null) {
                    totalPagesTVPopular = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainTvPopular.setVisibility(View.GONE);
                        loadingTvPopular.setVisibility(View.GONE);
                        int oldCount = tvPopularResults.size();
                        tvPopularResults.addAll(response.body().getResults());
                        tvPopularAdapter.notifyItemChanged(oldCount, tvPopularResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVResponse> call, @NonNull Throwable t) {
                loadingMainTvPopular.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading popular tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTopRatedTV() {
        rvTvTopRated = findViewById(R.id.rvTvTopRated);
        tvTopRatedAdapter = new TVAdapter(tvTopRatedResults, this);
        loadingTvTopRated = findViewById(R.id.loadingTvTopRated);
        loadingMainTvTopRated = findViewById(R.id.loadingMainTvTopRated);
        loadingMainTvTopRated.setVisibility(View.VISIBLE);
        getTopRatedTV();
        rvTvTopRated.setAdapter(tvTopRatedAdapter);

        rvTvTopRated.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvTopRated.canScrollHorizontally(1)) {
                    loadingTvTopRated.setVisibility(View.VISIBLE);
                    if (currentPageTVTopRated <= totalPagesTVTopRated) {
                        currentPageTVTopRated += 1;
                        getTopRatedTV();
                    }
                }
            }
        });
    }

    private void getTopRatedTV() {
        Call<TVResponse> call = apiService.getTvTopRated(MYAPI_KEY, LANGUAGE, currentPageTVTopRated);
        call.enqueue(new Callback<TVResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVResponse> call, @NonNull Response<TVResponse> response) {
                if (response.body() != null) {
                    totalPagesTVTopRated = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingTvTopRated.setVisibility(View.GONE);
                        loadingMainTvTopRated.setVisibility(View.GONE);
                        int oldCount = tvTopRatedResults.size();
                        tvTopRatedResults.addAll(response.body().getResults());
                        tvTopRatedAdapter.notifyItemChanged(oldCount, tvTopRatedResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVResponse> call, @NonNull Throwable t) {
                loadingMainTvTopRated.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading top rated tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnAirTV() {
        rvTvOnAir = findViewById(R.id.rvTvOnAir);
        tvOnAirAdapter = new TVAdapter(tvOnAirResults, this);
        loadingTvOnAir = findViewById(R.id.loadingTvOnAir);
        loadingMainTvOnAir = findViewById(R.id.loadingMainTvOnAir);
        loadingMainTvOnAir.setVisibility(View.VISIBLE);
        getOnAirTV();
        rvTvOnAir.setAdapter(tvOnAirAdapter);

        rvTvOnAir.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rvTvOnAir.canScrollHorizontally(1)) {
                    loadingTvOnAir.setVisibility(View.VISIBLE);
                    if (currentPageTVOnAir <= totalPagesTVOnAir) {
                        currentPageTVOnAir += 1;
                        getOnAirTV();
                    }
                }
            }
        });
    }

    private void getOnAirTV() {
        Call<TVResponse> call = apiService.getTvOnAir(MYAPI_KEY, LANGUAGE, currentPageTVOnAir);
        call.enqueue(new Callback<TVResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVResponse> call, @NonNull Response<TVResponse> response) {
                if (response.body() != null) {
                    totalPagesTVOnAir = response.body().getTotalPages();
                    if (response.body().getResults() != null) {
                        loadingMainTvOnAir.setVisibility(View.GONE);
                        loadingTvOnAir.setVisibility(View.GONE);
                        int oldCount = tvOnAirResults.size();
                        tvOnAirResults.addAll(response.body().getResults());
                        tvOnAirAdapter.notifyItemChanged(oldCount, tvOnAirResults.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVResponse> call, @NonNull Throwable t) {
                loadingMainTvOnAir.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error occurred loading on air tv shows",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_search, null);

        EditText inputSearch = v.findViewById(R.id.inputSearch);
        ImageView imageDoSearch = v.findViewById(R.id.imageDoSearch);
        RadioGroup radioGroup = v.findViewById(R.id.radioGroup);
        RadioButton radioButtonMovie = v.findViewById(R.id.radioButtonMovie);
        RadioButton radioButtonTv = v.findViewById(R.id.radioButtonTv);

        builder.setView(v);
        AlertDialog dialogSearch = builder.create();
        if (dialogSearch.getWindow() != null)
            dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonMovie) {
                searchType = radioButtonMovie.getText().toString();
            } else {
                searchType = radioButtonTv.getText().toString();
            }
        });
        imageDoSearch.setOnClickListener(view -> doSearch(inputSearch.getText().toString()));

        inputSearch.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                doSearch(inputSearch.getText().toString());
            }
            return false;
        });

        dialogSearch.show();
    }

    private void doSearch(String query) {
        if (query.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    R.string.noLettersError,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchType == null) {
            Toast.makeText(getApplicationContext(),
                    R.string.choose,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(ChartsActivity.this, SearchActivity.class);
        i.putExtra("tipe", searchType);
        i.putExtra("searchFor", query);
        startActivity(i);
    }


}