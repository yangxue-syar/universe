package com.universe.crawler.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * 只包含我们需要的字段，Jackson 会忽略多余字段
 */
public class OMDbResponse {
    @JsonProperty("Title")
    public String title;

    @JsonProperty("Year")
    public String year;

    @JsonProperty("Released")
    public String released; // e.g. "16 Jul 2010"

    @JsonProperty("Runtime")
    public String runtime; // e.g. "148 min"

    @JsonProperty("Director")
    public String director;

    @JsonProperty("Actors")
    public String actors; // comma separated

    @JsonProperty("Plot")
    public String plot;

    @JsonProperty("Poster")
    public String poster;

    @JsonProperty("imdbRating")
    public String imdbRating; // e.g. "8.8" or "N/A"

    @JsonProperty("imdbID")
    public String imdbID;

    @JsonProperty("Response")
    public String response; // "True" or "False"

    @JsonProperty("Error")
    public String error;

    @JsonProperty("Genre")
    public String genre; // e.g. "Action, Adventure, Sci-Fi"

    // full raw ratings array if needed
    @JsonProperty("Ratings")
    public List<Map<String,String>> ratings;
}