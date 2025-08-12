package com.app.farmaciadelivery.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth referenciaAutenticacao;
    private static DatabaseReference referenciaFirebase;

    public static String getIdUsuario() {

        FirebaseAuth autenticacao = getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            return autenticacao.getCurrentUser().getUid();
        }
        return null;

    }

    public static DatabaseReference getFirebase() {

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {

        if (referenciaAutenticacao == null) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

}
