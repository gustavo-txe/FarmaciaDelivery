package com.app.farmaciadelivery.repository;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuarioRepository {
    private final DatabaseReference usuariosRef;

    public UsuarioRepository() {
        usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    }

    public interface DadosUsuarioCallback {
        void onDadosRecebidos(String telefone, String endereco);
        void onErro(DatabaseError error);
    }

    public void recuperarContatoEndereco(DadosUsuarioCallback callback) {

        usuariosRef.child(UsuarioFirebase.getUsuarioAtual().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String telefone = snapshot.child("telefone").getValue(String.class);
                        String endereco = snapshot.child("endereco").getValue(String.class);

                        callback.onDadosRecebidos(telefone, endereco);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onErro(error);
                    }
                });
    }
}
