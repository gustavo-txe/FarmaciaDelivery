package com.app.farmaciadelivery.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.app.farmaciadelivery.R;

public class NotificationHelper {

    /*public static void exibirNotificacao(Context context, String titulo, String mensagem) {
        String canalId = "canal_pedidos";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8+ requer canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    canalId,
                    "Notificações de Pedidos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Canal usado para novas notificações de pedidos.");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(canal);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(R.drawable.icon_notification) // ícone pequeno obrigatório
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }*/
}