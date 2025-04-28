package com.app.farmaciadelivery.controllers;

import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.repository.ProdutoRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class EmpresaController {

    private FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private String idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    private DatabaseReference produtosRef;
    private ProdutoRepository produtoRepository = new ProdutoRepository();

    public DatabaseReference getProdutosRef() {
        produtosRef = firebaseRef.child("produtos").child(idUsuarioLogado);
        return produtosRef;
    }

    public FirebaseAuth getAutenticacao() {
        return autenticacao;
    }

    public DatabaseReference getFirebaseRef() {
        return firebaseRef;
    }

    public void removerImagem(Produto produto) {
        produtoRepository.removerImagem(produto);
    }

    public void removerItem(Produto produto) {
        produtoRepository.removerItem(produto);
    }

}
