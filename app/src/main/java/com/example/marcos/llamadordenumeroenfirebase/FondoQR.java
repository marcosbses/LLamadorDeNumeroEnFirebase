package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FondoQR {
    //devuelve png de 400x400
    public static Uri pegar(Context context,Uri qrUri){
        Bitmap fondoBitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.frame);
        Bitmap qrBitmap=null;
        Bitmap overlayBitmap=null;
        try {
            InputStream is=context.getContentResolver().openInputStream(qrUri);
            qrBitmap=BitmapFactory.decodeStream(is);
            qrBitmap=Bitmap.createBitmap(qrBitmap,0,0,(int)(qrBitmap.getWidth()*0.9),(int)(qrBitmap.getHeight()*0.9));//crop image
            overlayBitmap=overlay(fondoBitmap,qrBitmap);
            Log.i("infor","overlayBitmap width: "+overlayBitmap.getWidth());
            overlayBitmap=Bitmap.createScaledBitmap(overlayBitmap,400,400,false);
            Log.i("infor","overlayBitmap width after scaling "+overlayBitmap.getWidth());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getImageUri(context,overlayBitmap);
    }

    private static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    private static Uri getImageUri(Context context,Bitmap inImage) {
        try {
            //FileOutputStream fileOutputStream=context.openFileOutput("output",Context.MODE_PRIVATE);
            File temp=new File(context.getCacheDir(),"imprimir.png");

            FileOutputStream fileOutputStream=new FileOutputStream(temp);
            inImage.compress(Bitmap.CompressFormat.PNG,50,fileOutputStream);
            //return Uri.fromFile(context.getFileStreamPath("output"));
            return FileProvider.getUriForFile(context, "com.example.marcos.llamadordenumeroenfirebase.fileprovider", temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
