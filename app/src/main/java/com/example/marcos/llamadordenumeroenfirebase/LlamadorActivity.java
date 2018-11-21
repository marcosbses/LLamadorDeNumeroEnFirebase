package com.example.marcos.llamadordenumeroenfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LlamadorActivity extends AppCompatActivity {

    private DatabaseReference numeroDatabaseReference;
    private int numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamador);



        String numeroDeTicketera=getIntent().getExtras().getString("NUMERO_TICKETERA");
        String nombreDeBotonIniciador=getIntent().getExtras().getString("NOMBRE_BOTON");
        ((TextView)findViewById(R.id.textView)).setText(nombreDeBotonIniciador+" ("+numeroDeTicketera+")");

        numeroDatabaseReference= FirebaseDatabase.getInstance().getReference().child("ticketera").child(numeroDeTicketera).child("numero");
        this.numero=-1;
        numeroDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numero=Integer.valueOf(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void llamarSiguiente(View v){
        if(this.numero!=-1){
            numero++;
            numeroDatabaseReference.setValue(String.valueOf(numero));
        }
    }


}
