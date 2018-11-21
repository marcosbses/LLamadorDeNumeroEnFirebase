package com.example.marcos.llamadordenumeroenfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UltimoIdentificadorDeTicketera {
    public interface OnIdentificadorEncontrado{
        public void onUlimoIdentificadorDeTicketeraEncontrado(String ultimoIdentificador);
    }

    private DatabaseReference databaseReference;

    public UltimoIdentificadorDeTicketera(DatabaseReference databaseReference){
        this.databaseReference=databaseReference;
    }



    public void encontrarUltimoIdentificador(final OnIdentificadorEncontrado onIdentificadorEncontrado){


        databaseReference.child("ticketera").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String identificador=dataSnapshot.getChildren().iterator().next().child("id").getValue().toString();
                onIdentificadorEncontrado.onUlimoIdentificadorDeTicketeraEncontrado(identificador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
