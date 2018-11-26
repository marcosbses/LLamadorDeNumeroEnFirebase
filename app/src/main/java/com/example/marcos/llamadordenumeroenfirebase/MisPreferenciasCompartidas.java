package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;

public class MisPreferenciasCompartidas {
    public static String getIdDeAdministradorCreadorDeTicketeras(Context context){
        String id= context.getSharedPreferences("mis_prefs",Context.MODE_PRIVATE).getString("creadorUid",null);
        return id;
    }
    public static void setIdDeAdministradorCreadorDeTicketeras(Context context,String Uid){
        context.getSharedPreferences("mis_prefs",Context.MODE_PRIVATE).edit().putString("creadorUid",Uid).apply();
    }
    public static boolean getSoyAdmin(Context context){
        return context.getSharedPreferences("mis_prefs",Context.MODE_PRIVATE).getBoolean("soy_admin",false);
    }

    public static void setSoyAdmin(Context context,boolean soyAdmin){
        context.getSharedPreferences("mis_prefs",Context.MODE_PRIVATE).edit().putBoolean("soy_admin",soyAdmin).apply();
    }

}
