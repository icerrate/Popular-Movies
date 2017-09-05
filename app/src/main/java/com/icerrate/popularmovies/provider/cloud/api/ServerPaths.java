package com.icerrate.popularmovies.provider.cloud.api;

/**
 * @author Ivan Cerrate.
 */

public interface ServerPaths {

    String QUERY_PAGE = "page";

    String MOVIE_ID = "id";

    //URLS
    String POPULAR_MOVIES = "/3/movie/popular/";

    String TOP_RATED_MOVIES = "/3/movie/top_rated/";

    String TRAILERS_MOVIES = "/3/movie/{" + MOVIE_ID + "}/videos";

    String REVIEWS = "/3/movie/{" + MOVIE_ID + "}/reviews";
}
