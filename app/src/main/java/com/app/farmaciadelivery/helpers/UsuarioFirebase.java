package com.app.farmaciadelivery.helpers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.models.Usuario;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.app.farmaciadelivery.ui.empresa.home.EmpresaActivity;
import com.app.farmaciadelivery.ui.clientes.home.HomeInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();

    }

    public static boolean atualizarNomeUsuario(String nome) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder().setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d("Perfil", "Erro ao atulizar nome de perfil");
                }
            });

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void redirecionaUsuarioLogado(Activity activity) {

        FirebaseUser user = getUsuarioAtual();

        if (user != null) {

            DatabaseReference usuariosRef = ConfigFirebase.getFirebase()
                    .child("usuarios")
                    .child(getIdUsuario());
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Usuario usuario = snapshot.getValue(Usuario.class);
                    String tipoUsuario = usuario.getTipo();

                    if (tipoUsuario.equals("E")) {

                        Intent i = new Intent(activity, EmpresaActivity.class);
                        activity.startActivity(i);

                    } else {

                        Intent i = new Intent(activity, HomeInfoActivity.class);
                        activity.startActivity(i);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public static String getIdUsuario() {
        FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();

    }

}


