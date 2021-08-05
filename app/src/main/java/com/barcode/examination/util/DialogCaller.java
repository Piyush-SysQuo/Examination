package com.barcode.examination.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.barcode.examination.R;

public class DialogCaller {

    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener onClickListener) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok",onClickListener);
        if(message.equals(context.getString(R.string.exit))) {
            dialog.setNegativeButton("Cancel", null);
        }
        dialog.show();
    }

}
