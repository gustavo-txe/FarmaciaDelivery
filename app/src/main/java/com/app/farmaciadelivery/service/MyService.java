package com.app.farmaciadelivery.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.app.farmaciadelivery.ui.empresa.pedidos.PedidosEmpresaActivity;
import com.app.farmaciadelivery.R;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent pedidoIntent = new Intent(this, PedidosEmpresaActivity.class);

        pedidoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getApplicationContext().startActivity(pedidoIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                pedidoIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel1");
        builder.setContentTitle("Nenhum Pedido pendente");
        builder.setContentText("Aguardando pedidos...");
        builder.setSmallIcon(R.mipmap.ic_foodtest);
        builder.setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("Channel1", "Serviço em primeiro plano", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.notify(1, builder.build());

        }

        startForeground(1, builder.build());

        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();

    }

}
