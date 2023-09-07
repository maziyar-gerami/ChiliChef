package com.ict.chilichef.Manage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Maziyar on 6/4/2017.
 */

public class ShowAlertDialog {

    public static void show(final Context context) {

        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(context);
        diaBuilder.setTitle("No Internet Access")
                .setMessage("You are offline")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!HttpManager.checkNetwork(context)){
                            show(context);
                        }
                        else{

                        }

                    }
                })
                .setNegativeButton("Go Offline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        diaBuilder.show();

    }



}
