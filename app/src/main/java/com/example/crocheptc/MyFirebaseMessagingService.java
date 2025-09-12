package com.example.crocheptc;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Aqui você trata a mensagem recebida
        Log.d(TAG, "Mensagem recebida de: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notificação: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Mensagem: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Novo token gerado: " + token);
        // Aqui você envia o token para o seu servidor, se precisar
    }
}
