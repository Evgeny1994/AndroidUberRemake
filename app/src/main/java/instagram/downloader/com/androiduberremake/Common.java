package instagram.downloader.com.androiduberremake;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import instagram.downloader.com.androiduberremake.Model.DriverInfoModel;

/**
 * Created by Евгений on 25.02.2023.
 */

public class Common {
    public static final String DRIVER_INFO_REFERENCES = "DriverInfo";
    public static final String DRIVER_LOCATION_REFERENCES="DriversLocation";
    public static final String TOKEN_REFERENCE="Token";
    public static final String NOTI_TITLE="title";
    public static final String NOTI_CONTENT = "body";
    public static DriverInfoModel currentUser;
    public static String buildWelcomeMessage()
    {
        if (Common.currentUser!=null)
        {
            return new StringBuilder("Welcome.....")
                    .append(Common.currentUser.getFirstName())
                    .append(" ")
                    .append(Common.currentUser.getLastName()).toString();
        }
        else
        {
            return " ";
        }
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    public static void showNotification(Context context, int id, String title, String body, Intent intent)
//    {
//        PendingIntent pendingIntent = null;
 //       if (intent != null)
 //       {
 //           pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
 //           String NOTIFICATION_CHANNEL_ID ="edmt_dev_uber_remake";
 //           NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
 //           if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
 //           {
 //               NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Uber Remake", NotificationManager.IMPORTANCE_HIGH);
 //               notificationChannel.setDescription("Uber Remake");
 //               notificationChannel.enableLights(true);
 //               notificationChannel.setLightColor(Color.RED);
 //               notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
 //               notificationChannel.enableLights(true);
 //               notificationManager.createNotificationChannel(notificationChannel);
 //           }
 ////           NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
 //           builder.setContentTitle(title)
//                    .setContentText(body)
//                    .setAutoCancel(false)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setDefaults(Notification.DEFAULT_VIBRATE)
//                    .setSmallIcon(R.drawable.ic_directions_car_black_24dp)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_directions_car_black_24dp));
 //           if (pendingIntent!=null) {
//                builder.setContentIntent(pendingIntent);
//            }
//            Notification notification = builder.build();
//            notificationManager.notify(id, notification);
//            }
//        }
    }
