package com.app.farmaciadelivery.repository;

import static com.app.farmaciadelivery.helpers.UsuarioFirebase.getIdUsuario;
import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class EmpresaRepository {

    public void salvar(Empresa empresa) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresa").child(getIdUsuario());
        empresaRef.setValue(empresa);
    }

}
