package com.example.beetel.moviesApp.utilities;

/**
 * Created by beetel on 7/03/2016.
 */
public class MovieAPIUtility {
    public static final String API_BASE_URL="https://api.themoviedb.org/3/movie/550?api_key=900005580a3d413a1aa6cb617d127f8e";
    public static final String URL_IMAGE_BASE="http://image.tmdb.org/t/p/w185/";
    public static final String URL_IMAGE_BACKDROP_BASE="http://image.tmdb.org/t/p/w342";
    public static final String SORT_POPULAR_DESC="popularity.desc";
    public static final String SORT_RATING_DESC="vote_average.desc";

    public static final String EXTRA_RESULT_MODEL="extra_result_model";
    public static final String EXTRA_MOVIE="extra_movie";
    public static final String EXTRA_VIDEO="extra_video";
    public static final String EXTRA_REVIEW="extra_review";
    public static final String EXTRA_TRAILER_FRAGMENT="extra_video_fragment";
    public static final String EXTRA_DETAIL_FRAGMENT="extra_detail_fragment";
    public static final String EXTRA_REVIEW_FRAGMENT="extra_review_fragment";
    public static final String UTUBE_THUMBNAIL_URL_BASE="http://img.youtube.com/vi/";
    public static final String UTUBE_PLAYER_URL_BASE="https://www.youtube.com/watch?v=";
    public static final String[] TABS_MOVIE_DETAIL={"Plot","Trailer","Reviews"};
    public static final String PREF_CHOICE="choice";
}
