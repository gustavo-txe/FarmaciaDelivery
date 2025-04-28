package com.app.farmaciadelivery.controllers;

import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilUsuarioController {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public interface DadosUsuarioCallback {
        void onEnderecoRecebido(String endereco);

        void onTelefoneRecebido(String telefone);
    }

    public void recuperarDadosUsuario(final DadosUsuarioCallback callback) {
        String uid = UsuarioFirebase.getUsuarioAtual().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);

        ref.child("endereco").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String endereco = snapshot.getValue(String.class);
                callback.onEnderecoRecebido(endereco);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        ref.child("telefone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String telefone = snapshot.getValue(String.class);
                callback.onTelefoneRecebido(telefone);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public DatabaseReference getUserRef() {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference userRef = firebaseRef
                .child("usuarios")
                .child(UsuarioFirebase.getUsuarioAtual().getUid());

        return userRef;
    }

    public String getNomeUser() {
        return UsuarioFirebase.getUsuarioAtual().getDisplayName();
    }

    public String getEmailUser() {
        return UsuarioFirebase.getUsuarioAtual().getEmail();
    }

    public FirebaseUser getUser() {
        return user;
    }
}


