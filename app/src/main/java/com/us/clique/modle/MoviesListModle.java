package com.us.clique.modle;

public class MoviesListModle {
    public String getMoviesName() {
        return moviesName;
    }

    public void setMoviesName(String moviesName) {
        this.moviesName = moviesName;
    }

    public MoviesListModle(String moviesName) {
        this.moviesName = moviesName;
    }

    String moviesName;
}
