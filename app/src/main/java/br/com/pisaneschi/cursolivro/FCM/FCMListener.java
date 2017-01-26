package br.com.pisaneschi.cursolivro.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.pisaneschi.cursolivro.R;
import br.com.pisaneschi.cursolivro.activity.CarrinhoActivity;

/**
 * Created by android6587 on 26/01/17.
 */

public class FCMListener extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String message = data.get("message");

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 123,
                    new Intent(getApplicationContext(), CarrinhoActivity.class),
                    PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.drawable.casadocodigo)
                .setContentTitle(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        Context context = getBaseContext();
        NotificationManager systemService = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        systemService.notify(123, notification);
    }
}
