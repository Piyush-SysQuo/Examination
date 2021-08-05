package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.database.db.CandidatesDBClient;
import com.barcode.examination.model.GetCandidateDataResponse.CandidateData;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.util.DialogCaller;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.SuccessPopupDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.GetCandidateDataViewModel;
import com.barcode.examination.viewmodel.LogoutViewModel;
import com.barcode.examination.viewmodel.ScanWithFaceViewModel;
import com.barcode.examination.viewmodel.UpdateOccurrenceViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OptionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText rollNumberEditText;
    private ProgressDialog appDialog;
    private LogoutViewModel viewModel;
    private LoginAPIResponse resp;
    private String token;
    private String callType="quickscan";
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_input);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton logout = findViewById(R.id.btn_logout);
        LinearLayout scan = findViewById(R.id.btn_scanner);
        LinearLayout facify = findViewById(R.id.btn_facify);
        LinearLayout quickScan = findViewById(R.id.btn_quickscan);
        Button submitButton = findViewById(R.id.submitButton);
        Button moreButton = findViewById(R.id.moreButton);
        rollNumberEditText = findViewById(R.id.rollNumberEditText);
        TextView btn_scan_title = findViewById(R.id.btn_scan_title);
        TextView btn_facify_title = findViewById(R.id.btn_facify_title);
        TextView btn_quickscan_title = findViewById(R.id.btn_quickscan_title);
        TextView or = findViewById(R.id.or);
        logout.setOnClickListener(this);
        scan.setOnClickListener(this);
        facify.setOnClickListener(this);
        quickScan.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);


        // set custom font
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        moreButton.setTypeface(typeface_bold);
        submitButton.setTypeface(typeface_bold);
        btn_scan_title.setTypeface(typeface_bold);
        btn_facify_title.setTypeface(typeface_bold);
        btn_quickscan_title.setTypeface(typeface_bold);
        or.setTypeface(typeface_reg);
        rollNumberEditText.setTypeface(typeface_reg);


        appDialog = new ProgressDialog(OptionActivity.this);

        // get Token
        resp = (LoginAPIResponse) Util.getInstance(this).pickGsonObject(
                Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginAPIResponse>() {
                });

        if (resp != null){
           token = resp.getData().getToken();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
           case  R.id.btn_logout :
               showDialog(getString(R.string.exit));
               break;
            case R.id.btn_scanner :
                checkRunTimePermission();
                break;
            case R.id.submitButton :
               String rollNo =  rollNumberEditText.getText().toString().trim();
               inputRollNumber(rollNo);
                break;
            case R.id.moreButton :
                startActivity(new Intent(this,MoreOptions.class));
                break;
            case R.id.btn_quickscan:
                callType="quickscan";
                checkRunTimePermissionForCamera();
                break;
            case R.id.btn_facify:
                callType="facify";
                checkRunTimePermissionForCamera();
                break;

        }
    }

    // show dialog
    private void showDialog(String msg) {

        DialogCaller.showDialog(OptionActivity.this, getString(R.string.logout),msg , (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    // Add your code if it positive button
                    logout();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Add your code if it negative button
                    dialog.dismiss();
                    break;
            }
        });

    }

    private void inputRollNumber(String rollNo) {
        checkBarCode(rollNo);
    }


    // logout from application
    private void logout() {

        callLogoutAPI();
    }

    private void callLogoutAPI() {
        /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){
            if(appDialog != null){
                appDialog.showLoader();
            }
            // call login Api
            viewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
            viewModel.init();
            viewModel.userLogout(token);
            viewModel.getVolumesResponseLiveData().observe(this, logoutResponse -> {
                if(appDialog != null){
                    appDialog.dismiss();
                }
                if(logoutResponse != null) {
                    Boolean status = logoutResponse.getStatus();
//                    if(status) {
//
//                        // Clear Session
//                        Util.logout(this);
//
//                    }else{
//                        Toast.makeText(OptionActivity.this, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                    // Clear Session
                    Util.logout(this);
                    // Go to login page
                    Util.getInstance(OptionActivity.this).setString(Constants.DATA_DOWNLOADED,"false");
                    finish();
                    startActivity(new Intent(this, LogInActivity.class));
                    Toast.makeText(OptionActivity.this, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OptionActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    //* check bar code  matched or not*//*
    protected  void checkBarCode( String rollNumber){

        if(rollNumber.isEmpty()){
            Toast.makeText(this, "Please input valid roll number", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);

           if(isConnected){
               if (appDialog != null) {
                   appDialog.showLoader();
               }

               // call barcode Api
               GetCandidateDataViewModel viewModel = ViewModelProviders.of(this).get(GetCandidateDataViewModel.class);
               viewModel.init();
               viewModel.getCandidateData(token,rollNumber);
               viewModel.getVolumesResponseLiveData().observe(this, getCandidateDataAPIResponse -> {
                   if (appDialog != null) {
                       appDialog.dismiss();
                   }
                   if(getCandidateDataAPIResponse != null){
                       if(getCandidateDataAPIResponse.getStatus()){
                           CandidateData candidateData = getCandidateDataAPIResponse.getData().getCandidateData();

                           if(candidateData != null){

//                               Intent intent = new Intent(OptionActivity.this,DetailsActivity.class);
                               Intent intent = new Intent(OptionActivity.this,CandidateStatusActivity.class);
                               //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
                               intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(candidateData));
                               startActivity(intent);

                           }else{

                               Toast.makeText(OptionActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }else {
                           Toast.makeText(OptionActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                   }
               });

           }
           else {

               new Thread(() -> {

                   CandidatesList list = CandidatesDBClient.getInstance(OptionActivity.this).getAppDatabase().candidatesDAO()
                           .getSingleStudentData(rollNumber);

                   if (list != null) {
//                       DataBaseClient.getInstance(OptionActivity.this).getAppDatabase().examinationDao()
//                               .updateOcc(list.getRollNo(), list.getTimes() + 1, list.getCount() + 1);

//                       Intent intent = new Intent(OptionActivity.this, DetailsActivity.class);
                       Intent intent = new Intent(OptionActivity.this,CandidateStatusActivity.class);
                       intent.putExtra(Constants.KEY_FROM, Constants.VAL_FROM_DB);
                       intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(list));
                       startActivity(intent);

                   } else {
                       runOnUiThread(() -> {
                           Toast.makeText(this, "No candidate found for given roll number.", Toast.LENGTH_SHORT).show();
                       });
                   }

               }).start();

           }
    }

    // check camera  run time permission
    protected void checkRunTimePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(OptionActivity.this,ScannerActivity.class);
                        intent.putExtra(Constants.USE_CASE,Constants.REGISTER_CAND);
                        startActivity(intent);
                       // OptionActivity.this.finish();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /// check run time permission
    private void checkRunTimePermissionForCamera() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(OptionActivity.this);
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

    private void selectImage(OptionActivity optionActivity) {
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
                    callFaceScanAPI(file, callType);
                }
            }
        }
    }
    private void callFaceScanAPI(File file, String callType) {


        if(file == null){
            Toast.makeText(this, "Please click an image before submitting.", Toast.LENGTH_SHORT).show();
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

            ScanWithFaceViewModel viewModel = ViewModelProviders.of(this).get(ScanWithFaceViewModel.class);
            viewModel.init();
            viewModel.scanFaceForDataApi(token,body);
            viewModel.getVolumesResponseLiveData().observe(this, scanWithFaceAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(scanWithFaceAPIResponse != null){
                    if (callType.equals("quickscan")){
                        if (scanWithFaceAPIResponse.getStatus()){
                            callUpdateOccurranceAPI(token,scanWithFaceAPIResponse.getData().getCandidateData().getRollNumber());
//                            showAlertDialog("Face Verification Status","Candidate verified: "+scanWithFaceAPIResponse.getData().getCandidateData().getRollNumber());
                            openSuccessDialog("Candidate with roll no. "+scanWithFaceAPIResponse.getData().getCandidateData().getRollNumber() + " verified successfully.",1);
                        }else {
//                            showAlertDialog("Face Verification Status", "Candidate not verified.");
                            openSuccessDialog("Candidate cannot be verified.",0);
                        }
                    }else{
                        if (scanWithFaceAPIResponse.getData().getCandidateData()!=null && !scanWithFaceAPIResponse.getData().getCandidateData().getRollNumber().equals("")){
                            Intent intent = new Intent(OptionActivity.this,FacifyResultActivity.class);
                            intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(scanWithFaceAPIResponse.getData().getCandidateData()));
                            intent.putExtra(Constants.KEY_FACE_STATUS,scanWithFaceAPIResponse.getData().getFaceStatus());
                            intent.putExtra(Constants.KEY_CONFIDENCE,scanWithFaceAPIResponse.getData().getConfidence());
                            startActivity(intent);
                        }else{
//                            showAlertDialog("Error", "No candidate found for given face image.");
                            openSuccessDialog("No candidate found for given face image.",0);
                        }
                    }
                }else{
                    Toast.makeText(this,R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void callUpdateOccurranceAPI(String token, String rollNumber) {
        /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }

            new Thread(() -> {
                // Update Occurrance to DB
                int occ =  CandidatesDBClient.getInstance(OptionActivity.this).getAppDatabase().candidatesDAO()
                        .getOccurrenceForStudent(rollNumber);

                int occurrance = occ+1;

                CandidatesDBClient.getInstance(OptionActivity.this).getAppDatabase().candidatesDAO()
                        .updateOccurranceForStudent(rollNumber, occurrance);

            }).start();

            // call update occurrence Api
            UpdateOccurrenceViewModel viewModel = ViewModelProviders.of(this).get(UpdateOccurrenceViewModel.class);
            viewModel.init();
            viewModel.updateOccurrence(token, rollNumber);
            viewModel.getVolumesResponseLiveData().observe(this, updateOccurrenceAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(updateOccurrenceAPIResponse != null) {
//                    Boolean status = updateOccurrenceAPIResponse.getStatus();
                    Toast.makeText(OptionActivity.this, updateOccurrenceAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OptionActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            new Thread(() -> {
                CandidatesList list =  CandidatesDBClient.getInstance(OptionActivity.this).getAppDatabase().candidatesDAO()
                        .getSingleStudentData(rollNumber);

                int occ =  list.getOccurrance();

                int occurrance = occ+1;

                CandidatesDBClient.getInstance(OptionActivity.this).getAppDatabase().candidatesDAO()
                        .updateOccurranceForStudent(rollNumber, occurrance);

            }).start();
            Toast.makeText(this, "Occurrence Updated Successfully. Candidate attendance marked!", Toast.LENGTH_SHORT).show();
        }
    }

    void showAlertDialog(final String title, final String message){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AlertDialog alertDialog = new AlertDialog.Builder(OptionActivity.this).create();
//                alertDialog.setTitle(title);
//                alertDialog.setMessage(message);
//                alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        // Do nothing
//                    }
//                });
//                alertDialog.show();
//
//            }});

        AlertDialog alertDialog = new AlertDialog.Builder(OptionActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Do nothing
            }
        });
        alertDialog.show();

//        // Hide after some seconds
//        final Handler handler  = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (alertDialog.isShowing()) {
//                    alertDialog.dismiss();
//                }
//            }
//        };
//
//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                handler.removeCallbacks(runnable);
//            }
//        });
//
//        handler.postDelayed(runnable, 1000);

    }

    // success dialog
    private void openSuccessDialog(String msg, int popupType){
        SuccessPopupDialog mAlert = new SuccessPopupDialog(this,popupType);
        mAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAlert.setMessage(msg);
        mAlert.setOkButton(view -> {
            mAlert.dismiss();
        });
        mAlert.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAlert.isShowing()){
                    mAlert.dismiss();
                }
            }
        }, 1000); //change 5000 with a specific time you want
    }
}