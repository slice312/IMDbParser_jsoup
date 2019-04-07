package com.ttm.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class IMDbParser
{
    static final String url = "https://www.imdb.com";
    static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
            " AppleWebKit/537.36 (KHTML, like Gecko)" +
            " Chrome/73.0.3683.86 Safari/537.36";


    public static void main(String[] args) throws IOException
    {
        List<Film> films = readFilms();

        for (Film f : films)
            System.out.println(f);

        Map<Integer, List<Film>> years = Film.groupByYear(films);
        Map<String, List<Film>> genres = Film.groupByGenre(films);


        "NEW LINE\n".chars()
                .mapToObj((ch) -> {
                    return (char) ch;
                })
                .forEach(System.out::print);


        years.forEach((k, v) -> {
            System.out.println("KEY: " + k);
            System.out.println("VALUES: ");
            v.forEach(f -> System.out.println("\t" + f));
        });


        System.out.println("Done.");
    }


    public static List<Film> readFilms() throws IOException
    {
        final List<Film> films = new LinkedList<>();

        Document doc = Jsoup.connect(url + "/chart/top")
                .userAgent(userAgent)
                .header("Accept-Language", "en")
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .referrer("http://www.google.com")
                .get();
        Element table = doc.selectFirst("tbody.lister-list");
        Elements rows = table.getElementsByTag("tr");

        ExecutorService pool = Executors.newFixedThreadPool(25);
        int iteration = 0;

        for (Element row : rows)
        {
            final Film film = new Film();
            Element title = row.selectFirst("td.titleColumn");
            try
            {
                film.setRank(Double.valueOf(title.ownText()).intValue());
                film.setTitle(title.selectFirst("a").ownText());
                film.setYear(Integer.parseInt(title.selectFirst("span.secondaryInfo")
                                                      .ownText()
                                                      .substring(1, 5)));
                film.setRatingIMDb(Double.parseDouble(row.selectFirst("td.ratingColumn.imdbRating")
                                                              .selectFirst("strong")
                                                              .ownText()));
                films.add(film);

                //переход по ссылке для извлечения жанров
                pool.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
//                            System.out.println("Thread " + Thread.currentThread().getName());
                            String ref = url + title.selectFirst("a").attr("href");
                            Elements elems = Jsoup.connect(ref)
                                    .header("Accept-Language", "en")
                                    .header("Accept-Encoding", "gzip,deflate,sdch")
                                    .get()
                                    .selectFirst("div.subtext")
                                    .getElementsByTag("a");

                            LinkedList<String> list = new LinkedList<>();
                            for (int i = 0; i < elems.size() - 1; i++)
                                list.add(elems.get(i).ownText());

                            if (!list.isEmpty())
                                film.setGenre(list);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
                System.err.println("FILM PROBLEM: " + (iteration++));
            }
//            System.out.println("FILMS READ: " + film.getRank());
        }

        //ожидание завершения всех потоков
        try
        {
            pool.shutdown();
            if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                pool.shutdownNow();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return films;
    }
}



