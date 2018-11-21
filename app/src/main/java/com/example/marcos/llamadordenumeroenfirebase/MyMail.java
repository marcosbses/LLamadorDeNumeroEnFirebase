package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

public class MyMail {
    private Context context;
    private String asunto;
    private String mensaje;
    private Uri attachmentUri;
    private MyMail(Context context){
        this.context=context;
    }
    public static class Builder {
        private Context context;
        private String asunto;
        private String mensaje;
        private Uri attachmentUri;
        public Builder(Context context){
            this.context=context;
        }
        public Builder setAsunto(String asunto){
            this.asunto=asunto;
            return this;
        }
        public Builder setMensaje(String mensaje){
            this.mensaje=mensaje;
            return this;
        }
        public Builder setAttachmentUri(Uri uri){
            this.attachmentUri=uri;
            return this;
        }
        public MyMail build(){
            MyMail myMail=new MyMail(this.context);
            myMail.asunto=this.asunto;
            myMail.mensaje=this.mensaje;
            myMail.attachmentUri=this.attachmentUri;
            return myMail;
        }
    }
    public void send(String address){
        Log.i("infor","texto es: "+mensaje);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {address};
        emailIntent.putExtra(Intent.EXTRA_TEXT,mensaje);
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, attachmentUri);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, asunto);
        context.startActivity(emailIntent);

    }
}
