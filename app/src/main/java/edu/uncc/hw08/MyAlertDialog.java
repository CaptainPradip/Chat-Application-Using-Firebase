package edu.uncc.hw08;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/*
 * Homework 08
 * MyAlertDialog.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class MyAlertDialog {
    public static void show(Context context, String title, String message) {
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }
}
