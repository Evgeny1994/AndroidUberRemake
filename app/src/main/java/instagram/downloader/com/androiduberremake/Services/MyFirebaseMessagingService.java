package instagram.downloader.com.androiduberremake.Services;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import instagram.downloader.com.androiduberremake.Common;
import instagram.downloader.com.androiduberremake.R;
import instagram.downloader.com.androiduberremake.Utils.UserUtils;

/**
 * Created by Евгений on 30.03.2023.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static String CHANNEL_ID = "Cat channel";
    //    private static final int NOTIFICATION = 101;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) ;
        UserUtils.updateToken(this, s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notificationManager = NotificationManagerCompat.from(this);
        if (remoteMessage.getNotification().getBody() != null) {
            String S = remoteMessage.getNotification().getBody();
            String S2 = remoteMessage.getNotification().getTitle();
            Log.i("hhgshghsgh", S);
            Log.i("shgshghsgh", S2);
            sendNotification(remoteMessage);

        // Log.i("Принять данные", remoteMessage.getData().toString());
       // Map<String, String> dataRecv = remoteMessage.getData();
      //  if (dataRecv != null) {
            //    Log.i("Принятые данные", dataRecv.toString());

  //          Common.showNotification(this, new Random().nextInt(),
 //                   dataRecv.get(Common.NOTI_TITLE),
 //                   dataRecv.get(Common.NOTI_CONTENT),
 //                   null
//            );

        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String S1 = notification.getBody();
        System.out.println(S1);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("уведомленіе")
                .setContentText(S1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(false);
            notificationBuilder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}