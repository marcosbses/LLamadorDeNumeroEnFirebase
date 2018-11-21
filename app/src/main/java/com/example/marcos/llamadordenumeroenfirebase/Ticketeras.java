package com.example.marcos.llamadordenumeroenfirebase;

import android.telecom.Call;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ticketeras {
    public interface Callback{
        void ticketerasEncontradas(Ticketera[] ticketeras);
    }

    DatabaseReference databaseReference;
    Callback callback;
    public Ticketeras(DatabaseReference databaseReference, Ticketeras.Callback callback){
        this.databaseReference=databaseReference;
        this.callback=callback;
    }
    public void buscarTicketerasDeCreador(final String creador){
        databaseReference.child("ticketera").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ticketera> ticketerasList=new ArrayList<>();

                Iterator<DataSnapshot> ticketeras=dataSnapshot.getChildren().iterator();
                while(ticketeras.hasNext()){
                    //Log.i("infor","ticketera snapshot has next");
                    try {
                        DataSnapshot ticketeraSnapshot=ticketeras.next();
                        //Log.i("infor", "key: " + ticketeraSnapshot.child("creador").getValue().toString());
                        if(ticketeraSnapshot.child("creador").getValue().toString().equals(creador)) {
                            Ticketera ticketera = Ticketera.fromDataSnapshot(ticketeraSnapshot);
                            ticketerasList.add(ticketera);
                        }
                        //Log.i("infor","id de ticketera: "+ticketera.getId());
                    }catch(NullPointerException e){
                        //Log.i("infor","child not found");
                    }
                }
                //convierto lista a array y paso a callback
                Ticketera[] ticketeras2=new Ticketera[ticketerasList.size()];
                ticketeras2=ticketerasList.toArray(ticketeras2);
                callback.ticketerasEncontradas(ticketeras2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void buscarTodasLasTicketeras(){
        databaseReference.child("ticketera").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ticketera> ticketerasList=new ArrayList<>();

                Iterator<DataSnapshot> ticketeras=dataSnapshot.getChildren().iterator();
                while(ticketeras.hasNext()){
                    Log.i("infor","ticketera snapshot has next");

                        DataSnapshot ticketeraSnapshot=ticketeras.next();
                        //Log.i("infor", "key: " + ticketeraSnapshot.child("creador").getValue().toString());
                    try{
                        Ticketera ticketera = Ticketera.fromDataSnapshot(ticketeraSnapshot);
                        ticketerasList.add(ticketera);
                    }catch(NullPointerException e){
                        Log.i("infor","ticketera no valida");
                    }



                }
                //convierto lista a array y paso a callback
                Ticketera[] ticketeras2=new Ticketera[ticketerasList.size()];
                ticketeras2=ticketerasList.toArray(ticketeras2);
                callback.ticketerasEncontradas(ticketeras2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
