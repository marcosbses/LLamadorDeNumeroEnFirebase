package com.example.marcos.llamadordenumeroenfirebase;

public class ConversorBaseSesentaYDos {
    private static final String[] simbolos=new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public static String convertirABaseSesentaYDos(int baseDiez){
        String ret="";

        while(baseDiez>61){
            System.out.println(baseDiez%62);
            ret=ret+simbolos[baseDiez%62];
            baseDiez=baseDiez/62;
        }
        System.out.println(baseDiez);
        ret=ret+simbolos[baseDiez];
        return new StringBuilder(ret).reverse().toString();
    }

    public static int convertirABaseDiez(String base62){
        int ret=0;
        for(int i=0;i<base62.length();i++){
            int base=digitoBse62ABaseDiez(String.valueOf(base62.charAt(i)));
            ret=ret+base*(int)Math.pow(62,base62.length()-i-1);

        }
        return ret;
    }

    private static int digitoBse62ABaseDiez(String digito){
        return java.util.Arrays.asList(simbolos).indexOf(digito);
    }
}
