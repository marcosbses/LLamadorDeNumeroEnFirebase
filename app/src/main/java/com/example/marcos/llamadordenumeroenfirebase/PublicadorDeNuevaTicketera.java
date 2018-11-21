package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;

public class PublicadorDeNuevaTicketera implements UltimoIdentificadorDeTicketera.OnIdentificadorEncontrado,UltimoNumeroDeTicketera.OnUltimoNumeroDeTicketeraEncondradoListener {

    private Context context;
    private DatabaseReference databaseReference;
    private String ultimoNumeroDeTicketera;
    private String ultimoIdentificadorDeTicketera;
    private boolean codigoPrivadoParaCompartir=false;

    public PublicadorDeNuevaTicketera(Context context,DatabaseReference databaseReference){
        this.databaseReference=databaseReference;
        this.context=context;
    }

    public void publicar(){
        Log.i("infor","publicar");
        new UltimoNumeroDeTicketera(databaseReference).encontrarUltimoNumeroDeTicketera(this);
        new UltimoIdentificadorDeTicketera(databaseReference).encontrarUltimoIdentificador(this);
    }

    public void publicarConCodigoPrivadoParaCompartir(){
        this.codigoPrivadoParaCompartir=true;
        new UltimoNumeroDeTicketera(databaseReference).encontrarUltimoNumeroDeTicketera(this);
        new UltimoIdentificadorDeTicketera(databaseReference).encontrarUltimoIdentificador(this);
    }

    @Override
    public void onUlimoIdentificadorDeTicketeraEncontrado(String ultimoIdentificador) {
        Log.i("infor","identificador encontrado");
        Log.i("infor",ultimoIdentificador);
        this.ultimoIdentificadorDeTicketera=ultimoIdentificador;
        if(ultimoIdentificadorDeTicketera!=null&&ultimoNumeroDeTicketera!=null){
            valoresRequeridosEncontrados();
        }
    }

    @Override
    public void onUltimoNumeroDeTicketeraEncondrado(String ultimoNumeroDeTicketera) {
        Log.i("infor","utlimo numero de ticketera encontrado: "+ultimoNumeroDeTicketera);
        this.ultimoNumeroDeTicketera=ultimoNumeroDeTicketera;
    }

    private void valoresRequeridosEncontrados(){
        Log.i("infor","valores requeridos encontrados");
        Log.i("infor","ultimo identificador: "+ultimoIdentificadorDeTicketera);

        ConversorBaseSesentaYDos conversor=new ConversorBaseSesentaYDos();
        //actualizo el identificador de ticketera para que crear una nueva ticketera
        int a=conversor.convertirABaseDiez(ultimoIdentificadorDeTicketera.substring(1,ultimoIdentificadorDeTicketera.length()));
        String nuevoCodigo="P"+conversor.convertirABaseSesentaYDos(a+1);
        //creo y envio mail con nuevo codigo publico
        MyMail myMail=new MyMail.Builder(context).setAsunto("nuevo codigo").setMensaje("este es el nuevo codigo").setAttachmentUri(QrUriFromText(nuevoCodigo)).build();
        myMail.send("marcosucu@gmail.com");
        if(codigoPrivadoParaCompartir){
            enviarMailConCodigoParaCompartir();
        }

        new Ticketera(databaseReference).setNumeroTicketera(ultimoNumeroDeTicketera).aumentarNumeroDeTicketera().setCreador("carlos").setId(nuevoCodigo).publicarEnFirebase();

    }

    private void enviarMailConCodigoParaCompartir(){
        int a=ConversorBaseSesentaYDos.convertirABaseDiez(ultimoIdentificadorDeTicketera);
        String nuevoCodigo=ConversorBaseSesentaYDos.convertirABaseSesentaYDos(a+1);
        MyMail myMail= null;
        try {
            myMail = new MyMail.Builder(context).setAsunto("nuevo codigo privado").setMensaje("este es privado y para compartir").setAttachmentUri(QrParaCompartirUriFromText("P"+nuevoCodigo)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myMail.send("marcosucu@gmail.com");
    }


    private Uri QrParaCompartirUriFromText(String codigoPublico) throws Exception {
        String paraCompartir=TraductorCodigoPublicoPrivado.traducirCodigoDePublicoAPrivado(codigoPublico);
        return FileProvider.getUriForFile(context, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", QrFileFromText(paraCompartir));
    }

    private Uri QrUriFromText(String texto){
        return FileProvider.getUriForFile(context, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", QrFileFromText(texto));
    }
    private File QrFileFromText(String texto){
        File file= QRCode.from(texto).to(ImageType.PNG).file();
        return file;
    }
}
