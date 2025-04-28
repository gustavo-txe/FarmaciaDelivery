package com.app.farmaciadelivery.models;

import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {
    private String id;
    private String nome;
    private String tipo;

    public Usuario() {
    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(getId());
        usuarios.setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
