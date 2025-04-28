package com.app.farmaciadelivery.controllers;

import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.repository.ProdutoRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddProdutoController {

    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private String idUsuarioLogado = ConfigFirebase.getIdUsuario();
    private ProdutoRepository produtoRepository = new ProdutoRepository();
    private StorageReference fileRef;

    public void setProduto(Produto produto, String preco, String nome, String descricao, String ctg, String promo) {

        produto.setPreco(Double.parseDouble(preco));
        produto.setIdUsuario(getIdUsuarioLogado());
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setCategoria(ctg);
        produto.setPromocao(promo);

    }

    public StorageReference getFileRef(String fileExtensionsUri) {
        fileRef = getFirebaseReference().child(System.currentTimeMillis() + "." + fileExtensionsUri);
        return fileRef;
    }

    public void setUrl(Produto produto, String uri) {
        produto.setUrlImage(uri);
    }

    public void salvarProduto(Produto produto) {
        produtoRepository.salvar(produto);
    }

    public StorageReference getFirebaseReference() {
        return reference;
    }

    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

}
