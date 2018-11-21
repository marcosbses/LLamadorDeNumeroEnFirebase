package com.example.marcos.llamadordenumeroenfirebase;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConversorBaseSesentaYDosTest {
    @Test
    public void convertirABaseDiezTest(){
        ConversorBaseSesentaYDos conversor=new ConversorBaseSesentaYDos();
        assertEquals(136587,conversor.convertirABaseDiez("zx1"));
    }
}
