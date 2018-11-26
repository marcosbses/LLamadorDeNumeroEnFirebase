package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.FocusingProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class ObtenerNombreDeCreadorDeTicketeraPorQrActivity extends AppCompatActivity implements SurfaceHolder.Callback {



    private boolean surfaceAvailable;
    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    private String codigoPublico;




    private class FirstBarcodeFocusingProcessor extends FocusingProcessor<Barcode> {


        public FirstBarcodeFocusingProcessor(Detector<Barcode> detector, Tracker<Barcode> tracker) {
            super(detector, tracker);
        }

        @Override
        public int selectFocus(Detector.Detections<Barcode> detections) {
            Log.i("infor","select focus");
            return detections.getDetectedItems().keyAt(0);
        }
    }
    private class QrBarcodeTracker extends Tracker<Barcode> {
        private Context context;
        private ObtenerNombreDeCreadorDeTicketeraPorQrActivity obtenerNombreDeCreadorDeTicketeraPorQrActivity;
        public QrBarcodeTracker(Context context, ObtenerNombreDeCreadorDeTicketeraPorQrActivity obtenerNombreDeCreadorDeTicketeraPorQrActivity){
            super();
            this.context=context;
            this.obtenerNombreDeCreadorDeTicketeraPorQrActivity=obtenerNombreDeCreadorDeTicketeraPorQrActivity;
        }
        @Override
        public void onNewItem(int id, final Barcode barcode){
            Log.i("infor","on new item");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,barcode.displayValue,Toast.LENGTH_LONG).show();

                    try {

                        obtenerNombreDeCreadorDeTicketeraPorQrActivity.nombreDeCreadorEncontrado(barcode.displayValue);
                    } catch (Exception e) {
                        Log.i("infor","exceptcion de traduccion de codigos");
                        Toast.makeText(context,"No se reconoce codigo",Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }

                }
            });


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtener_nombre_de_creador_de_ticketera_por_qr);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            iniciarCamara();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 22);
        }
    }


    private void iniciarCamara() {
        Log.i("infor","iniciar camara");

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (cameraSource == null) {
                crearCameraSource();
            }
            if (surfaceAvailable) {
                Log.i("infor","hay superficie");
                cameraSource.start(surfaceView.getHolder());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void crearCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE|Barcode.UPC_A).build();
        Tracker<Barcode> barcodeTracker=new QrBarcodeTracker(getApplicationContext(),this);
        FirstBarcodeFocusingProcessor firstBarcodeFocusingProcessor=new FirstBarcodeFocusingProcessor(barcodeDetector,barcodeTracker);
        barcodeDetector.setProcessor(firstBarcodeFocusingProcessor);
        CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).build();
        this.cameraSource = cameraSource;

    }





    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("infor","surface created");
        surfaceAvailable = true;
        iniciarCamara();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void nombreDeCreadorEncontrado(String nombreCreador){
        Intent data=new Intent();
        data.putExtra("NOMBRE_CREADOR",nombreCreador);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        cameraSource.stop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cameraSource.release();
    }


}
