package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.BypassAuthViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BypassActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn, proofImage;
    private TextView titleTV, proofKey, authKey, authTV;
    private TextInputEditText reasonET;
    private Button submitBtn;
    private ProgressDialog appDialog;

    private LoginAPIResponse resp;
    private String token,authType,rollNumber;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bypass);
        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        appDialog = new ProgressDialog(BypassActivity.this);
        // find id's
        backBtn = findViewById(R.id.btnBack);
        proofImage = findViewById(R.id.proofImage);

        titleTV = findViewById(R.id.titleTV);
        authKey = findViewById(R.id.authTypeKey);
        authTV = findViewById(R.id.authTypeTV);
        proofKey = findViewById(R.id.proofKey);
        reasonET = findViewById(R.id.bypassReasonET);

        submitBtn = findViewById(R.id.submitButton);

        // set custom font
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        titleTV.setTypeface(typeface_bold);
        authKey.setTypeface(typeface_bold);
        authTV.setTypeface(typeface_reg);
        proofKey.setTypeface(typeface_bold);
        submitBtn.setTypeface(typeface_bold);

        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });
        if (resp != null){
            token = resp.getData().getToken();
        }

        // get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            authType = (String) intent.getExtras().get(Constants.AUTH_TYPE);
            rollNumber = (String) intent.getExtras().get(Constants.KEY_ROLL_NUMBER);
            switch (authType){
                case "face-register":
                    authTV.setText("Face Registration");
                    break;
                case "face-verify":
                    authTV.setText("Face Verification");
                    break;
                case "iris-register":
                    authTV.setText("Iris Registration");
                    break;
                case "iris-verify":
                    authTV.setText("Iris Verification");
                    break;
                default:
                    authTV.setText("");
            }
        }

        // set onclick listeners
        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        proofImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack :
                finish();
                break;
            case R.id.submitButton :
                callBypassAPI(authType);
                break;
            case R.id.proofImage:
                checkRunTimePermission();
                break;
        }
    }

    private void callBypassAPI(String authType) {
        String reason = reasonET.getText().toString().trim();

        if(reason.matches("")){
            Toast.makeText(this, "Please input a valid reason for bypassing authentication step.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(file == null){
            Toast.makeText(this, "Please click a proof image before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }
            // convert field into Request body and image convert into multipart
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), fileBody);
            RequestBody rollNum =  RequestBody.create(MediaType.parse("text/plain"), rollNumber);
            RequestBody auth =  RequestBody.create(MediaType.parse("text/plain"),authType);
            RequestBody comments =  RequestBody.create(MediaType.parse("text/plain"),reason);
            BypassAuthViewModel viewModel = ViewModelProviders.of(this).get(BypassAuthViewModel.class);
            viewModel.init();
            viewModel.bypassAuthApi(token,rollNum,auth,comments,body);
            viewModel.getVolumesResponseLiveData().observe(this, bypassAuthAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(bypassAuthAPIResponse != null){
                    if (bypassAuthAPIResponse.getStatus()){
                        finish();
                    }
                    Toast.makeText(this, bypassAuthAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                Toast.makeText(this,R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // check run time permission
    private void checkRunTimePermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(BypassActivity.this);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void selectImage(BypassActivity bypassActivity) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap clickedImage = (Bitmap) data.getExtras().get("data");
                    file = Utility.getFile(this, clickedImage);
                    openImageDialog(clickedImage);
                }
            }
        }
    }

    // Method to show clicked/selected image
    private void openImageDialog(Bitmap bitmap){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proofImage.setImageBitmap(bitmap);
                proofImage.setPadding(0,0,0,0);
                proofImage.setClickable(false);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                file=null;
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_image_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.imageView);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
                image.setLayoutParams(layoutParams);
                image.setImageBitmap(bitmap);
            }
        });

        dialog.show();
    }


}