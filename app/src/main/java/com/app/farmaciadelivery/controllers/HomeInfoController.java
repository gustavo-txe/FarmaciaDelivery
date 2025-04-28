package com.app.farmaciadelivery.controllers;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.ui.adapters.AdapterEmpresa;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeInfoController {

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private DatabaseReference empresaRef = firebaseRef.child("empresa");

    public void recuperarEmpresa(List<Empresa> empresaList, AdapterEmpresa adapterEmpresa) {

        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                empresaList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    empresaList.add(ds.getValue(Empresa.class));
                }

                adapterEmpresa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public FirebaseAuth getAutenticacao() {
        return autenticacao;
    }

}
