package it.example.applicazioneufficiale.charts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //Request to display Most Popular Movies
    @GET("movie/popular")
    Call<MovieRespon> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                       @Query("page") int page);

    //Request to display Now Playing Movies
    @GET("movie/now_playing")
    Call<MovieRespon> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                          @Query("page") int page);

    //Request to show Upcoming Movies
    @GET("movie/upcoming")
    Call<MovieRespon> getUpcomingMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                        @Query("page") int page);

    //Request to display Top Rated Movies
    @GET("movie/top_rated")
    Call<MovieRespon> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                        @Query("page") int page);

    //Request for Movie search
    @GET("search/movie")
    Call<MovieRespon> searchMovie(@Query("api_key") String apiKey, @Query("language") String language,
                                  @Query("query") String query, @Query("page") int page);

    //Request to show details of the movie
    @GET("movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(@Path("movie_id") String id, @Query("api_key") String apiKey);

    // Request to show Most Popular TV Shows
    @GET("tv/popular")
    Call<TVRespon> getTvPopular(@Query("api_key") String apiKey, @Query("language") String language,
                                @Query("page") int page);

    //Request to show Top Rated TV Shows
    @GET("tv/top_rated")
    Call<TVRespon> getTvTopRated(@Query("api_key") String apiKey, @Query("language") String language,
                                 @Query("page") int page);

    //Request to show On Air TV Shows
    @GET("tv/on_the_air")
    Call<TVRespon> getTvOnAir(@Query("api_key") String apiKey, @Query("language") String language,
                              @Query("page") int page);

    //Request to display TV Show details
    @GET("tv/{tv_id}")
    Call<TVDetails> getTvDetails(@Path("tv_id") String id, @Query("api_key") String apiKey);

    //Request to search TV Shows
    @GET("search/tv")
    Call<TVRespon> searchTv(@Query("api_key") String apiKey, @Query("language") String language,
                            @Query("query") String query, @Query("page") int page);

}
