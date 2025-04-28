package com.app.farmaciadelivery.ui.empresa.pedidos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.app.farmaciadelivery.controllers.PedidosEmpresaController;
import com.app.farmaciadelivery.ui.adapters.AdapterPedidosEmpresa;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.service.MyService;
import com.app.farmaciadelivery.ui.listener.RecyclerItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PedidosEmpresaActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidosEmpresa;
    private AdapterPedidosEmpresa adapterPedido;
    private List<Pedido> pedidos = new ArrayList<>();
    private ImageView iconBackPedidos;
    private TextView textViewPedidos;
    private PedidosEmpresaController pedidosEmpresaController = new PedidosEmpresaController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_empresa);
        getSupportActionBar().hide();

        InicializarComponente();
        recuperarPedidos();
        confirmarPedidos();

        Intent intent = new Intent(this, MyService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            stopService(intent);
        }

        if (pedidos.size() == 0) {
            textViewPedidos.setText("Aguardando Pedidos ...");
        }

        iconBackPedidos.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    //Confirmar visualização/envio dos pedidos dos clientes
    private void confirmarPedidos() {
        recyclerPedidosEmpresa.addOnItemTouchListener(new RecyclerItemClickListener(
                        this, recyclerPedidosEmpresa,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Pedido nomePedido = pedidos.get(position);

                                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(PedidosEmpresaActivity.this);
                                alertDialog.setTitle("Confirmar Pedido");
                                alertDialog.setMessage("Deseja confirmar o envio do pedido do cliente: " + nomePedido.getNome() + "?");
                                alertDialog.setCancelable(false);

                                alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {

                                    Pedido pedido = pedidos.get(position);
                                    pedidosEmpresaController.confirmarStatus(pedido);

                                    Toast.makeText(PedidosEmpresaActivity.this, "Pedido Confirmado com Sucesso!", Toast.LENGTH_SHORT).show();
                                    adapterPedido.notifyDataSetChanged();

                                });

                                alertDialog.setNegativeButton("Cancelar", (dialog, which) -> Toast.makeText(PedidosEmpresaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show());
                                androidx.appcompat.app.AlertDialog alert = alertDialog.create();
                                alert.show();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(PedidosEmpresaActivity.this);
                                alertDialog.setTitle("Excluir Histórico");
                                alertDialog.setMessage("Deseja excluir esse Pedido ?");
                                alertDialog.setCancelable(false);

                                alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {

                                    Pedido pedido = pedidos.get(position);
                                    pedidosEmpresaController.statusConfirmadoExcluir(pedido);
                                    pedidosEmpresaController.removerPedidoEmpresa(pedido);

                                    Toast.makeText(PedidosEmpresaActivity.this, "Pedido excluído com Sucesso!", Toast.LENGTH_SHORT).show();
                                    adapterPedido.notifyDataSetChanged();

                                });

                                alertDialog.setNegativeButton("Cancelar", (dialog, which) -> Toast.makeText(PedidosEmpresaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show());
                                androidx.appcompat.app.AlertDialog alert = alertDialog.create();
                                alert.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    //Notificação de pedidos
    private void enviarNotificacao() {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification mNotification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("n", "Delivery Pedidos", NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

            channel.setSound(defaultSoundUri, audioAttributes);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

        }
        Intent intent = new Intent(this, PedidosEmpresaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        int totalDePedidos = 0;
        String title = "Novo Pedido!";

        for (Pedido pedidosPendentes : pedidos) {

            if (pedidosPendentes.getStatus().equals("Pedido Pendente ...")) {
                totalDePedidos++;
            }

        }

        String title2 = "Você tem " + totalDePedidos + " Pedido(s) pendente(s)!";

        if (totalDePedidos == 0) {

            title = "Você tem pedidos ativos!";
            title2 = "Verifique se há pedidos pendentes!";

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(PedidosEmpresaActivity.this, "n")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icon_notification)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentText(title2)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        mNotification = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(PedidosEmpresaActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        managerCompat.notify(1, mNotification);

    }

    private void recuperarPedidos() {

        pedidosEmpresaController.getPedidos().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pedidos.clear();
                if (snapshot.getValue() != null) {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);

                    }

                    adapterPedido.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pedidosEmpresaController.getDatabaseReferencePedidos().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                enviarNotificacao();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    public void InicializarComponente() {

        iconBackPedidos = findViewById(R.id.backIconNovoProduto);
        textViewPedidos = findViewById(R.id.textViewPedidos);

        //RecyclerView
        recyclerPedidosEmpresa = findViewById(R.id.recyclerPedidosCliente);
        recyclerPedidosEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidosEmpresa.setHasFixedSize(true);
        adapterPedido = new AdapterPedidosEmpresa(pedidos);
        recyclerPedidosEmpresa.setAdapter(adapterPedido);

    }

}





