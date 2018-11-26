package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CodigoQr {
    public static Bitmap createBitmapFromQrText(String texto){
        File file;
        file= QRCode.from("Gran Aplicacion").to(ImageType.PNG).file();
        Bitmap bitmap=extractBitmap(file);
        return bitmap;
    }

    private static Bitmap extractBitmap(File file){
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

    public static Uri QrUriFromText(Context context,String texto){
        return FileProvider.getUriForFile(context, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", QrFileFromText(texto));
    }
    private static File QrFileFromText(String texto){
        File file=QRCode.from(texto).to(ImageType.PNG).file();
        return file;
    }
}
