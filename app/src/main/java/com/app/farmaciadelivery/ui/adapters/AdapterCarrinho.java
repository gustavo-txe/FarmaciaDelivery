package com.app.farmaciadelivery.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.R;

import java.text.NumberFormat;
import java.util.List;

public class AdapterCarrinho extends RecyclerView.Adapter<AdapterCarrinho.MyViewHolder> {

    private List<Produto> produtos;
    private Context context;

    public AdapterCarrinho(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_carrinho, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Produto produto = produtos.get(i);
        holder.nome.setText(produto.getNomeProduto());
        holder.quantidade.setText("x " + produto.getQuantidade());

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        holder.valor.setText(nf.format(produto.getPreco()));

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView quantidade;
        TextView valor;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNomeRefeicao);
            quantidade = itemView.findViewById(R.id.textQtdCarrinho);
            valor = itemView.findViewById(R.id.textPreco);

        }
    }


}
