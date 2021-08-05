package com.barcode.examination.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;
import com.barcode.examination.IriTechUtils.DemoConfig;
import com.barcode.examination.IriTechUtils.IddkCaptureInfo;
import com.barcode.examination.IriTechUtils.MediaData;
import com.barcode.examination.IriTechUtils.PreferencesActivity;
import com.barcode.examination.R;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.LoginViewModel;
import com.iritech.common.object.ResultCode;
import com.iritech.iddk.android.HIRICAMM;
import com.iritech.iddk.android.Iddk2000Apis;
import com.iritech.iddk.android.IddkCaptureStatus;
import com.iritech.iddk.android.IddkCommStd;
import com.iritech.iddk.android.IddkConfig;
import com.iritech.iddk.android.IddkResult;
import com.iritech.iricore.IriCore;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("HandlerLeak")
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {


    private Button login;
    private TextInputEditText centerId, userId, password;
    private LoginViewModel viewModel;
    private ProgressDialog appDialog;

    private boolean isInitLicenseByInternet = true;
    private static final String OUT_DIR = "/iritech/output/";
    public static final String DEF_OUTPUT_DIRECTORY_PATH = Environment.getExternalStorageDirectory().getPath() + OUT_DIR;

    private static Iddk2000Apis mApis = null;
    private HIRICAMM mDeviceHandle = null;
    private IddkCaptureStatus mCurrentStatus = null;
    private IddkResult mCaptureResult = null;
    private MediaData mMediaData = null;
    private IddkCaptureInfo mCaptureInfo = null;
    private DemoConfig mManiaConfig = null;

    private boolean isLicenseActive = false;

    private static final String[] appPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA};

    private static final int PERMISSIONS_REQUEST_CODE = 111;
    private String mLicenseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        // find id's of view
        login = findViewById(R.id.btn_login);
//        centerId = findViewById(R.id.edit_center_id);
        userId = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        TextView sign_title = findViewById(R.id.sign_title);
        TextView sign_title1 = findViewById(R.id.sign_title1);

        login.setOnClickListener(this);

        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        login.setTypeface(typeface_reg);
//        centerId.setTypeface(typeface_reg);
        userId.setTypeface(typeface_reg);
        password.setTypeface(typeface_reg);
        sign_title.setTypeface(typeface_bold);
        sign_title1.setTypeface(typeface_bold);

        appDialog = new ProgressDialog(LogInActivity.this);

//        // Initialize the application
//        initApp();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(arePermissionsEnabled(this)){
//                // permissions granted, continue flow normally
//                new InitIriCoreAsyncTask().execute();
//            } else {
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(getString(R.string.app_name) + " requires permissions to run\n" +
//                        "Please accept following permissions.")
//                        .setTitle("Permission required");
//                builder.setCancelable(false);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        requestMultiplePermissions();
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        }else{
//            showDialog("Warning",getString(R.string.device_incompatible_error));
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_login:

              // attempt login
              doLogin();

                break;

        }
    }

    // call login Api
    private void doLogin() {

//       String center_id =  centerId.getText().toString().trim();
       String userName = userId.getText().toString().trim();
       String pwd = password.getText().toString().trim();
        isLicenseActive = true;
//       if (!isLicenseActive){
//           Toast.makeText(this, "Device License is inactive, trying to recheck. If problem persists, contact your administrator.", Toast.LENGTH_SHORT).show();
//           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//               if(arePermissionsEnabled(this)){
//                   // permissions granted, continue flow normally
//                   new InitIriCoreAsyncTask().execute();
//               } else {
//
//                   AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                   builder.setMessage(getString(R.string.app_name) + " requires permissions to run\n" +
//                           "Please accept following permissions.")
//                           .setTitle("Permission required");
//                   builder.setCancelable(false);
//                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                       public void onClick(DialogInterface dialog, int id) {
//
//                           requestMultiplePermissions();
//                       }
//                   });
//
//                   AlertDialog dialog = builder.create();
//                   dialog.show();
//               }
//           }else{
//               showDialog("Warning",getString(R.string.device_incompatible_error));
//           }
//           return;
//       }

       if(userName.isEmpty()){
           Toast.makeText(this, getString(R.string.msg_user), Toast.LENGTH_SHORT).show();
           return;
       }else if(pwd.isEmpty()){
           Toast.makeText(this, getString(R.string.msg_pwd), Toast.LENGTH_SHORT).show();
           return;
       }else {

           // attempt login
            callLoginApi(userName,pwd);
       }
    }

    // call login api
    protected void callLoginApi(@NonNull String userName, @NonNull  String password){

       /* check internet connected or not*/
        boolean isConnected = new Utility().getInternetConnection(this);

        if(isConnected){
            if(appDialog != null){
                appDialog.showLoader();
            }
            // call login Api
            viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
            viewModel.init();
            viewModel.userLogin(userName,password);
            viewModel.getVolumesResponseLiveData().observe(this, loginInResponse -> {
                if(appDialog != null){
                    appDialog.dismiss();
                }
                if(loginInResponse != null) {
                    Boolean status = loginInResponse.getStatus();
                    if(status) {

                        // save data into database
                        Util.getInstance(getApplicationContext()).putGsonObject(
                                Constants.PREFS_LOGIN_RESPONSE, loginInResponse, new TypeToken<LoginAPIResponse>() {
                                });
                        finish();

                        // start new activity
                        startActivity(new Intent(LogInActivity.this,CentreDetailsActivity.class));

                    }else{
                        Toast.makeText(LogInActivity.this, loginInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LogInActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    /*****************************************************************************
     * Initialize the application.
     *****************************************************************************/
    private void initApp(){
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        // Set default output directory
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String outputDirStr = sharedPref.getString("output_dir_pref", null);
        //Toast.makeText(this, "default setting = " + outputDirStr + ", default = " + DEF_OUTPUT_DIRECTORY_PATH, Toast.LENGTH_LONG).show();
        if (outputDirStr == null)
        {
            PreferencesActivity.setOutputDirectory(getApplicationContext(), DEF_OUTPUT_DIRECTORY_PATH);
        }

        // Get an instance of the IDDK library
        mApis = Iddk2000Apis.getInstance(this);

        // Application data initialization
        mDeviceHandle = new HIRICAMM();
        mCurrentStatus = new IddkCaptureStatus();
        mCaptureResult = new IddkResult();
        mManiaConfig = new DemoConfig();
        mCaptureInfo = new IddkCaptureInfo();

        // This is an opt. But we should do it as a hobby
        IddkResult ret = new IddkResult();
        IddkConfig iddkConfig = new IddkConfig();
        iddkConfig.setCommStd(IddkCommStd.IDDK_COMM_USB);
        iddkConfig.setEnableLog(false);
        ret = Iddk2000Apis.setSdkConfig(iddkConfig);
        if (ret.getValue() != IddkResult.IDDK_OK)
        {
            showDialog(getResources().getString(R.string.warning), getResources().getString(R.string.sdkinit_error));
        }
    }

    /*****************************************************************************
     * Show notification dialog.
     *****************************************************************************/
    void showDialog(final String title, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(LogInActivity.this).create();
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
            }
        });

    }

    /*****************************************************************************
     * Permission Check for Device
     *****************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean arePermissionsEnabled(Context context){
        for(String permission : appPermissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : appPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), PERMISSIONS_REQUEST_CODE);
    }

    /***************************************************************
     * Utils
     ***************************************************************/
    private class InitIriCoreAsyncTask extends AsyncTask<Void, Void, ResultCode> {
        public InitIriCoreAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appDialog.showLoader();
        }

        @Override
        protected ResultCode doInBackground(Void... voids) {
            //setLicenseFolder();

            ResultCode result = ResultCode.OK;
            if (isInitLicenseByInternet)
            {
                ///// I. Activate license by internet
                // Notes: requestLicense will check license with server. If license is already existed in local, it will do not check with server.
                // - Set isForceNewRequest = true when want to reset license and check again with server. Require internet connection to check with server.
                // e.g.: in case license expired and want to force load new license again from server.
                // 1. First request license with isForceNewRequest = false
                result = requestLicense(true);
                if (!result.equals(ResultCode.OK)) {
                    return result;
                }

                result = initializeLibrary();

                if (!result.equals(ResultCode.OK)) {
                    // Init IriCore failed
                    // If error happen
                    // 2. request license with isForceNewRequest = true, to reset license and check with server
                    result = requestLicense(true);
                    if (!result.equals(ResultCode.OK)) {
                        // 3. If request license still failed, please check with IriTech.
                        return result;
                    }

                    // Init
                    result = initializeLibrary();

                    if (!result.equals(ResultCode.OK)) {
                        return result;
                    }
                }
            } else {
                ///// II. Init license with license file
                String licenseFilePath = "/storage/emulated/0/license.bin";
                result = intLicense(licenseFilePath);
                if (!result.equals(ResultCode.OK)) {
                    return result;
                }

                // Init
                result = initializeLibrary();

                if (!result.equals(ResultCode.OK)) {
                    return result;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(ResultCode resultCode) {
            if(appDialog!=null) {
                appDialog.dismiss();
            }
            if (resultCode != ResultCode.OK) {
                showDialog("Error", "Initial IriCore failed: " + resultCode.toString());
            }else{
                isLicenseActive = true;
                Util.getInstance(getApplicationContext()).setString(Constants.KEY_LICENSE_ACTIVE,"true");
                showDialog("Information","License Active. Initialization Completed.");
            }
        }
    }

    public ResultCode requestLicense(boolean isForceNewRequest) {

        // TODO: set license code here
        mLicenseCode = Constants.LICENSE_CODE;
        if (mLicenseCode.isEmpty()) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "License code is invalid.";
                    showDialog("Information", msg);
                }
            });
            return ResultCode.LICENSE_LOCKCODE_INVALID;
        }

        // requestLicense will check license with server. If local license existed, it will do not check with server
        // - Set isForceNewRequest = true when want to reset license and check again with server,
        // e.g.: in case license expired and want to force load new license again from server.
        final ResultCode result = IriCore.requestLicense(this, isForceNewRequest, mLicenseCode);

        if (!result.equals(ResultCode.OK)) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "IriCore request license failed, error: " + result.toString()
                            + (result == ResultCode.UNKNOWN_ERROR_ ? "(" + result.value() + ")" : "")
                            + ". Please check if licenseCode is correct.";
                    showDialog("Information", msg);
                }
            });
        }

        return result;
    }

    public ResultCode intLicense(String licenseFilePath) {
        ResultCode result = IriCore.initLicenseFile(licenseFilePath);
        return result;
    }

    public ResultCode initializeLibrary() {
        final ResultCode result = IriCore.initializeLibrary(getApplicationContext());
        if (result == ResultCode.REQUIRE_PERMISSION_READ_PHONE_STATE) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Initialize IriCore failed, error: " + result.toString();
                    showDialog("Information", msg);
                }
            });
        } else if (!result.equals(ResultCode.OK)) {
            LogInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Initialize IriCore failed, error: " + result.toString()
                            + ". Please check if license valid.";
                    showDialog("Information", msg);
                }
            });
        }

        return result;
    }
}