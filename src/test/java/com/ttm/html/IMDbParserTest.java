package com.ttm.html;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


public class IMDbParserTest
{

    @Test
    public void shouldAnswerWithTrue()
    {
        try
        {
            List<Film> films = IMDbParser.readFilms();
            Film fm = films.stream()
                    .filter(f -> f.getTitle().equals("Casino"))
                    .findFirst()
                    .get();
            Assert.assertArrayEquals(new String[]{"Crime", "Drama"}, fm.getGenre().toArray());
            Assert.assertEquals(1995, fm.getYear());

            fm = films.stream()
                    .filter(f -> f.getTitle().equals("The Departed"))
                    .findFirst()
                    .get();
            Assert.assertArrayEquals(new String[]{"Crime", "Drama", "Thriller"}, fm.getGenre().toArray());
            Assert.assertEquals(2006, fm.getYear());

            fm = films.stream()
                    .filter(f -> f.getTitle().equals("Whiplash"))
                    .findFirst()
                    .get();
            Assert.assertArrayEquals(new String[]{"Drama", "Music"}, fm.getGenre().toArray());
            Assert.assertEquals(2014, fm.getYear());


        }
        catch (IOException | NoSuchElementException e)
        {
            e.printStackTrace();
            Assert.fail("IOException");
        }

        Assert.assertTrue(true);
    }
}
