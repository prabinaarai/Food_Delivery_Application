package com.foodapp.SendNotification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.foodapp.ChefFoodPanel.ChefPreparedOrderView;
import com.foodapp.ChefFoodPanel_BottomNavigation;
import com.foodapp.CustomerFoodPanel.PayableOrders;
import com.foodapp.CustomerFoodPanel_BottomNavigation;
import com.foodapp.Delivery_FoodPanelBottomNavigation;
import com.foodapp.MainActivity;
import com.foodapp.R;

import java.util.Random;

public class ShowNotification {

    public static void ShowNotif(Context context, String title, String message, String page) {
        String CHANNEL_ID = "NOTICE";
        String CHANNEL_NAME = "NOTICE";

        // Create notification channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent acIntent = new Intent(context, MainActivity.class);
        acIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Determine the target activity based on the page parameter
        if (page.trim().equalsIgnoreCase("Order")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Orderpage");
        } else if (page.trim().equalsIgnoreCase("Payment")) {
            acIntent = new Intent(context, PayableOrders.class);
        } else if (page.trim().equalsIgnoreCase("Home")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Homepage");
        } else if (page.trim().equalsIgnoreCase("Confirm")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Confirmpage");
        } else if (page.trim().equalsIgnoreCase("Preparing")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparingpage");
        } else if (page.trim().equalsIgnoreCase("Prepared")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "Preparedpage");
        } else if (page.trim().equalsIgnoreCase("DeliveryOrder")) {
            acIntent = new Intent(context, Delivery_FoodPanelBottomNavigation.class).putExtra("PAGE", "DeliveryOrderpage");
        } else if (page.trim().equalsIgnoreCase("DeliverOrder")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "DeliverOrderpage");
        } else if (page.trim().equalsIgnoreCase("AcceptOrder")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "AcceptOrderpage");
        } else if (page.trim().equalsIgnoreCase("RejectOrder")) {
            acIntent = new Intent(context, ChefPreparedOrderView.class).putExtra("PAGE", "RejectOrderpage");
        } else if (page.trim().equalsIgnoreCase("ThankYou")) {
            acIntent = new Intent(context, CustomerFoodPanel_BottomNavigation.class).putExtra("PAGE", "ThankYoupage");
        } else if (page.trim().equalsIgnoreCase("Delivered")) {
            acIntent = new Intent(context, ChefFoodPanel_BottomNavigation.class).putExtra("PAGE", "Deliveredpage");
        }

        // Create the pending intent for the notification
        pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chef_hat_and_fork)
                .setColor(ContextCompat.getColor(context, R.color.Red))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        int random = new Random().nextInt(9999 - 1) + 1;

        // Check for notification permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, you may want to request it here
            return; // Exit the method without showing the notification
        }

        // Notify the user
        notificationManagerCompat.notify(random, nBuilder.build());
    }
}
