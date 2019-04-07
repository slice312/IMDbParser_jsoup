package com.ttm.html;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Film
{
    private int rank;
    private String title;
    private int year;
    private double ratingIMDb;
    private List<String> genre;
    //-------------------------------------------------------------------------

    public Film()
    {
    }

    public Film(int rank, String title, int year, double ratingIMDb)
    {
        this.rank = rank;
        this.title = title;
        this.year = year;
        this.ratingIMDb = ratingIMDb;
    }


    public static Map<Integer, List<Film>> groupByYear(List<Film> films)
    {
        return films.stream().collect(Collectors.groupingBy(Film::getYear));
    }


    public static Map<String, List<Film>> groupByGenre(List<Film> films)
    {
        Map<String, List<Film>> map = new HashMap<>();
        for (Film f : films)
        {
            for (String genre : f.getGenre())
            {
                if (!map.containsKey(genre))
                    map.put(genre, new LinkedList<>());
                map.get(genre).add(f);
            }
        }
        return map;
    }


    public int getRank()
    {
        return rank;
    }

    public String getTitle()
    {
        return title;
    }

    public int getYear()
    {
        return year;
    }

    public double getRatingIMDb()
    {
        return ratingIMDb;
    }

    public List<String> getGenre()
    {
        return genre;
    }


    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public void setRatingIMDb(double ratingIMDb)
    {
        this.ratingIMDb = ratingIMDb;
    }

    public void setGenre(List<String> genre)
    {
        this.genre = genre;
    }


    @Override
    public String toString()
    {
        return "Film{" +
                "rank=" + rank +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", ratingIMDb=" + ratingIMDb +
                ", genre=" + genre +
                '}';
    }


}


