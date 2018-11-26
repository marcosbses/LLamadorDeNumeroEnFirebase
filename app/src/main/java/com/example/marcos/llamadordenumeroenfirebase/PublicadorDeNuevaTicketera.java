package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;

public class PublicadorDeNuevaTicketera implements UltimoIdentificadorDeTicketera.OnIdentificadorEncontrado,UltimoNumeroDeTicketera.OnUltimoNumeroDeTicketeraEncondradoListener {

    private Context context;
    private DatabaseReference databaseReference;
    private String ultimoNumeroDeTicketera;
    private String ultimoIdentificadorDeTicketera;

    public PublicadorDeNuevaTicketera(Context context,DatabaseReference databaseReference){
        this.databaseReference=databaseReference;
        this.context=context;
    }

    public void publicar(){
        Log.i("infor","publicar");
        new UltimoNumeroDeTicketera(databaseReference).encontrarUltimoNumeroDeTicketera(this);
        new UltimoIdentificadorDeTicketera(databaseReference).encontrarUltimoIdentificador(this);
    }


    @Override
    public void onUlimoIdentificadorDeTicketeraEncontrado(String ultimoIdentificador) {
        Log.i("infor","identificador encontrado");
        Log.i("infor",ultimoIdentificador);
        this.ultimoIdentificadorDeTicketera=ultimoIdentificador;
        if(ultimoIdentificadorDeTicketera!=null&&ultimoNumeroDeTicketera!=null){
            valoresRequeridosEncontradosParaPublicar();
        }
    }

    @Override
    public void onUltimoNumeroDeTicketeraEncondrado(String ultimoNumeroDeTicketera) {
        Log.i("infor","utlimo numero de ticketera encontrado: "+ultimoNumeroDeTicketera);
        this.ultimoNumeroDeTicketera=ultimoNumeroDeTicketera;
        if(ultimoIdentificadorDeTicketera!=null&&this.ultimoNumeroDeTicketera!=null){
            valoresRequeridosEncontradosParaPublicar();
        }
    }

    private void valoresRequeridosEncontradosParaPublicar(){
        Log.i("infor","valores requeridos encontrados");
        Log.i("infor","ultimo identificador: "+ultimoIdentificadorDeTicketera);

        ConversorBaseSesentaYDos conversor=new ConversorBaseSesentaYDos();
        //actualizo el identificador de ticketera para que crear una nueva ticketera
        int a=conversor.convertirABaseDiez(ultimoIdentificadorDeTicketera.substring(1,ultimoIdentificadorDeTicketera.length()));
        String nuevoCodigo="P"+conversor.convertirABaseSesentaYDos(a+1);
        //creo y envio mail con nuevo codigo publico
        MyMail myMail=new MyMail.Builder(context).setAsunto("Nuevo codigo").setMensaje("este es el nuevo codigo").setAttachmentUri(QrUriFromText(nuevoCodigo)).build();

        //myMail.send("marcosucu@gmail.com"); lo actualizo
        Log.i("infor","Se envia mail con nuevo codigo Qr a :"+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        myMail.send(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        //new Ticketera(databaseReference).setNumeroTicketera(ultimoNumeroDeTicketera).aumentarNumeroDeTicketera().setCreador("carlos").setId(nuevoCodigo).publicarEnFirebase(); lo actualizo
        new Ticketera(databaseReference).setNumeroTicketera(ultimoNumeroDeTicketera).aumentarNumeroDeTicketera().setCreador(FirebaseAuth.getInstance().getCurrentUser().getUid()).setId(nuevoCodigo).publicarEnFirebase();

    }

    private Uri QrUriFromText(String texto){
        return FileProvider.getUriForFile(context, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", QrFileFromText(texto));
    }
    private File QrFileFromText(String texto){
        File file= QRCode.from(texto).to(ImageType.PNG).file();
        return file;
    }
}
