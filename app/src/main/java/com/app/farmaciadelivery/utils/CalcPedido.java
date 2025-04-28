package com.app.farmaciadelivery.utils;

import com.app.farmaciadelivery.models.ItemPedido;

import java.text.NumberFormat;
import java.util.List;

public class CalcPedido {

    private String descricaoItens = "";
    private Double total = 0.0;

    //calcula o valor total do pedido
    public void calcPedido(List<ItemPedido> itens) {

        int numeroItem = 1;

        ItemPedido itemPedido = itens.get(0);
        total = itemPedido.getTaxa();

        for (ItemPedido ItemPedido : itens) {

            int qtde = ItemPedido.getQuantidade();
            Double preco = ItemPedido.getPreco();
            total += (qtde * preco);

            NumberFormat nf = NumberFormat.getCurrencyInstance();

            String nome = ItemPedido.getNomeProduto();
            descricaoItens += numeroItem + ") " + nome + " / (" + qtde + " x " + nf.format(preco) + ") \n";
            numeroItem++;

        }
    }

    public String getDescricaoItens() {
        return descricaoItens.toString();
    }

    public double getTotal() {
        return total;
    }

}
