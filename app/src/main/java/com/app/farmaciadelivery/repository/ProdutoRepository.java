package com.app.farmaciadelivery.repository;

import static com.app.farmaciadelivery.helpers.UsuarioFirebase.getIdUsuario;

import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProdutoRepository {

    public void salvar(Produto produto) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(produto.getIdUsuario())
                .child(produto.getIdProduto());
        produtoRef.setValue(produto);
    }

    public void removerProduto() {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidosusuario")
                .child(getIdUsuario())
                .child("oLuJHUaBy3cW04i9laZ6zb18ccW2");
        pedidoRef.getRef().removeValue();

    }

    public void removerImagem(Produto produto) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(produto.getUrlImage());
        storageReference.delete();

    }

    public void removerItem(Produto produto) {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdUsuario())
                .child(produto.getIdProduto());
        produtoRef.getRef().removeValue();

    }

}
