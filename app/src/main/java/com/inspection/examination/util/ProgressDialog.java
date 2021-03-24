package com.inspection.examination.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.inspection.examination.R;

public class ProgressDialog {

    private Activity activity;
    private AlertDialog dialog;

    public ProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void showLoader(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_p_dialog,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
