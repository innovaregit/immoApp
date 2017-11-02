package com.innovare.marceloagustini.immoapp.utilidades;

import com.innovare.marceloagustini.immoapp.clases.Publicacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marceloagustini on 2/11/17.
 */

public class HardcodePubs {
    public static ArrayList<Publicacion> fillPubs() {
        ArrayList<Publicacion> lista = new ArrayList<Publicacion>();
        //
        Publicacion pub1 = new Publicacion();
        pub1.setId(new Long(1));
        pub1.setTipo("alquiler");
        pub1.setTitulo("Alquilo Casa en zona centro");
        pub1.setDescripcion("Excelente oportunidad, casa en centro a dos cuadras de casa de gobierno");
        pub1.setValor(3000.);
        lista.add(pub1);
        //
        //
        Publicacion pub2 = new Publicacion();
        pub2.setId(new Long(1));
        pub2.setTipo("venta");
        pub2.setTitulo("Vendo Casa en zona centro");
        pub2.setDescripcion("Excelente oportunidad, casa en centro a dos cuadras de casa de gobierno");
        pub2.setValor(1800000.);
        lista.add(pub2);
        //

        Publicacion pub3 = new Publicacion();
        pub3.setId(new Long(1));
        pub3.setTipo("venta");
        pub3.setTitulo("Vendo monoambiente a estrenar");
        pub3.setDescripcion("Excelente oportunidad, nuevo en el piso 29 ");
        pub3.setValor(1100000.00);
        lista.add(pub3);
        //
        return lista;
    }
}
