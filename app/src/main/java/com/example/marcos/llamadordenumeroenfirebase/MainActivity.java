package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Ticketeras.Callback{

    DatabaseReference databaseReference;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseReference=FirebaseDatabase.getInstance().getReference();
        this.imageView=(ImageView)findViewById(R.id.imageView);

    }

    public void aumentar(View v){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int valor= Integer.parseInt(dataSnapshot.getValue().toString());
                valor=valor+1;
                dataSnapshot.getRef().setValue(valor);
                Log.i("infor","valor: "+valor);
                Toast.makeText(MainActivity.this,"valor: "+valor,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void crearImagenQr(View v){

        File file;
        file= QRCode.from("Gran Aplicacion").to(ImageType.PNG).file();
        Bitmap bitmap=extractBitmap(file);
        imageView.setImageBitmap(bitmap);

    }

    private Bitmap extractBitmap(File file){
        Bitmap bitmap = null;;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void enviarMail(View v){
        Log.i("infor","enviar mail");
        File file=crearQrPngFile();
        enviarMail(file,"marcosucu@gmail.com");
    }

    public void nuevaTicketera(View v){
        new Ticketera(databaseReference).publicarEnFirebase();

    }

    private File crearQrPngFile(){
        File file=QRCode.from("t1").to(ImageType.PNG).file();
        return file;
    }

    private Uri crearNuevoCodigoUri(){
        return uriFromFile(crearQrPngFile());
    }

    private Uri uriFromFile(File file){
        return FileProvider.getUriForFile(this, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", file);
    }


    private String nuevoIdentificadorPublicoDeTicketera(){
        return null;
    }

    private void enviarMail(File attachment,String address){
        Log.i("infor","cache path :"+getCacheDir().getPath());

        //Uri path = Uri.fromFile(attachment);
        Uri path = FileProvider.getUriForFile(this, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", attachment);
        MyMail myMail=new MyMail.Builder(this).setAsunto("Nuevo Codigo").setMensaje("Este es el Nuevo codigo").setAttachmentUri(path).build();
        myMail.send(address);
    }


    public void crearTicketera(View v){
        //new Ticketera(databaseReference).setCreador("carlos").setId("zx1").setNumeroTicketera("t2").publicarEnFirebase();
        new PublicadorDeNuevaTicketera(this,databaseReference).publicar();
    }

    public void autenticar(View v){
        // Choose authentication providers
        /*List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                41);
    }

    public void encontrarValoresRequeridosParaCrearNuevoQr(View v){
        new PublicadorDeNuevaTicketera(this,databaseReference).publicar();*/
    }



    private Uri QrUriFromText(String texto){
        return FileProvider.getUriForFile(this, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", QrFileFromText(texto));
    }
    private File QrFileFromText(String texto){
        File file=QRCode.from(texto).to(ImageType.PNG).file();
        return file;
    }

    public void buscarTicketeras(View v){
        Ticketeras ticketeras=new Ticketeras(databaseReference,this);
        ticketeras.buscarTicketerasDeCreador("carlos");
    }


    @Override
    public void ticketerasEncontradas(Ticketera[] ticketeras) {
        Log.i("infor","ticketeras encontradas");
        for(Ticketera ticketera:ticketeras){
            Log.i("infor","ticketera id"+ticketera.getId());
        }
    }

    public void escogerTicketera(View v){
        Intent intent=new Intent(this,EscogerTicketeraDeCreadorActivity.class);
        String nombreDeCredor=getSharedPreferences("preferencias_generales", Context.MODE_PRIVATE).getString("creador_de_ticketeras",null);
        intent.putExtra("EXTRA_CREADOR",nombreDeCredor);
        startActivity(intent);
    }

    public void obtenerNombreDeCreador(View v){
        Intent intent=new Intent(this,ObtenerNombreDeCreadorDeTicketeraPorQrActivity.class);
        startActivityForResult(intent,22);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("infor","on activity result");
        Log.i("infor","nombre de creador: "+data.getExtras().getString("NOMBRE_CREADOR"));
        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                Log.i("infor","guardar  preferencias");
                if(!data.getExtras().getString("NOMBRE_CREADOR").equals("Codigo no reconocido")){
                    getSharedPreferences("preferencias_generales",Context.MODE_PRIVATE).edit().putString("creador_de_ticketeras",data.getExtras().getString("NOMBRE_CREADOR")).apply();
                }
            }
        }
    }
}
