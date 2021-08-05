package com.barcode.examination.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.barcode.examination.R;

public class SuccessPopupDialog extends Dialog {
    private String message;
    private int popupType = 1;
    private Context context;
    private View.OnClickListener btYesListener=null;
    public SuccessPopupDialog(Context context,int popupType) {
        super(context);
        this.popupType = popupType;
        this.context = context;
    }
//    public SuccessPopupDialog(Context context, int themeResId) {
//        super(context, themeResId);
//    }
    protected SuccessPopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.success_dialog);
        // set custom font
        Typeface typeface_reg = Typeface.createFromAsset(context.getAssets(), "Metropolis_Regular.ttf");
        TextView tvmessage = (TextView) findViewById(R.id.message);
        ImageView icon = (ImageView) findViewById(R.id.dialogIcon);
        tvmessage.setTypeface(typeface_reg);
        tvmessage.setText(getMessage());

        if (this.popupType==0){
            icon.setImageDrawable(context.getDrawable(R.drawable.ic_failure));
        }

        Button btok = (Button) findViewById(R.id.btok);
        btok.setOnClickListener(btYesListener);
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setOkButton( View.OnClickListener onClickListener) {
        dismiss();
        this.btYesListener = onClickListener;
    }
}