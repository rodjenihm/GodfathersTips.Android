package com.rodjenihm.godfatherstips;

import android.app.AlertDialog;
import android.content.Context;

public abstract class Utilities {
    public static void showAlertDialog(Context context, String title, String message, Action action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    if (action != null) {
                        action.invoke();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}