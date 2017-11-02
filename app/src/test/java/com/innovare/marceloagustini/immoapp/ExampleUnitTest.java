package com.innovare.marceloagustini.immoapp;

import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.utilidades.HardcodePubs;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void readList() throws Exception {
        for (Publicacion p: HardcodePubs.fillPubs()) {
            System.out.println(p.getTitulo());
        }
    }
}