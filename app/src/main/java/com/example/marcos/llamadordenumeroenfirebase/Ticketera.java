package com.example.marcos.llamadordenumeroenfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ticketera {
    private DatabaseReference databaseReference;
    private String creador;
    private String id;
    private String numeroTicketera;//numero relacionado con codigo o identificador
    private String numeroLlamado;
    private String numerosSacados;


    public Ticketera(DatabaseReference databaseReference){
        this.databaseReference=databaseReference;
    }

    public Ticketera setCreador(String creador) {
        this.creador = creador;
        return this;
    }

    public Ticketera setId(String id) {
        this.id = id;
        return this;
    }

    public Ticketera setNumeroTicketera(String numeroTicketera) {
        this.numeroTicketera = numeroTicketera;
        return this;
    }

    public Ticketera aumentarNumeroDeTicketera(){
        String numeroString=numeroTicketera.substring(1,numeroTicketera.length());
        int numero=Integer.valueOf(numeroString);
        numero++;
        numeroTicketera="t"+String.valueOf(numero);
        return this;
    }

    public String getId(){return this.id;}

    public String getNumeroDeTicketera(){ return this.numeroTicketera;}

    public String getCreador(){return this.creador;}

    public void publicarEnFirebase(){
        Map<String,String> mapa=new HashMap<>();
        mapa.put("id",id);
        mapa.put("creador",creador);
        mapa.put("numero","0");
        mapa.put("numeros_sacados","0");
        databaseReference.child("ticketera").child(numeroTicketera).setValue(mapa);
    }

    public static Ticketera fromDataSnapshot(DataSnapshot ticketeraSnapshot){
        Ticketera ticketera=new Ticketera(ticketeraSnapshot.getRef().getRoot());
        String creador=ticketeraSnapshot.child("creador").getValue().toString();
        String id=ticketeraSnapshot.child("id").getValue().toString();
        String numeroLlamado=ticketeraSnapshot.child("numero").getValue().toString();
        String numeroDeTicketera=ticketeraSnapshot.getKey();
        ticketera.creador=creador;
        ticketera.numeroTicketera=numeroDeTicketera;
        ticketera.id=id;
        ticketera.numeroLlamado=numeroLlamado;
        ticketera.numerosSacados=ticketeraSnapshot.child("numeros_sacados").getValue().toString();
        return ticketera;

    }

}
