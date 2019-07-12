package com.icerrate.popularmovies.mock;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;

import java.util.ArrayList;

public class MockUtils {

    public static ArrayList<Movie> getMovies(boolean hasItems) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (hasItems) {
            movies.add(getSingleMovie(1));
            movies.add(getSingleMovie(2));
            movies.add(getSingleMovie(3));
        }
        return movies;
    }

    public static ArrayList<Trailer> getTrailers(boolean hasItems) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        if (hasItems) {
            trailers.add(getSingleTrailer("1"));
            trailers.add(getSingleTrailer("2"));
            trailers.add(getSingleTrailer("3"));
        }
        return trailers;
    }

    public static ArrayList<Review> getReviews(boolean hasItems) {
        ArrayList<Review> reviews = new ArrayList<>();
        if (hasItems) {
            reviews.add(getSingleReview("1"));
            reviews.add(getSingleReview("2"));
            reviews.add(getSingleReview("3"));
        }
        return reviews;
    }
    public static Movie getSingleMovie(int id) {
        Movie movie = new Movie(id, "Title", "Path", "Backdrop", "overview", 5, "Date");
        movie.setFavorite(true);
        return movie;
    }

    public static Trailer getSingleTrailer(String id) {
        return new Trailer(id, "Iso639", "Iso3166", "Key", "Name", "Site", 100, "Type");
    }

    public static Review getSingleReview(String id) {
        return new Review(id, "Author", "Content", "Url");
    }
}
