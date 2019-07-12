package com.icerrate.popularmovies.provider.cloud.api;

/**
 * @author Ivan Cerrate.
 */

public interface ServerPaths {

    String QUERY_PAGE = "page";

    String QUERY_STRING = "query";

    String MOVIE_ID = "id";

    //URLS
    String POPULAR_MOVIES = "/3/movie/popular/";

    String TOP_RATED_MOVIES = "/3/movie/top_rated/";

    String SEARCH_MOVIES = "/3/search/movie";

    String TRAILERS_MOVIES = "/3/movie/{" + MOVIE_ID + "}/videos";

    String REVIEWS = "/3/movie/{" + MOVIE_ID + "}/reviews";
}
