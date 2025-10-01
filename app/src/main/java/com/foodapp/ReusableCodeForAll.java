package com.foodapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ReusableCodeForAll {

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).setTitle(title).setMessage(message).show();
    }

    public static void ShowAlert(ChefVerifyPhone chefVerifyPhone, String error, String message) {
    }

    public static void ShowAlert(ChefRegisteration chefRegisteration, String error, String message) {
    }

    public static void ShowAlert(ChefLogin cheflogin, String verificationFailed, String s) {
    }

    public static void ShowAlert(Chefsendotp chefsendotp, String error, String message) {
    }

    public static void ShowAlert(Delivery_Login deliveryLogin, String verificationFailed, String s) {
    }

    public static void ShowAlert(Delivery_registeration deliveryRegistration, String error, String message) {
    }

    public static void ShowAlert(Delivery_SendOtp deliverySendotp, String error, String message) {
    }

    public static void ShowAlert(Delivery_VerifyPhone deliveryVerifyPhone, String error, String message) {
    }

    public static void ShowAlert(Login login, String verificationFailed, String s) {
    }

    public static void ShowAlert(Registeration registration, String error, String message) {
    }

    public static void ShowAlert(sendotp sendotp, String error, String message) {
    }

    public static void ShowAlert(VerifyPhone verifyPhone, String error, String message) {
    }
}

   