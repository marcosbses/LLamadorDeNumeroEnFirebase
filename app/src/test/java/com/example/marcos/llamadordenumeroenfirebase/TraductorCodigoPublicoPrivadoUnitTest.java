package com.example.marcos.llamadordenumeroenfirebase;

import org.junit.Test;
import static org.junit.Assert.*;

public class TraductorCodigoPublicoPrivadoUnitTest {
    @Test
    public void traducirCodigoDePublicoAPrivado(){

        try {
            assertEquals("Rxz0",TraductorCodigoPublicoPrivado.traducirCodigoDePublicoAPrivado("Pxz1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
