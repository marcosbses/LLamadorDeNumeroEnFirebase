package com.example.marcos.llamadordenumeroenfirebase;


public class TraductorCodigoPublicoPrivado {

    public static String traducirCodigoDePublicoAPrivado(String publico) throws Exception {
        String prefix=String.valueOf(publico.charAt(0));
        if(!prefix.equals("P")){
            throw new Exception("Codigo publico debe comenzar con P");
        }
        publico=publico.substring(1,publico.length());
        int codigo=ConversorBaseSesentaYDos.convertirABaseDiez(publico);
        return "R"+ConversorBaseSesentaYDos.convertirABaseSesentaYDos(codigo-1);

    }

    public static String traducirCodigoDePrivadoAPublico(String privado) throws Exception{
        String prefix=String.valueOf(privado.charAt(0));
        if(!prefix.equals("R")){
            throw new Exception("Codigo publico debe comenzar con P");
        }
        privado=privado.substring(1,privado.length());
        int codigo=ConversorBaseSesentaYDos.convertirABaseDiez(privado);
        return "P"+ConversorBaseSesentaYDos.convertirABaseSesentaYDos(codigo+1);
    }
}
