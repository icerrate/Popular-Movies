package com.icerrate.popularmovies.provider.cloud.api

/**
 * @author Ivan Cerrate.
 */
object ServerPaths {
    const val QUERY_PAGE = "page"
    const val QUERY_STRING = "query"
    const val MOVIE_ID = "id"

    // URLS
    const val POPULAR_MOVIES = "/3/movie/popular"
    const val TOP_RATED_MOVIES = "/3/movie/top_rated"
    const val SEARCH_MOVIES = "/3/search/movie"
    const val TRAILERS_MOVIES = "/3/movie/{$MOVIE_ID}/videos"
    const val REVIEWS = "/3/movie/{$MOVIE_ID}/reviews"
}