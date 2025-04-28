package com.app.farmaciadelivery.controllers;

import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class EditController {

    public void atualizarProduto(Map<String, Object> updates, String extraUpdateID) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child("oLuJHUaBy3cW04i9laZ6zb18ccW2")
                .child(extraUpdateID);
        produtoRef.updateChildren(updates);
    }

}
