package com.example.marcos.llamadordenumeroenfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EscogerTicketeraDeCreadorActivity extends AppCompatActivity implements Ticketeras.Callback {

    private String creador;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_ticketera_de_creador);

        TextView textView=new TextView(this);
        textView.setText("Hola desde actividad escoger ticketeras");
        ((ViewGroup)findViewById(R.id.root_layout)).addView(textView);
        String creador=this.getIntent().getExtras().getString("EXTRA_CREADOR");
        Log.i("infor","lo tenemos a "+creador);
        this.creador=creador;
        this.databaseReference= FirebaseDatabase.getInstance().getReference();
        new Ticketeras(databaseReference,this).buscarTicketerasDeCreador(creador);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usuario_comun_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.o_c:
                obtenerNombreDeCreadorPorEscaneoDeQr();
                return true;
            case R.id.h_a:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener crearOnClickListener(final Ticketera ticketera){
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("infor","on click");
                Intent intent=new Intent(getApplicationContext(),LlamadorActivity.class);
                intent.putExtra("NUMERO_TICKETERA",ticketera.getNumeroDeTicketera());
                intent.putExtra("NOMBRE_BOTON",((Button)view).getText().toString());
                startActivity(intent);
            }
        };
        return onClickListener;
    }


    @Override
    public void ticketerasEncontradas(Ticketera[] ticketeras) {
        Log.i("infor","ticketeras encontradas en Escoger Ticketera...");
        ((LinearLayout)findViewById(R.id.linear_layout)).removeAllViews();
        int i=0;
        for(Ticketera ticketera:ticketeras){
            i++;
            Log.i("infor","numero de ticketera: "+ticketera.getNumeroDeTicketera());
            //creo boton
            Button button=new Button(this);
            button.setText("Ticketera: "+i);
            button.setOnClickListener(crearOnClickListener(ticketera));

            //agrego el boton al layout
            ((LinearLayout)findViewById(R.id.linear_layout)).addView(button);
        }
    }

    public void obtenerNombreDeCreadorPorEscaneoDeQr(){
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
                    getSharedPreferences("preferencias_generales", Context.MODE_PRIVATE).edit().putString("creador_de_ticketeras",data.getExtras().getString("NOMBRE_CREADOR")).apply();
                    this.creador=data.getExtras().getString("NOMBRE_CREADOR");
                    new Ticketeras(databaseReference,this).buscarTicketerasDeCreador(creador);
                }
            }
        }
    }
}
