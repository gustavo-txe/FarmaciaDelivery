package com.app.farmaciadelivery.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.R;

import java.text.NumberFormat;
import java.util.List;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder> {

    private List<Empresa> empresas;

    public AdapterEmpresa(List<Empresa> empresa) {
        this.empresas = empresa;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Empresa empresa = empresas.get(i);
        holder.nomeEmpresa.setText(empresa.getNome());
        holder.categoria.setText(empresa.getHorarioDeFuncionamento());
        holder.tempo.setText(empresa.getTempo());
        holder.endereco.setText(empresa.getEndereco());
        holder.telefone.setText(empresa.getTelefone());

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        holder.taxa.setText(nf.format(empresa.getTaxaDeEntrega()));

    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeEmpresa;
        TextView categoria;
        TextView tempo;
        TextView endereco;
        TextView telefone;
        TextView taxa;

        public MyViewHolder(View itemView) {
            super(itemView);

            nomeEmpresa = itemView.findViewById(R.id.textNomeEmpresa);
            categoria = itemView.findViewById(R.id.textCategoriaEmpresa);
            tempo = itemView.findViewById(R.id.textTempoEmpresa);
            endereco = itemView.findViewById(R.id.textEndereco);
            telefone = itemView.findViewById(R.id.textTelefone);
            taxa = itemView.findViewById(R.id.textTaxaEntrega);

        }
    }
}
