package com.example.marcos.llamadordenumeroenfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UltimoNumeroDeTicketera {
    public interface OnUltimoNumeroDeTicketeraEncondradoListener{
        void onUltimoNumeroDeTicketeraEncondrado(String ultimoNumeroDeTicketera);
    }
    private DatabaseReference databaseReference;

    public UltimoNumeroDeTicketera(DatabaseReference databaseReference){
        this.databaseReference=databaseReference;
    }

    public void encontrarUltimoNumeroDeTicketera(final OnUltimoNumeroDeTicketeraEncondradoListener listener){
        databaseReference.child("ticketera").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ultimoNumero=dataSnapshot.getChildren().iterator().next().getKey();
                listener.onUltimoNumeroDeTicketeraEncondrado(ultimoNumero);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
