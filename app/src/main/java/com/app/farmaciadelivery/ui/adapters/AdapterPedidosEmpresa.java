package com.app.farmaciadelivery.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.farmaciadelivery.utils.CalcPedido;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.models.ItemPedido;

import java.text.NumberFormat;
import java.util.List;

public class AdapterPedidosEmpresa extends RecyclerView.Adapter<AdapterPedidosEmpresa.MyViewHolder> {

    private List<Pedido> pedidos;

    public AdapterPedidosEmpresa(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Pedido pedido = pedidos.get(i);
        holder.nome.setText(pedido.getNome());
        holder.endereco.setText("• Endereço Do Cliente: " + pedido.getEndereco());
        holder.observacao.setText("+ Observação Do Pedido: " + pedido.getObservacao());
        holder.numero.setText("• Número Do Cliente: " + pedido.getObservacaoNumero());
        holder.hora.setText("Horário Do Pedido: " + pedido.getHora());
        holder.status.setText(pedido.getStatus());

        List<ItemPedido> itens;
        itens = pedido.getItens();

        CalcPedido calc = new CalcPedido();
        calc.calcPedido(itens);

        NumberFormat nf = NumberFormat.getCurrencyInstance();

        holder.itens.setText(calc.getDescricaoItens());
        holder.total.setText("Total: " + nf.format(calc.getTotal()) + " (Taxa de entrega incluída)");
        holder.pgto.setText("• Método de Pagamento: " + pedido.getMetodoPagamento());

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView endereco;
        TextView pgto;
        TextView observacao;
        TextView itens;
        TextView numero;
        TextView hora;
        TextView total;
        TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.text_status);
            hora = itemView.findViewById(R.id.hora_pedido);
            nome = itemView.findViewById(R.id.textPedidoNome);
            endereco = itemView.findViewById(R.id.textPedidoEndereco);
            pgto = itemView.findViewById(R.id.textPedidoPgto);
            observacao = itemView.findViewById(R.id.textPedidoObs);
            itens = itemView.findViewById(R.id.textPedidoItens);
            numero = itemView.findViewById(R.id.textPedidoNumero);
            total = itemView.findViewById(R.id.textPedidoTotal);

        }
    }

}
