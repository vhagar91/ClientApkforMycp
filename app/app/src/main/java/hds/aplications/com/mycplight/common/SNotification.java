package hds.aplications.com.mycp.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;


public class SNotification {
    private static SNotification SNotification = new SNotification();

    private static int NOTIF_ALERTA_ID = 1;

    public static SNotification getInstance() {
        return SNotification;
    }

    private SNotification() {
    }

    public static int notificate(Context context, Class<?> cls, int smallIcon, Bitmap largeIcon, String title, String text, String info, String ticker){
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context)
                        .setSmallIcon(smallIcon)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentInfo(info)
                        .setTicker(ticker).setAutoCancel(true);

        Intent notIntent = new Intent(context, cls);

        PendingIntent contIntent = PendingIntent.getActivity(context, 0, notIntent, 0);

        noti.setContentIntent(contIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

       // noti.flags |= Notification.FLAG_AUTO_CANCEL;

        int idNotification = NOTIF_ALERTA_ID;
        mNotificationManager.notify(idNotification, noti.build());
        NOTIF_ALERTA_ID++;

        return idNotification;
        //mNotificationManager.cancel(NOTIF_ALERTA_ID);
    }
}
