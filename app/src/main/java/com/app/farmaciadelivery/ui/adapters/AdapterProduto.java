package com.app.farmaciadelivery.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.R;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

    private List<Produto> produtos;
    private Context context;

    public void setFilteredList(List<Produto> filteredList) {
        this.produtos = filteredList;
        notifyDataSetChanged();
    }

    public AdapterProduto(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Produto produto = produtos.get(i);
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());

        NumberFormat nf = NumberFormat.getCurrencyInstance();

        //Verifica se o produto está em promoção
        if (Objects.equals(produto.getPromocao(), "sim")) {
            holder.valorAnterior.setText("De " + nf.format(produto.getPrecoAnterior()) + " Por:");
        } else if (Objects.equals(produto.getPromocao(), "nao")) {
            holder.valorAnterior.setText(" ");
        }

        holder.valor.setText(nf.format(produto.getPreco()));

        Glide.with(context).load(produtos.get(i).getUrlImage()).into(holder.imageRetrieve);

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView descricao;
        TextView valor;
        TextView valorAnterior;
        ImageView imageRetrieve;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeRefeicao);
            descricao = itemView.findViewById(R.id.textQtdCarrinho);
            valor = itemView.findViewById(R.id.textPreco);
            valorAnterior = itemView.findViewById(R.id.textPrecoAnteriorProduto);
            imageRetrieve = itemView.findViewById(R.id.imageModelRetrieve);

        }
    }
}
