package com.app.farmaciadelivery.controllers;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.repository.EmpresaRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InfoEmpresaController {

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private String idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    private EmpresaRepository empresaRepository = new EmpresaRepository();

    public void setEmpresa(Empresa empresa, String nome, String horarioFuncionamento, String tempo,
                           String telefone, String endereco, String taxa) {

        empresa.setIdUsuario(idUsuarioLogado);
        empresa.setNome(nome);
        empresa.setHorarioDeFuncionamento(horarioFuncionamento);
        empresa.setTempo(tempo);
        empresa.setTelefone(telefone);
        empresa.setEndereco(endereco);
        empresa.setTaxaDeEntrega(Double.parseDouble(taxa));
        empresaRepository.salvar(empresa);

    }

    public interface InfoEmpresaCallback {
        void onRecuperarInfoEmpresa(String nome, String horarioFuncionamento, String tempo,
                                    String telefone, String endereco, String taxa);
    }

    public void recuperarDadosEmpresa(InfoEmpresaCallback callback) {

        DatabaseReference empresaRef = firebaseRef.child("empresa").child(idUsuarioLogado);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    Empresa empresa = dataSnapshot.getValue(Empresa.class);

                    callback.onRecuperarInfoEmpresa(empresa.getNome(), empresa.getHorarioDeFuncionamento(),
                            empresa.getTempo(), empresa.getTelefone(), empresa.getEndereco(),
                            String.valueOf(empresa.getTaxaDeEntrega()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
