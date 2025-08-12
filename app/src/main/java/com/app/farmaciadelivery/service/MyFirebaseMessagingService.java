package com.app.farmaciadelivery.service;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {/*

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM_RECEBIDO", "Mensagem recebida: " + remoteMessage.getData());

        if (remoteMessage.getNotification() != null) {
            String titulo = remoteMessage.getNotification().getTitle();
            String corpo = remoteMessage.getNotification().getBody();

            // Exibir notificação
            Log.d("FCM_NOTIF", "Título: " + titulo + ", Corpo: " + corpo);
            NotificationHelper.exibirNotificacao(this, titulo, corpo);
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String idEmpresa = user.getUid();
            FirebaseDatabase.getInstance().getReference()
                    .child("tokens")
                    .child(idEmpresa)
                    .setValue(token);
        }
    }*/


}


