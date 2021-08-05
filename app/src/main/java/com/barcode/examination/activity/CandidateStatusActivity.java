package com.barcode.examination.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.barcode.examination.IriTechUtils.DemoConfig;
import com.barcode.examination.IriTechUtils.DemoUtility;
import com.barcode.examination.IriTechUtils.IddkCaptureInfo;
import com.barcode.examination.IriTechUtils.MediaData;
import com.barcode.examination.IriTechUtils.PreferencesActivity;
import com.barcode.examination.IriTechUtils.PropertyName;
import com.barcode.examination.IriTechUtils.SettingsUtil;
import com.barcode.examination.R;
import com.barcode.examination.model.GetCandidateDataResponse.CandidateData;
import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.ProgressDialog;
import com.barcode.examination.util.Util;
import com.barcode.examination.util.Utility;
import com.barcode.examination.viewmodel.GetCandidateDataViewModel;
import com.barcode.examination.viewmodel.RegisterFaceViewModel;
import com.barcode.examination.viewmodel.RegisterIrisViewModel;
import com.iritech.driver.UsbNotification;
import com.iritech.iddk.android.HIRICAMM;
import com.iritech.iddk.android.Iddk2000Apis;
import com.iritech.iddk.android.IddkCaptureMode;
import com.iritech.iddk.android.IddkCaptureOperationMode;
import com.iritech.iddk.android.IddkCaptureStatus;
import com.iritech.iddk.android.IddkCommStd;
import com.iritech.iddk.android.IddkConfig;
import com.iritech.iddk.android.IddkDeviceConfig;
import com.iritech.iddk.android.IddkDeviceInfo;
import com.iritech.iddk.android.IddkEyeSubType;
import com.iritech.iddk.android.IddkImage;
import com.iritech.iddk.android.IddkImageFormat;
import com.iritech.iddk.android.IddkImageKind;
import com.iritech.iddk.android.IddkInteger;
import com.iritech.iddk.android.IddkIrisQuality;
import com.iritech.iddk.android.IddkQualityMode;
import com.iritech.iddk.android.IddkResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CandidateStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn, faceImage1, faceImage2, irisImage1, irisImage2;
    private TextView titleTV, nameKey, nameTV, rollKey, rollTV, appKey, appTV, barKey, barTV, faceKey, faceTV, irisKey,irisTV;
    private Button regFace, bypassRegFace, regIris, bypassRegIris, verFace, bypassVerFace, verIris, bypassVerIris, continueBtn;
    private LinearLayout faceStatusLayout, faceImagesLayout, faceRegButtonsLayout;
    private LinearLayout irisStatusLayout, irisImagesLayout, irisRegButtonsLayout;
    private LinearLayout deviceSerialLayout;
    private ProgressDialog appDialog;

    private CandidateData result;
    private CandidatesList dataList;
    private String from;
    private LoginAPIResponse resp;
    private String token,rollNumber;
    private boolean onCreated = false;
    private File faceImageFile1,faceImageFile2,irisImageFile1,irisImageFile2;
    private String imageIndex="faceImage2";

    private UsbNotification mUsbNotification = null;
    private static Iddk2000Apis mApis = null;
    private HIRICAMM mDeviceHandle = null;
    private IddkCaptureStatus mCurrentStatus = null;
    private IddkResult mCaptureResult = null;
    private MediaData mMediaData = null;
    private IddkCaptureInfo mCaptureInfo = null;
    private DemoConfig mManiaConfig = null;
    private static final String OUT_DIR = "/iritech/output/";
    public static final String DEF_OUTPUT_DIRECTORY_PATH = Environment.getExternalStorageDirectory().getPath() + OUT_DIR;

    private boolean isLicenseActive = false;
    private static final int TAB_CAPTURE_INDEX_ID = 0x00;
    private static final int ABOUT_DIALOG_ID = 0x00;
    private static final int IRIS_VERIFY = 1;
    private static final int IRIS_ENROLL = 2;
    private static final int IRIS_ENROLL_MORE = 3;
    private static final int IRIS_IDENTIFY = 4;
    protected static final String ACTION_USB_PERMISSION = "com.inspection.examination.device.USB_PERMISSION";
    private Spinner mListOfDevices = null;
    private TextView mSerialTextView = null;

    private TextView mStatusTextView = null;
    private ImageView mCaptureView = null;
    private ImageView mCaptureViewRight = null;
    private Bitmap mCurrentBitmap = null;

    private static int mCaptureCount = 0;
    private boolean mIspreviewing = false;
    private boolean mIsBadQualityImage = false;
    private boolean mIsRegFirstTime = true;
    private boolean mIsMatchFirstTime = true;
    private int irisRegCurrentAction = 0;
    private int mTotalScore = 0;
    private int mUsableArea = 0;
    private String mCurrentDeviceName = "";
    private String mCurrentOutputDir = "";

    private boolean mIsGalleryLoaded = false;
    private boolean mIsCameraReady = false;
    private boolean mIsJustError = false;
    private boolean mIsCheckDedup = true;

    private int mScreenWidth = 0;
    private static final int IriTech_VID = 8035;

    private static final int PERMISSIONS_REQUEST_CODE = 111;
    private String mUserID;
    private ArrayList<IddkImage> mMonoBestImage;
    private String mLicenseCode=Constants.LICENSE_CODE;

    private int leftEyeScore = 0;
    private int rightEyeScore = 0;

//    private enum eIdentifyResult
//    {
//        IRI_IDENTIFY_DIFFERENT, IRI_IDENTIFY_LOOKLIKE, IRI_IDENTIFY_SAME, IRI_IDENTIFY_DUPLICATED
//    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_status);
        // Hide the status bar.
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        appDialog = new ProgressDialog(CandidateStatusActivity.this);
        // find id's
        backBtn = findViewById(R.id.btnBack);
        faceImage1 = findViewById(R.id.faceImage1);
        faceImage2 = findViewById(R.id.faceImage2);
        irisImage1 = findViewById(R.id.irisImage1);
        irisImage2 = findViewById(R.id.irisImage2);

        titleTV = findViewById(R.id.titleTV);
        nameKey = findViewById(R.id.candidateNameKeyTV);
        nameTV = findViewById(R.id.candidateNameTV);
        rollKey = findViewById(R.id.rollNumberKeyTV);
        rollTV = findViewById(R.id.rollNumberTV);
        appKey = findViewById(R.id.applicationKey);
        appTV = findViewById(R.id.applicationTV);
        barKey = findViewById(R.id.barcodeKey);
        barTV = findViewById(R.id.barcodeTV);
        faceKey = findViewById(R.id.faceKey);
        faceTV = findViewById(R.id.faceTV);
        irisKey = findViewById(R.id.irisKey);
        irisTV = findViewById(R.id.irisTV);

        regFace = findViewById(R.id.registerFaceButton);
        bypassRegFace = findViewById(R.id.bypassFaceButton);
        regIris = findViewById(R.id.registerIrisButton);
        bypassRegIris = findViewById(R.id.bypassIrisButton);
        verFace = findViewById(R.id.verifyFaceButton);
        bypassVerFace = findViewById(R.id.bypassFaceVerButton);
        verIris = findViewById(R.id.verifyIrisButton);
        bypassVerIris = findViewById(R.id.bypassIrisVerButton);
        continueBtn = findViewById(R.id.continueButton);

        faceStatusLayout = findViewById(R.id.faceStatusLayout);
        faceImagesLayout = findViewById(R.id.faceImagesLayout);
        faceRegButtonsLayout = findViewById(R.id.faceButtonsLayout);
        irisStatusLayout = findViewById(R.id.irisStatusLayout);
        irisImagesLayout = findViewById(R.id.irisImagesLayout);
        irisRegButtonsLayout = findViewById(R.id.irisButtonsLayout);
        deviceSerialLayout = findViewById(R.id.deviceSerialLayout);

        // set custom font
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");
        Typeface typeface_reg = Typeface.createFromAsset(getAssets(), "Metropolis_Regular.ttf");
        titleTV.setTypeface(typeface_bold);
        nameKey.setTypeface(typeface_bold);
        nameTV.setTypeface(typeface_reg);
        rollKey.setTypeface(typeface_bold);
        rollTV.setTypeface(typeface_reg);
        appKey.setTypeface(typeface_bold);
        appTV.setTypeface(typeface_reg);
        barKey.setTypeface(typeface_bold);
        barTV.setTypeface(typeface_reg);
        faceKey.setTypeface(typeface_bold);
        faceTV.setTypeface(typeface_reg);
        irisKey.setTypeface(typeface_bold);
        irisTV.setTypeface(typeface_reg);

        regFace.setTypeface(typeface_bold);
        bypassRegFace.setTypeface(typeface_bold);
        regIris.setTypeface(typeface_bold);
        bypassRegIris.setTypeface(typeface_bold);
        verFace.setTypeface(typeface_bold);
        bypassVerFace.setTypeface(typeface_bold);
        verIris.setTypeface(typeface_bold);
        bypassVerIris.setTypeface(typeface_bold);
        continueBtn.setTypeface(typeface_bold);

        // Get status view
        mStatusTextView = (TextView) findViewById(R.id.time_textView);

        // This view is used to get streaming image (right eye if Binocular device)
        mCaptureView = (ImageView) findViewById(R.id.irisImage1);
        mCaptureViewRight = (ImageView) findViewById(R.id.irisImage2);

        // Prepare media data for capturing process
        mMediaData = new MediaData(getApplicationContext());

        // Initialize spinner (list of devices)
        mListOfDevices = (Spinner) findViewById(R.id.list_of_devices_id);

        mSerialTextView = (TextView) findViewById(R.id.tv_serial);

        // Get width of the device
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;


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
            from = (String) intent.getExtras().get(Constants.KEY_FROM);

            if(from.equals(Constants.VAL_FROM_SERVER)) {
                result = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_RESULT));

            }
            else{
                dataList = Parcels.unwrap(intent.getParcelableExtra(Constants.KEY_RESULT));

            }
        }

        // set onclick listeners
        backBtn.setOnClickListener(this);
        regFace.setOnClickListener(this);
        bypassRegFace.setOnClickListener(this);
        regIris.setOnClickListener(this);
        bypassRegIris.setOnClickListener(this);
        verFace.setOnClickListener(this);
        bypassVerFace.setOnClickListener(this);
        verIris.setOnClickListener(this);
        bypassVerIris.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        irisImage1.setOnClickListener(this);
        irisImage2.setOnClickListener(this);
        faceImage2.setOnClickListener(this);

        // set data into view's
        if(result != null) {
            if (from.equals(Constants.VAL_FROM_SERVER)) {
                nameTV.setText(result.getName());
                rollTV.setText(result.getRollNumber());
                rollNumber = result.getRollNumber();
                appTV.setText(result.getApplicationNumber());
                faceTV.setText(result.getFaceStatus());
                // set image url to profile image view
                Glide.with(CandidateStatusActivity.this).load(result.getImageURL())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).centerCrop().into(faceImage1);

                if (result.getFaceStatus().matches("Not Registered")){
                    faceTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Registration Bypassed")){
                    faceTV.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getFaceStatus().matches("Not Verified")){
                    faceTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getFaceStatus().matches("Verification Bypassed")){
                    faceTV.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    faceTV.setTextColor(this.getColor(R.color.green));
                }
                irisTV.setText(result.getIrisStatus());
                if (result.getIrisStatus().matches("Not Registered")){
                    irisTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Registration Bypassed")){
                    irisTV.setTextColor(this.getColor(R.color.light_orange));
                }else if (result.getIrisStatus().matches("Not Verified")){
                    irisTV.setTextColor(this.getColor(R.color.red));
                }else if (result.getIrisStatus().matches("Verification Bypassed")){
                    irisTV.setTextColor(this.getColor(R.color.light_orange));
                }else {
                    irisTV.setTextColor(this.getColor(R.color.green));
                }
            }
        }
        else {
            nameTV.setText(dataList.getName());
            rollTV.setText(dataList.getRollNumber());
            rollNumber = dataList.getRollNumber();
            appTV.setText(dataList.getApplicationNumber());
            faceTV.setText(dataList.getFaceStatus());
            // set image url to profile image view
            byte[] decodedString = Base64.decode(dataList.getImageString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            faceImage1.setImageBitmap(decodedByte);
            if (dataList.getFaceStatus().matches("Not Registered")){
                faceTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Registration Bypassed")){
                faceTV.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getFaceStatus().matches("Not Verified")){
                faceTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getFaceStatus().matches("Verification Bypassed")){
                faceTV.setTextColor(this.getColor(R.color.light_orange));
            }else {
                faceTV.setTextColor(this.getColor(R.color.green));
            }
            irisTV.setText(dataList.getIrisStatus());
            if (dataList.getIrisStatus().matches("Not Registered")){
                irisTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Registration Bypassed")){
                irisTV.setTextColor(this.getColor(R.color.light_orange));
            }else if (dataList.getIrisStatus().matches("Not Verified")){
                irisTV.setTextColor(this.getColor(R.color.red));
            }else if (dataList.getIrisStatus().matches("Verification Bypassed")){
                irisTV.setTextColor(this.getColor(R.color.light_orange));
            }else {
                irisTV.setTextColor(this.getColor(R.color.green));
            }
        }

        // Hiding the unneccessary views on the basis of authentication modules allowed
        if (!resp.getData().getCentreInfo().getFaceAuth()){
            // Face Auth Disabled
            // Hide the face layouts
            faceStatusLayout.setVisibility(View.GONE);
            faceImagesLayout.setVisibility(View.GONE);
            faceRegButtonsLayout.setVisibility(View.GONE);
        }

        if (!faceTV.getText().toString().matches("Not Registered")){
            // Face Registration Completed
            // Hide the face layouts
            faceImagesLayout.setVisibility(View.GONE);
            faceRegButtonsLayout.setVisibility(View.GONE);
        }

//        Piyush
        /**
        *  Uncomment below code for enabling iris auth
        * */
        if (!resp.getData().getCentreInfo().getIrisAuth()){
            // Iris Auth Disabled
            // Hide the iris layouts
            irisStatusLayout.setVisibility(View.GONE);
            irisImagesLayout.setVisibility(View.GONE);
            irisRegButtonsLayout.setVisibility(View.GONE);
            mListOfDevices.setVisibility(View.GONE);
            mStatusTextView.setVisibility(View.GONE);
            deviceSerialLayout.setVisibility(View.GONE);
        }

        if (!irisTV.getText().toString().matches("Not Registered")){
            // Iris Registration Completed
            // Hide the face layouts
            irisImagesLayout.setVisibility(View.GONE);
            irisRegButtonsLayout.setVisibility(View.GONE);
            mListOfDevices.setVisibility(View.GONE);
            mStatusTextView.setVisibility(View.GONE);
            deviceSerialLayout.setVisibility(View.GONE);
        }

        /**
         *  Uncomment above code and comment below code for enabling iris auth
         * */
//        if (true){
//            // Iris Auth Disabled
//            // Hide the iris layouts
//            irisStatusLayout.setVisibility(View.GONE);
//            irisImagesLayout.setVisibility(View.GONE);
//            irisRegButtonsLayout.setVisibility(View.GONE);
//            mListOfDevices.setVisibility(View.GONE);
//            mStatusTextView.setVisibility(View.GONE);
//            deviceSerialLayout.setVisibility(View.GONE);
//
//        }
//
//        if (true){
//            // Iris Registration Completed
//            // Hide the face layouts
//            irisImagesLayout.setVisibility(View.GONE);
//            irisRegButtonsLayout.setVisibility(View.GONE);
//            mListOfDevices.setVisibility(View.GONE);
//            mStatusTextView.setVisibility(View.GONE);
//            deviceSerialLayout.setVisibility(View.GONE);
//        }

        // Set onCreated = true
        initApp();

        onCreated = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callGetCandidateDataAPI() {
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
                            result.setFaceStatus(candidateData.getFaceStatus());
                            result.setIrisStatus(candidateData.getIrisStatus());
                            faceTV.setText(candidateData.getFaceStatus());
                            if (candidateData.getFaceStatus().matches("Not Registered")){
                                faceTV.setTextColor(this.getColor(R.color.red));
                            }else if (candidateData.getFaceStatus().matches("Registration Bypassed")){
                                faceTV.setTextColor(this.getColor(R.color.light_orange));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceRegButtonsLayout.setVisibility(View.GONE);
                            }else if (candidateData.getFaceStatus().matches("Not Verified")){
                                faceTV.setTextColor(this.getColor(R.color.red));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceRegButtonsLayout.setVisibility(View.GONE);
                            }else if (candidateData.getFaceStatus().matches("Verification Bypassed")){
                                faceTV.setTextColor(this.getColor(R.color.light_orange));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceRegButtonsLayout.setVisibility(View.GONE);
                            }else {
                                faceTV.setTextColor(this.getColor(R.color.green));
                                faceImagesLayout.setVisibility(View.GONE);
                                faceRegButtonsLayout.setVisibility(View.GONE);
                            }
                            irisTV.setText(candidateData.getIrisStatus());
                            if (candidateData.getIrisStatus().matches("Not Registered")){
                                irisTV.setTextColor(this.getColor(R.color.red));
                            }else if (candidateData.getIrisStatus().matches("Registration Bypassed")){
                                irisTV.setTextColor(this.getColor(R.color.light_orange));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisRegButtonsLayout.setVisibility(View.GONE);
                                mListOfDevices.setVisibility(View.GONE);
                                mStatusTextView.setVisibility(View.GONE);
                                deviceSerialLayout.setVisibility(View.GONE);
                            }else if (candidateData.getIrisStatus().matches("Not Verified")){
                                irisTV.setTextColor(this.getColor(R.color.red));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisRegButtonsLayout.setVisibility(View.GONE);
                                mListOfDevices.setVisibility(View.GONE);
                                mStatusTextView.setVisibility(View.GONE);
                                deviceSerialLayout.setVisibility(View.GONE);
                            }else if (candidateData.getIrisStatus().matches("Verification Bypassed")){
                                irisTV.setTextColor(this.getColor(R.color.light_orange));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisRegButtonsLayout.setVisibility(View.GONE);
                                mListOfDevices.setVisibility(View.GONE);
                                mStatusTextView.setVisibility(View.GONE);
                                deviceSerialLayout.setVisibility(View.GONE);
                            }else {
                                irisTV.setTextColor(this.getColor(R.color.green));
                                irisImagesLayout.setVisibility(View.GONE);
                                irisRegButtonsLayout.setVisibility(View.GONE);
                                mListOfDevices.setVisibility(View.GONE);
                                mStatusTextView.setVisibility(View.GONE);
                                deviceSerialLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    Toast.makeText(CandidateStatusActivity.this, getCandidateDataAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnBack :
                finish();
                break;
            case R.id.registerFaceButton :
                callRegisterFaceAPI();
                break;
            case R.id.registerIrisButton :
                callRegisterIrisAPI();
                break;
            case R.id.bypassFaceButton :
                goToBypass("face-register");
                break;
            case R.id.bypassIrisButton :
                goToBypass("iris-register");
                break;
            case R.id.verifyFaceButton :
                callVerifyFaceAPI();
                break;
            case R.id.verifyIrisButton :
                callVerifyIrisAPI();
                break;
            case R.id.bypassFaceVerButton :
                goToBypass("face-verify");
                break;
            case R.id.bypassIrisVerButton :
                goToBypass("iris-verify");
                break;
            case R.id.continueButton :
                goToDetails();
                break;
            case R.id.irisImage1 :
                checkIrisScannerPerms(mCaptureView,"left-eye");
                break;
            case R.id.irisImage2 :
                checkIrisScannerPerms(mCaptureViewRight,"right-eye");
                break;
            case R.id.faceImage2 :
                imageIndex = "faceImage2";
                checkRunTimePermission("faceImage2");
                break;
        }
    }

    // check run time permission
    private void checkRunTimePermission(String imageIdx) {
        imageIndex = imageIdx;
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                selectImage(CandidateStatusActivity.this);
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

    private void selectImage(CandidateStatusActivity candidateStatusActivity) {
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
                    if (imageIndex.equals("faceImage1")){
                        faceImageFile1 = Utility.getFileWithName(this, clickedImage,"FaceImage1.jpeg");
                    }else{
                        faceImageFile2 = Utility.getFileWithName(this, clickedImage,"FaceImage2.jpeg");
                    }

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
                if (imageIndex.equals("faceImage1")){
                    faceImage1.setImageBitmap(bitmap);
                    faceImage1.setPadding(0,0,0,0);
                    faceImage1.setClickable(false);
                    imageIndex = "faceImage2";
                }else{
                    faceImage2.setImageBitmap(bitmap);
                    faceImage2.setPadding(0,0,0,0);
                    faceImage2.setClickable(false);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageIndex.equals("faceImage1")){
                    faceImageFile1 = null;
                }else{
                    faceImageFile2 = null;
                }
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

    private void checkIrisScannerPerms(ImageView captureView, String side) {
        try{
            if (!Util.getInstance(getApplicationContext()).getString(Constants.KEY_LICENSE_ACTIVE).matches("true")){
                Toast.makeText(this, "License Inactive. Please logout and login again to continue.", Toast.LENGTH_SHORT).show();
            }else {
//        // Clear any images that appear in the previous session
//        FrameLayout layout = (FrameLayout) findViewById(R.id.best_image_layout_id);
//        layout.removeAllViews();
//        layout.invalidate();

                if (!mCurrentDeviceName.equals("") && mListOfDevices != null) {
                    // If we want to start a capturing process on another IriShield device, we have to check
                    // the current selected device name on Spinner

                    /*if (mListOfDevices.getSelectedItem().toString().equals(mCurrentDeviceName))
                    {
                        startCamera(true, captureView, side);
                    }
                    else
                    {*/
                        // User chooses another IriShield to start a capturing process. We must release any resources of the current IriShield device
                        mApis.closeDevice(mDeviceHandle);

                        // Reset any internal states of the application
                        setInitState();

                        // Get device handle and start a capturing process
                        IddkResult ret = mApis.openDevice(mListOfDevices.getSelectedItem().toString(), mDeviceHandle);
                        if (ret.intValue() == IddkResult.IDDK_OK || ret.intValue() == IddkResult.IDDK_DEVICE_ALREADY_OPEN)
                        {
                            updateCurrentStatus(getString(R.string.opendevice_connected));

                            //                // We can enable the start button from now
                            //                View view = findViewById(R.id.start_button_id);
                            //                view.setEnabled(true);
                            //
                            //                view = findViewById(R.id.stop_button_id);
                            //                view.setEnabled(false);

                            mIsJustError = false;
                            mCurrentDeviceName = mListOfDevices.getSelectedItem().toString();

                            startCamera(true, captureView, side);
                        }
                        else
                        {
                            if (ret.getValue() == IddkResult.IDDK_DEVICE_ACCESS_DENIED)
                            {
                                updateCurrentStatus(getString(R.string.opendevice_accessdenied));
                            }
                            else
                            {
                                updateCurrentStatus(getString(R.string.opendevice_failed));
                            }
                        }
//                    }
                }
                else
                {
                    Toast.makeText(this, "No device found. Please connect a device first.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            showDialog("Iris error in "+side, e.getMessage());
        }
    }

    private void goToDetails() {

        if (resp.getData().getCentreInfo().getFaceAuth() && faceTV.getText().toString().matches("Not Registered")){
            Toast.makeText(this, "Please complete face image registration first.", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
        * Uncomment below code to enable iris auth
        * */

        if (resp.getData().getCentreInfo().getIrisAuth() && irisTV.getText().toString().matches("Not Registered")){
            Toast.makeText(this, "Please complete iris image registration first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(CandidateStatusActivity.this,DetailsActivity.class);
        if(result != null) {
            //            Intent intent = new Intent(ScannerActivity.this,CandidateStatusActivity.class);
            intent.putExtra(Constants.KEY_FROM,Constants.VAL_FROM_SERVER);
            intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(result));
        }
        else {
            //            Intent intent = new Intent(ScannerActivity.this,CandidateStatusActivity.class);
            intent.putExtra(Constants.KEY_FROM, Constants.VAL_FROM_DB);
            intent.putExtra(Constants.KEY_RESULT, Parcels.wrap(dataList));
        }
        startActivity(intent);
        this.finish();
    }

    private void goToBypass(String type) {
        onCreated = false;
        Intent intent = new Intent(CandidateStatusActivity.this,BypassActivity.class);
        intent.putExtra(Constants.AUTH_TYPE, type);
        intent.putExtra(Constants.KEY_ROLL_NUMBER, rollNumber);
        startActivity(intent);
    }

    private void callVerifyIrisAPI() {
        Toast.makeText(this, "Verify Iris API Called", Toast.LENGTH_SHORT).show();
    }

    private void callVerifyFaceAPI() {
        Toast.makeText(this, "Verify Face API Called", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callRegisterIrisAPI() {

        if (leftEyeScore<=50){
            Toast.makeText(this, "Left Iris Image isn't acceptable, please click again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rightEyeScore<=50){
            Toast.makeText(this, "Right Iris Image isn't acceptable, please click again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (irisImageFile1==null){
            Toast.makeText(this, "Left Iris Image File not found. Try clicking again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (irisImageFile2==null){
            Toast.makeText(this, "Right Iris Image File not found. Try clicking again.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }
            // convert field into Request body and image convert into multipart
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), irisImageFile1);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageA", irisImageFile1.getName(), fileBody);
            RequestBody fileBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), irisImageFile2);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("imageB", irisImageFile2.getName(), fileBody2);
            RequestBody rollNum =  RequestBody.create(MediaType.parse("text/plain"), rollNumber);

            RegisterIrisViewModel viewModel = ViewModelProviders.of(this).get(RegisterIrisViewModel.class);
            viewModel.init();
            viewModel.registerIrisApi(token,rollNum,body,body2);
            viewModel.getVolumesResponseLiveData().observe(this, registerIrisAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(registerIrisAPIResponse != null){
                    result.setFaceStatus(registerIrisAPIResponse.getData().getCandidateData().getFaceStatus());
                    result.setIrisStatus(registerIrisAPIResponse.getData().getCandidateData().getIrisStatus());
                    if (registerIrisAPIResponse.getStatus()){
                        irisTV.setText(registerIrisAPIResponse.getData().getCandidateData().getIrisStatus());
                        irisTV.setTextColor(this.getColor(R.color.green));
                        irisImagesLayout.setVisibility(View.GONE);
                        irisRegButtonsLayout.setVisibility(View.GONE);
                        mListOfDevices.setVisibility(View.GONE);
                        mStatusTextView.setVisibility(View.GONE);
                        deviceSerialLayout.setVisibility(View.GONE);
                    }
                    showDialog("Iris Registration Status",registerIrisAPIResponse.getMessage());
//                    Toast.makeText(this, registerIrisAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callRegisterFaceAPI() {
//        if(faceImageFile1 == null){
//            Toast.makeText(this, "Please click Face Image 1 before submitting.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(faceImageFile2 == null){
            Toast.makeText(this, "Please click Face Image before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isConnected = new Utility().getInternetConnection(this);
        if(isConnected){
            if (appDialog != null) {
                appDialog.showLoader();
            }
            // convert field into Request body and image convert into multipart
//            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), faceImageFile1);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("imageA", faceImageFile1.getName(), fileBody);
            RequestBody fileBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), faceImageFile2);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("imageA", faceImageFile2.getName(), fileBody2);
            RequestBody rollNum =  RequestBody.create(MediaType.parse("text/plain"), rollNumber);

            RegisterFaceViewModel viewModel = ViewModelProviders.of(this).get(RegisterFaceViewModel.class);
            viewModel.init();
            viewModel.registerFaceApi(token,rollNum,body2);
            viewModel.getVolumesResponseLiveData().observe(this, registerFaceAPIResponse -> {
                if (appDialog != null) {
                    appDialog.dismiss();
                }
                if(registerFaceAPIResponse != null){
                    result.setFaceStatus(registerFaceAPIResponse.getData().getCandidateData().getFaceStatus());
                    result.setIrisStatus(registerFaceAPIResponse.getData().getCandidateData().getIrisStatus());
                    if (registerFaceAPIResponse.getStatus()){
                        faceTV.setText(registerFaceAPIResponse.getData().getCandidateData().getFaceStatus());
                        faceTV.setTextColor(this.getColor(R.color.green));
                        faceImagesLayout.setVisibility(View.GONE);
                        faceRegButtonsLayout.setVisibility(View.GONE);
                    }else{
                        faceImageFile2 = null;
                        faceImage2.setImageDrawable(this.getDrawable(R.drawable.ic_add));
                        faceImage2.setPadding(10,10,10,10);
                        faceImage2.setClickable(true);
                    }
                    showDialog("Face Registration Status",registerFaceAPIResponse.getMessage());
//                    Toast.makeText(this, registerFaceAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,R.string.error_msg, Toast.LENGTH_SHORT).show();
                    faceImageFile2 = null;
                    faceImage2.setImageDrawable(this.getDrawable(R.drawable.ic_add));
                    faceImage2.setPadding(10,10,10,10);
                    faceImage2.setClickable(true);
                }
            });
        }
        else {
            Toast.makeText(this, R.string.internet_connection, Toast.LENGTH_SHORT).show();
            faceImageFile2 = null;
            faceImage2.setImageDrawable(this.getDrawable(R.drawable.ic_add));
            faceImage2.setPadding(10,10,10,10);
            faceImage2.setClickable(true);
        }
    }

    /*****************************************************************************
     * Show notification dialog.
     *****************************************************************************/
    void showDialog(final String title, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(CandidateStatusActivity.this).create();
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
     * Called when application exits.
     *****************************************************************************/
    @Override
    protected void onDestroy()
    {
        // If we are in previewing, stop it
        stopCamera(false);

        // Release the handle
        mApis.closeDevice(mDeviceHandle);

        mUsbNotification.cancelNofitications();

        if (mUsbReceiver != null)
            unregisterReceiver(mUsbReceiver);


        super.onDestroy();
    }

    /*****************************************************************************
     * When IriShield attached to Android system, this function will be called.
     *****************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (!onCreated){
            callGetCandidateDataAPI();
        }

        openDevice();
    }

    /*****************************************************************************
     * Application wants to pause and resume later.
     *****************************************************************************/
    @Override
    protected void onPause()
    {
        stopCamera(false);
        super.onPause();
    }

    /*****************************************************************************
     * Override this function so as to stop camera when menu settings is opened.
     *****************************************************************************/
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event)
    {
        if (keycode == KeyEvent.KEYCODE_MENU)
        {
            stopCamera(false);
        }
        return super.onKeyDown(keycode, event);
    }

    /*****************************************************************************
     * Initialize camera and start a capturing process
     *****************************************************************************/
    private void startCamera(boolean sound, ImageView captureView, String side)
    {
        IddkResult ret = new IddkResult();
        if (!mIsCameraReady)
        {
            IddkInteger imageWidth = new IddkInteger();
            IddkInteger imageHeight = new IddkInteger();
            ret = mApis.initCamera(mDeviceHandle, imageWidth, imageHeight);
            if (ret.intValue() != IddkResult.IDDK_OK)
            {
                updateCurrentStatus(getString(R.string.cam_not_init));
                handleError(ret);
                return;
            }

            mIsCameraReady = true;

            updateCurrentStatus(getString(R.string.cam_ready));
        }
        if (!mIspreviewing)
        {
//            // We disable the start button
//            View button = findViewById(R.id.start_button_id);
//            button.setEnabled(false);
//            button = findViewById(R.id.stop_button_id);
//            button.setEnabled(true);

            if (sound)
            {
                mMediaData.moveEyeClosePlayer.start();
            }
            mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);

            // Get the current setting values
            if (setNativeConfig() < 0)
            {
                updateCurrentStatus(getString(R.string.config_get_failed));
                return;
            }

            // Start a capturing process
//            CaptureTask captureTask = new CaptureTask(mCaptureView, mCaptureViewLeft);
            CaptureTask captureTask = new CaptureTask(captureView, null,side);
            captureTask.execute(mApis, mCaptureResult, mCurrentStatus);

            mIspreviewing = true;
        }
    }

    /*****************************************************************************
     * This function is used to get capture information from menu settings. Before a capturing process starts, this function is re-called to check updated capture information.
     *****************************************************************************/
    private int setNativeConfig(){
        // Reset
        mTotalScore = 0;
        mUsableArea = 0;
        irisRegCurrentAction = 0;

        // Get the preferences in settings menu
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String operationMode = sharedPref.getString("operation_mode_pref", getString(R.string.settings_autocapture));
        if (operationMode.equals("0"))
        {
            mCaptureInfo.setCaptureOperationMode(IddkCaptureOperationMode.IDDK_AUTO_CAPTURE);
        }
        else if (operationMode.equals("1"))
        {
            mCaptureInfo.setCaptureOperationMode(IddkCaptureOperationMode.IDDK_OPERATOR_INITIATED_AUTO_CAPTURE);
        }

        String countStr = sharedPref.getString("count_interval_pref", "3");
        int iCount = 3;
        try
        {
            iCount = Integer.parseInt(countStr);
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
            showDialog(getString(R.string.error) + " !", getString(R.string.settings_correct_number));
            return -1;
        }
        mCaptureInfo.setCount(iCount);

        String captureModeStr = sharedPref.getString("capture_mode_pref", "0");
        if (captureModeStr.equals("0"))
        {
            mCaptureInfo.setCaptureMode(IddkCaptureMode.IDDK_TIMEBASED);
        }
        else if (captureModeStr.equals("1"))
        {
            mCaptureInfo.setCaptureMode(IddkCaptureMode.IDDK_FRAMEBASED);
        }

        // Timeout for process when press Start capture button on UI.
        String timeoutStr = sharedPref.getString("capture_timeout_pref", "15");
        int timeout = 15;
        try
        {
            timeout = Integer.parseInt(timeoutStr);
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
            showDialog(getString(R.string.error) + " !", getString(R.string.settings_correct_number_timeout));
            return -1;
        }
        mCaptureInfo.setTimeout(timeout);

        String qualityMode = sharedPref.getString("quality_mode_pref", "0");
        if (qualityMode.equals("0"))
        {
            mCaptureInfo.setQualitymode(IddkQualityMode.IDDK_QUALITY_NORMAL);
        }
        else if (qualityMode.equals("1"))
        {
            mCaptureInfo.setQualitymode(IddkQualityMode.IDDK_QUALITY_HIGH);
        }
        else if (qualityMode.endsWith("2"))
        {
            mCaptureInfo.setQualitymode(IddkQualityMode.IDDK_QUALITY_VERY_HIGH);
        }

        String streamImageScale = sharedPref.getString("stream_image_scale_pref", "1");
        if (streamImageScale.equals("1"))
        {
            mCaptureInfo.setStreamScale(1);
        }
        else if (streamImageScale.equals("2"))
        {
            mCaptureInfo.setStreamScale(2);
        }
        else if (streamImageScale.endsWith("4"))
        {
            mCaptureInfo.setStreamScale(4);
        }
        else if (streamImageScale.endsWith("8"))
        {
            mCaptureInfo.setStreamScale(8);
        }

        // Save image settings
        String prefixName = sharedPref.getString("prefix_name_pref", getString(R.string.settings_pre_default));
        mCaptureInfo.setPrefixName(prefixName);
        mCaptureCount++;

        boolean isSaveBestImages = sharedPref.getBoolean("best_images_pref", true);
        mCaptureInfo.setSaveBest(isSaveBestImages);

        String outputDirStr = sharedPref.getString("output_dir_pref", DEF_OUTPUT_DIRECTORY_PATH);
        if (!outputDirStr.trim().endsWith("/"))
        {
            outputDirStr += "/";
        }
        mCurrentOutputDir = outputDirStr;

        boolean isSaveStreamImages = sharedPref.getBoolean("stream_images_pref", true);
        mCaptureInfo.setSaveStream(isSaveStreamImages);

        // Set current device configuration
        IddkDeviceConfig deviceConfig = new IddkDeviceConfig();
        IddkResult iRet = mApis.getDeviceConfig(mDeviceHandle, deviceConfig);
        boolean isShowImages = (iRet.getValue() == IddkResult.IDDK_OK) ? deviceConfig.isEnableStream() : true;
        if (isShowImages == false)
        {
            showDialog(getResources().getString(R.string.warning), getResources().getString(R.string.stream_disable));
        }

        int streamScale = (int)deviceConfig.getStreamScale();
        if (streamScale > 1) {
            // Image stream is scaled
        }

        deviceConfig.setStreamScale((byte)mCaptureInfo.getStreamScale());

        iRet = mApis.setDeviceConfig(mDeviceHandle, deviceConfig);
        if (iRet.getValue() != IddkResult.IDDK_OK) {
            showDialog(getResources().getString(R.string.warning),
                    getResources().getString(R.string.msg_cannot_change_stream_scale)
                            + "\nError: " + iRet.toString()
                            + "\nCurrent stream scale = " + streamScale);
        }

        mCaptureInfo.setShowStream(isShowImages);
        if (!isShowImages)
        {
//            mCaptureView.setImageBitmap(null);
//            mCaptureViewLeft.setImageBitmap(null);
        }

        mIsCheckDedup = sharedPref.getBoolean("check_dedup_pref", true);

        // Threshold
        boolean isShowResult = sharedPref.getBoolean("show_result", true);
        String totalScoreStr = sharedPref.getString("threshold_total_score", "50");
        String usableAreaStr = sharedPref.getString("threshold_usable_area", "50");

        if (SettingsUtil.getSettings(getApplicationContext()) != null) {
            // If setting file exited, then use value in settings file
            Properties settings = SettingsUtil.readSettings(getApplicationContext());
            String showResult = settings.getProperty(PropertyName.ShowResult);
            isShowResult = !("0".equals(showResult));
            totalScoreStr = settings.getProperty(PropertyName.TotalScore);
            usableAreaStr = settings.getProperty(PropertyName.UsableArea);
        }

        int iTotalScore = 50;
        try
        {
            iTotalScore = Integer.parseInt(totalScoreStr);
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }

        int iUsableArea = 50;
        try
        {
            iUsableArea = Integer.parseInt(usableAreaStr);
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }

        mCaptureInfo.setShowResult(isShowResult);
        mCaptureInfo.setTotalScore(iTotalScore);
        mCaptureInfo.setUsableArea(iUsableArea);

        return 0;
    }

    /*****************************************************************************
     * Stop the capture and deinit camera
     *****************************************************************************/
    private IddkResult stopCamera(boolean sound)
    {
        IddkResult iRet = new IddkResult();
        iRet.setValue(IddkResult.IDDK_OK);
        if (mIspreviewing)
        {
//            // We enable the start button
//            View button = findViewById(R.id.start_button_id);
//            button.setEnabled(true);
//            button = findViewById(R.id.stop_button_id);
//            button.setEnabled(false);

            if (sound)
            {
                mMediaData.captureFinishedPlayer.start();
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            iRet = mApis.stopCapture(mDeviceHandle);
            if (iRet.getValue() != IddkResult.IDDK_OK)
            {
                handleError(iRet);
                return iRet;
            }

            mIspreviewing = false;
        }

        if (mIsCameraReady)
        {
            iRet = mApis.deinitCamera(mDeviceHandle);
            if (iRet.getValue() != IddkResult.IDDK_OK)
            {
                handleError(iRet);
                return iRet;
            }

            mIsCameraReady = false;
        }
        return iRet;
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

        // Get notification instance
        mUsbNotification = UsbNotification.getInstance(this);

        // Register detached event for the IriShield
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        updateListOfDevices();
    }

    /*****************************************************************************
     * A broadcast receiver that is used to receive the detached events whenever a IriShield device is detached from the Android system. This function also sends a message to notify the main thread
     * (GUI thread) to execute the post jobs.
     *****************************************************************************/
    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action) || UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                // Make a notice to user
                mUsbNotification.cancelNofitications();
                mUsbNotification.createNotification(getString(R.string.device_disconnected));

                // Play a sound when a IriShield is detached from the Android system
                mMediaData.deviceDisconnected.start();

                updateListOfDevices();

                // Send a message to main thread
                final Message msg = Message.obtain(mHandler, 0, null);
                mHandler.dispatchMessage(msg);
            }
            else if(ACTION_USB_PERMISSION.equals(intent.getAction())){
//				UsbDevice d = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean p = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                if(p){
                    // Send a message to main thread
                    final Message msg = Message.obtain(mHandler, 0, null);
                    mHandler.dispatchMessage(msg);
                }
            }
        }
    };

    /*****************************************************************************
     * A handler that is used to receive and handle the message sent from the broadcast receiver.
     *****************************************************************************/
    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            // Reset state of the application
            setInitState();

            // Scan and open device again
            openDevice();
        }
    };

    /*****************************************************************************
     * Reset all the internal states of the application. This function is called whenever device connection has been changed.
     *****************************************************************************/
    private void setInitState(){
        mIsCameraReady = false;
        mIsGalleryLoaded = false;
        mIspreviewing = false;
        mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);
        mIsJustError = false;

        DemoUtility.sleep(1000);
        mStatusTextView.setText(getString(R.string.opendevice_notfound));
//        mCaptureView.setImageBitmap(null);


//        mTabHost.setCurrentTab(TAB_CAPTURE_INDEX_ID);
//
//        if (mTabHost.getCurrentTab() == TAB_CAPTURE_INDEX_ID)
//        {
//            mStatusTextView.setText(getString(R.string.opendevice_notfound));
//            mCaptureView.setImageBitmap(null);
//            mCaptureViewLeft.setImageBitmap(null);
//
////            View view = findViewById(R.id.start_button_id);
////            view.setEnabled(false);
////            view = findViewById(R.id.stop_button_id);
////            view.setEnabled(false);
//        }
    }

    /*****************************************************************************
     * This function is used to scan and open IriShield. In case there are multiple IriShields attached to the Android system, IriShield at index 0 is opened as default.
     *****************************************************************************/
    private void openDevice() {
        // Clear any internal states
        IddkResult ret = new IddkResult();
//        mCaptureView.setImageBitmap(null);

//        mCaptureViewLeft.setImageBitmap(null);
//        mTabHost.setCurrentTab(TAB_CAPTURE_INDEX_ID);
        mIsCameraReady = false;
        mIsGalleryLoaded = false;
        mCurrentStatus.setValue(IddkCaptureStatus.IDDK_IDLE);
        mIspreviewing = false;

//        // Disable start and stop buttons
//        View view = findViewById(R.id.start_button_id);
//        view.setEnabled(false);
//        view = findViewById(R.id.stop_button_id);
//        view.setEnabled(false);


        if (mCurrentDeviceName == null || mCurrentDeviceName.isEmpty()) {
            updateCurrentStatus(getResources().getString(R.string.opendevice_notfound));
            return;
        }

        // We open the IriShield at index 0 as default
        ret = mApis.openDevice(mCurrentDeviceName, mDeviceHandle);
        if (ret.intValue() == IddkResult.IDDK_OK || ret.intValue() == IddkResult.IDDK_DEVICE_ALREADY_OPEN) {
            // Check device version
            // Our Android SDK not working well with IriShield device version <= 2.24
            IddkDeviceInfo deviceInfo = new IddkDeviceInfo();
            ret = mApis.getDeviceInfo(mDeviceHandle, deviceInfo);
            if (ret.getValue() == IddkResult.IDDK_OK) {
                int majorVersion = deviceInfo.getKernelVersion();
                int minorVersion = deviceInfo.getKernelRevision();

//                mBtSerial.setVisibility(View.VISIBLE);
                mSerialTextView.setText(deviceInfo.getSerialNumber());

                if (majorVersion <= 2 && minorVersion <= 24) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.opendevice_version_incompatible));
                    return;
                }
            } else {
                // Error occurs here
                handleError(ret);
                return;
            }

            updateCurrentStatus(getString(R.string.opendevice_connected));

//            // We can enable the start button from now
//            view = findViewById(R.id.start_button_id);
//            view.setEnabled(true);

            // Reset error status
            mIsJustError = false;


        } else {
            // Device not found or something wrong occurs
            if (ret.getValue() == IddkResult.IDDK_DEVICE_ACCESS_DENIED) {
                updateCurrentStatus(getResources().getString(R.string.opendevice_accessdenied));
                requestUsbDevicePermission();

//                view = findViewById(R.id.start_button_id);
//                view.setEnabled(true);
            } else {
                updateCurrentStatus(getResources().getString(R.string.opendevice_failed));
            }
        }
        //}

        // There is no IriShield attached to the Android system
        //updateListOfDevices(null);
        //updateCurrentStatus(getResources().getString(R.string.opendevice_notfound));
        //}
    }

    /*****************************************************************************
     * Requesting Usb Permission
     *****************************************************************************/
    private void requestUsbDevicePermission() {
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            if (device.getVendorId() == IriTech_VID) {
                mUsbManager.requestPermission(device, mPermissionIntent);
            }
        }
    }

    /*****************************************************************************
     * This function handles any errors that may occur in the program. Notice to disable start and stop button to prevent other errors.
     *****************************************************************************/
    public void handleError(IddkResult error)
    {
        mIsCameraReady = false;
        // If there is a problem with the connection
        if ((error.getValue() == IddkResult.IDDK_DEVICE_IO_FAILED) || (error.getValue() == IddkResult.IDDK_DEVICE_IO_DATA_INVALID) || (error.getValue() == IddkResult.IDDK_DEVICE_IO_TIMEOUT))
        {
            showDialog(getString(R.string.error), getString(R.string.connection_restart_ask));
            updateCurrentStatus(getString(R.string.connection_failed));
//            mCaptureView.setImageBitmap(null);

//            if (mTabHost.getCurrentTab() == TAB_CAPTURE_INDEX_ID)
//            {
//                updateCurrentStatus(getString(R.string.connection_failed));
//
////                View view = findViewById(R.id.start_button_id);
////                view.setEnabled(false);
////                view = findViewById(R.id.stop_button_id);
////                view.setEnabled(false);
//
//                mCaptureView.setImageBitmap(null);
//                mCaptureViewLeft.setImageBitmap(null);
//            }

            mIsJustError = true;
        }
        else
        {
            showDialog(getString(R.string.warning), DemoUtility.getErrorDesc(error));
        }
    }

    /*****************************************************************************
     * Update current camera status to the user via GUI.
     *****************************************************************************/
    private void updateCurrentStatus(final String newStatus)
    {
        mStatusTextView.post(new Runnable()
        {
            public void run()
            {
                mStatusTextView.setText(Html.fromHtml(newStatus.replace("\n", "<br>")));
            }
        });
    }

    /*****************************************************************************
     * Calling this function to invalidate the spinner that represents the list of IriShield(s) attached to the Android system.
     *****************************************************************************/
    @SuppressWarnings("unchecked")
    private void updateListOfDevices(){
        ArrayList<String> deviceDescs = new ArrayList<String>();
        mApis.scanDevices(deviceDescs);

        ArrayAdapter<String> adapter = null;
        if (deviceDescs == null || deviceDescs.size() == 0)
        {
            // Clear all the items in the spinner and notify it to refresh
            adapter = (ArrayAdapter<String>) mListOfDevices.getAdapter();
            if (adapter != null)
            {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
            mCurrentDeviceName = "";

//            mBtSerial.setVisibility(View.GONE);
            mSerialTextView.setText("");

            return;
        }
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_style, deviceDescs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListOfDevices.setAdapter(adapter);
        mCurrentDeviceName = mListOfDevices.getSelectedItem().toString();
    }

    /*****************************************************************************
     * This asynchronous task is run simultaneously with the main thread to update the current streaming images to captureView. A capturing process is also implemented in this class.
     *****************************************************************************/
    private class CaptureTask extends AsyncTask<Object, Bitmap, Integer>
    {
        ImageView captureView = null; // Right eye
        ImageView captureViewLeft = null; // Left eye
        String side = "left-eye";
        IddkResult iRet;
        boolean isBinocularDevice = false;
        private CountDownTimer countDownTimer;

        public CaptureTask(View captureView, View captureViewLeft, String side)
        {
            this.captureView = (ImageView) captureView;
            this.captureViewLeft = (ImageView) captureViewLeft;
            this.side = side;

            if (captureViewLeft != null)
            {
                IddkInteger isBino = new IddkInteger();
                mApis.Iddk_IsBinocular(mDeviceHandle, isBino);
                isBinocularDevice = (isBino.getValue() == 1) ? true : false;
                if (isBinocularDevice == false) // Monocular device
                {
                    this.captureViewLeft.setImageBitmap(null);
                    this.captureViewLeft.setVisibility(View.INVISIBLE);
                    this.captureViewLeft.getLayoutParams().height = 1;
                    this.captureViewLeft.getLayoutParams().width = 1;

                    this.captureView.getLayoutParams().width = mScreenWidth - 10;
                    this.captureView.getLayoutParams().height = (this.captureView.getLayoutParams().width / 4) * 3;
                }
                else
                {
                    this.captureViewLeft.setVisibility(View.VISIBLE);
                    this.captureViewLeft.getLayoutParams().width = mScreenWidth / 2 - 5;
                    this.captureViewLeft.getLayoutParams().height = (this.captureViewLeft.getLayoutParams().width / 4) * 3;

                    this.captureView.getLayoutParams().width = mScreenWidth / 2 - 5;
                    this.captureView.getLayoutParams().height = (this.captureView.getLayoutParams().width / 4) * 3;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.captureView.setPadding(0,0,0,0);
            int timeout = mCaptureInfo.getTimeout() * 1000; // miliseconds
            // You can monitor the progress here as well by changing the onTick() time
            countDownTimer = new CountDownTimer(timeout, 1000) {
                public void onTick(long millisUntilFinished) {
                    // You can monitor the progress here as well by changing the onTick() time
                }
                public void onFinish() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCurrentStatus.setValue(IddkCaptureStatus.IDDK_ABORT);
                            updateCurrentStatus("Capture timeout");
                            showDialog("Capture Status","Camera Capture Timeout!");
                            stopCamera(false);
                        }
                    });
                    //Toast.makeText(IriShieldDemo.this, "Capture timeout", Toast.LENGTH_LONG).show();
                }
            }.start();
        }

        /*****************************************************************************
         * Capturing process is implemented here. It runs simultaneously with the main thread and update streaming images to captureView. After the capturing process ends, we get the best image and
         * save it in a default directory.
         *****************************************************************************/
        protected Integer doInBackground(Object... params)
        {
            ArrayList<IddkImage> monoImages = new ArrayList<IddkImage>();
            IddkCaptureStatus captureStatus = new IddkCaptureStatus(IddkCaptureStatus.IDDK_IDLE);

            iRet = (IddkResult) params[1];
            Iddk2000Apis mApis = (Iddk2000Apis) params[0];

            boolean bRun = true;
            boolean eyeDetected = false;
            IddkEyeSubType subType = null;
            if (isBinocularDevice)
            {
                subType = new IddkEyeSubType(IddkEyeSubType.IDDK_BOTH_EYE);
            }
            else
            {
                subType = new IddkEyeSubType(IddkEyeSubType.IDDK_UNKNOWN_EYE);
            }
            IddkInteger maxEyeSubtypes = new IddkInteger();

            iRet = mApis.startCapture(mDeviceHandle, mCaptureInfo.getCaptureMode(), mCaptureInfo.getCount(), mCaptureInfo.getQualitymode(), mCaptureInfo.getCaptureOperationMode(), subType, true, null);

            if (iRet.intValue() != IddkResult.IDDK_OK)
            {
                mCaptureResult = iRet;
                return -1;
            }

            // Create folder for save stream
            // id-yyyy-mm-dd
            //StringBuilder outputStreamDirBuilder = new StringBuilder();
            //outputStreamDirBuilder.append(mCurrentOutputDir).append("/streamimages");
            //String outputStreamDir = outputStreamDirBuilder.toString();
            String outputStreamDir = mCurrentOutputDir;

            String outputIdStreamDir = "";
            Calendar cal = Calendar.getInstance();
            int date = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1; // Real month have to + 1
            int year = cal.get(Calendar.YEAR);
            if (mCaptureInfo.isShowStream() && (mCaptureInfo.isSaveStream() || mCaptureInfo.isSaveBest())) {
                StringBuilder outputDirStr = new StringBuilder();
                // /sdcard/iritech/outpur/streamimages/user-2019-05-22_11_41_15/
                outputDirStr.append(outputStreamDir).append("/");
                outputDirStr.append(mUserID).append("-").append(year).append("-").append(month).append("-").append(date).append("-")
                        .append(cal.get(Calendar.HOUR_OF_DAY)).append("_").append(cal.get(Calendar.MINUTE)).append("_").append(cal.get(Calendar.SECOND)).append("/");

                outputIdStreamDir = outputDirStr.toString();
                File file = new File(outputIdStreamDir);
                if (!file.exists() && !file.mkdirs())
                {
                    updateCurrentStatus(getString(R.string.cannot_create_stream_folder));
                    return -1;
                }
            }

            while (bRun)
            {
                if (mCaptureInfo.isShowStream())
                {
                    iRet = mApis.getStreamImage(mDeviceHandle, monoImages, maxEyeSubtypes, captureStatus);

                    if (iRet.intValue() == IddkResult.IDDK_OK)
                    {
                        Calendar cal2 = Calendar.getInstance();
                        int milis = cal2.get(Calendar.MILLISECOND);

                        StringBuilder filePathBuilder = new StringBuilder();
                        // /sdcard/iritech/outpur/streamimages/user-2019-05-22_11_41_15/
                        filePathBuilder.append(outputIdStreamDir).append("/");
                        filePathBuilder.append(mUserID).append("-").append(year).append("-").append(month).append("-").append(date).append("-")
                                .append(cal2.get(Calendar.HOUR_OF_DAY)).append("_").append(cal2.get(Calendar.MINUTE)).append("_").append(cal2.get(Calendar.SECOND))
                                .append("_").append(milis);

                        String filePath = filePathBuilder.toString();

                        if (isBinocularDevice)
                        {
                            Bitmap streamImageLeft = null;
                            Bitmap streamImageRight = null;
                            if (subType.getValue() == IddkEyeSubType.IDDK_LEFT_EYE)
                            {
                                streamImageLeft = convertBitmap(monoImages.get(0).getImageData(), monoImages.get(0).getImageWidth(), monoImages.get(0).getImageHeight());
                            }
                            else if (subType.getValue() == IddkEyeSubType.IDDK_RIGHT_EYE)
                            {
                                streamImageRight = convertBitmap(monoImages.get(0).getImageData(), monoImages.get(0).getImageWidth(), monoImages.get(0).getImageHeight());
                            }
                            else
                            // both eye
                            {
                                streamImageRight = convertBitmap(monoImages.get(0).getImageData(), monoImages.get(0).getImageWidth(), monoImages.get(0).getImageHeight());
                                streamImageLeft = convertBitmap(monoImages.get(1).getImageData(), monoImages.get(1).getImageWidth(), monoImages.get(1).getImageHeight());
                            }
                            publishProgress(streamImageRight, streamImageLeft);

                            if (mCaptureInfo.isSaveStream()) {
                                if(streamImageLeft != null) {
                                    filePath = filePathBuilder.toString() + "_left.raw";
                                    DemoUtility.SaveBin(filePath, monoImages.get(1).getImageData());
                                    Log.d("Stream", filePath);
                                }
                                if(streamImageRight != null) {
                                    filePath = filePathBuilder.toString() + "_right.raw";
                                    DemoUtility.SaveBin(filePath, monoImages.get(0).getImageData());
                                    Log.d("Stream", filePath);
                                }
                            }
                        }
                        else
                        {
                            Bitmap streamImage = convertBitmap(monoImages.get(0).getImageData(), monoImages.get(0).getImageWidth(), monoImages.get(0).getImageHeight());
                            publishProgress(streamImage);

                            if (mCaptureInfo.isSaveStream()) {
                                if(streamImage != null) {
                                    filePath += "_unknown.raw";
                                    DemoUtility.SaveBin(filePath, monoImages.get(0).getImageData());
                                }
                            }
                        }
                    }
                    else if (iRet.intValue() == IddkResult.IDDK_SE_NO_FRAME_AVAILABLE)
                    {
                        // when GetStreamImage returns IDDK_SE_NO_FRAME_AVAILABLE,
                        // it does not always mean that capturing process has been finished or encountered problems.
                        // It may be because new stream images are not available.
                        // We need to query the current capture status to know what happens.
                        iRet = mApis.getCaptureStatus(mDeviceHandle, captureStatus);
                        mCurrentStatus.setValue(captureStatus.getValue());
                    }
                }
                else
                {
                    iRet = mApis.getCaptureStatus(mDeviceHandle, captureStatus);
                    mCurrentStatus.setValue(captureStatus.getValue());
                    DemoUtility.sleep(60);
                }

                // If GetStreamImage and GetCaptureStatus cause no error, process the capture status
                if (iRet.intValue() == IddkResult.IDDK_OK)
                {
                    // Eye(s) is(are) detected
                    if (captureStatus.intValue() == IddkCaptureStatus.IDDK_CAPTURING)
                    {
                        if (!eyeDetected)
                        {
                            updateCurrentStatus(getString(R.string.capture_eyey_detected));
                            mMediaData.eyeDetectedPlayer.start();
                            eyeDetected = true;
                            mCurrentStatus.setValue(IddkCaptureStatus.IDDK_CAPTURING);
                        }
                    }
                    else if (captureStatus.intValue() == IddkCaptureStatus.IDDK_COMPLETE)
                    {
                        // Capture has finished
                        updateCurrentStatus(getString(R.string.capture_finished));
                        mMediaData.captureFinishedPlayer.start();
                        bRun = false;
                        mCurrentStatus.setValue(IddkCaptureStatus.IDDK_COMPLETE);
                    }
                    else if (captureStatus.intValue() == IddkCaptureStatus.IDDK_ABORT)
                    {
                        // Capture has been aborted
                        bRun = false;
                        mCurrentStatus.setValue(IddkCaptureStatus.IDDK_ABORT);
                    }
                }
                else
                {
                    // Terminate the capture if errors occur
                    bRun = false;
                }
            }

            mCaptureResult = iRet;
            if (mCurrentStatus.getValue() == IddkCaptureStatus.IDDK_COMPLETE)
            {
                // Get the best image
                mMonoBestImage = new ArrayList<IddkImage>();
                iRet = mApis.getResultImage(mDeviceHandle, new IddkImageKind(IddkImageKind.IDDK_IKIND_K1), new IddkImageFormat(IddkImageFormat.IDDK_IFORMAT_MONO_RAW), (byte) 1, mMonoBestImage, maxEyeSubtypes);
                if ((!isBinocularDevice && iRet.intValue() == IddkResult.IDDK_OK)
                        || (isBinocularDevice && (iRet.intValue() == IddkResult.IDDK_OK || iRet.intValue() == IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED || iRet.intValue() == IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED)))
                {
                    // Showing the best image so that user can see it
                    Bitmap bestImage = null;
                    Bitmap bestImageRight = null;
                    Bitmap bestImageLeft = null;
                    if (isBinocularDevice)
                    {
                        if (iRet.intValue() == IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED)
                        {
                            bestImageLeft = convertBitmap(mMonoBestImage.get(1).getImageData(), mMonoBestImage.get(1).getImageWidth(), mMonoBestImage.get(1).getImageHeight());
                        }
                        else if (iRet.intValue() == IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED)
                        {
                            bestImageRight = convertBitmap(mMonoBestImage.get(0).getImageData(), mMonoBestImage.get(0).getImageWidth(), mMonoBestImage.get(0).getImageHeight());
                        }
                        else
                        // both eye
                        {
                            bestImageRight = convertBitmap(mMonoBestImage.get(0).getImageData(), mMonoBestImage.get(0).getImageWidth(), mMonoBestImage.get(0).getImageHeight());
                            bestImageLeft = convertBitmap(mMonoBestImage.get(1).getImageData(), mMonoBestImage.get(1).getImageWidth(), mMonoBestImage.get(1).getImageHeight());
                        }
                        publishProgress(bestImageRight, bestImageLeft);
                    }
                    else
                    {
                        bestImage = convertBitmap(mMonoBestImage.get(0).getImageData(), mMonoBestImage.get(0).getImageWidth(), mMonoBestImage.get(0).getImageHeight());
                        publishProgress(bestImage);
                    }

                    // Print the total score and usable area
                    ArrayList<IddkIrisQuality> quality = new ArrayList<IddkIrisQuality>();
                    iRet = mApis.getResultQuality(mDeviceHandle, quality, maxEyeSubtypes);
                    if (isBinocularDevice)
                    {

                        if (mCaptureInfo.isShowResult())
                        {
                            if (iRet.intValue() == IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED)
                            {
                                updateCurrentStatus(getString(R.string.right_eye) + ": "+getString(R.string.total_score)+" = " + quality.get(0).getTotalScore()
                                        + ", "+getString(R.string.usable_area)+" = " + quality.get(0).getUsableArea() + checkIrisQuality(quality.get(0), mCaptureInfo));
                            }
                            else if (iRet.intValue() == IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED)
                            {
                                updateCurrentStatus(getString(R.string.left_eye) + ": "+getString(R.string.total_score)+" = " + quality.get(1).getTotalScore()
                                        + ", "+getString(R.string.usable_area)+" = " + quality.get(1).getUsableArea() + checkIrisQuality(quality.get(1), mCaptureInfo));
                            }
                            else if (iRet.intValue() == IddkResult.IDDK_OK)
                            {
                                updateCurrentStatus(getString(R.string.right_eye) + ": "+getString(R.string.total_score)+" = " + quality.get(0).getTotalScore()
                                        + ", "+ getString(R.string.usable_area)+" = " + quality.get(0).getUsableArea() + checkIrisQuality(quality.get(0), mCaptureInfo) + "\n"
                                        + getString(R.string.left_eye) + ": "+getString(R.string.total_score)+" = " + quality.get(1).getTotalScore()
                                        + ", "+getString(R.string.usable_area)+" = " + quality.get(1).getUsableArea() + checkIrisQuality(quality.get(1), mCaptureInfo));
                            }
                        }
                    }
                    else
                    {
                        if (mCaptureInfo.isShowResult()) {
                            String currStatus = getString(R.string.unknown_eye) + ": "+ getString(R.string.total_score) + " = " + quality.get(0).getTotalScore() + ", "+getString(R.string.usable_area)+ " = " + quality.get(0).getUsableArea() + checkIrisQuality(quality.get(0), mCaptureInfo);
                            updateCurrentStatus(currStatus);
                            if (this.side.equals("left-eye")){
                                leftEyeScore = quality.get(0).getUsableArea();
                                irisImageFile1 = Utility.getFileWithName(getApplicationContext(), bestImage, "Left.jpeg");
                            }else{
                                rightEyeScore = quality.get(0).getUsableArea();
                                irisImageFile2 = Utility.getFileWithName(getApplicationContext(), bestImage, "Right.jpeg");
                            }

//                            if (currStatus.contains("FAILED")){
//                                showDialog("Capture Status", getString(R.string.unknown_eye) + ": "+ getString(R.string.total_score) + " = " + quality.get(0).getTotalScore() + ", "+getString(R.string.usable_area)+ " = " + quality.get(0).getUsableArea() + "==> Image Status: FAILED \nPlease click the image again.");
//                            }else{
//                                showDialog("Capture Status", getString(R.string.unknown_eye) + ": "+ getString(R.string.total_score) + " = " + quality.get(0).getTotalScore() + ", "+getString(R.string.usable_area)+ " = " + quality.get(0).getUsableArea() + "==> Image Status: PASSED \nAcceptable Iris Image.");
//                            }
                        }



                    }

//                    // TODO: Using IriCore to compare iris image with the iris in Database or some where
//                    compare(mMonoBestImage);

                    // Save the best image
                    if (mCaptureInfo.isSaveBest())
                    {
                        FileOutputStream out = null;
                        FileOutputStream outLeft = null;
                        FileOutputStream outRight = null;
                        try
                        {
                            Calendar cal3 = Calendar.getInstance();
                            StringBuilder outputDirStr = new StringBuilder();
                            outputDirStr.append(outputIdStreamDir).append("/");
                            outputDirStr.append(mCaptureInfo.getPrefixName()).append("_").append(mUserID).append("-")
                                    .append(year).append("-").append(month).append("-").append(date).append("-")
                                    .append(cal3.get(Calendar.HOUR_OF_DAY)).append("_").append(cal3.get(Calendar.MINUTE))
                                    .append("_").append(cal3.get(Calendar.SECOND)).append("_").append(cal3.get(Calendar.MILLISECOND))
                                    .append("_s").append(quality.get(0).getTotalScore()).append("_u").append(quality.get(0).getUsableArea());

                            if (isBinocularDevice)
                            {
                                if (iRet.getValue() == IddkResult.IDDK_SE_RIGHT_FRAME_UNQUALIFIED)
                                {
                                    outLeft = new FileOutputStream(outputDirStr.toString() + "_left.jpg");
                                    if (outLeft != null)
                                    {
                                        bestImageLeft.compress(Bitmap.CompressFormat.JPEG, 100, outLeft);
                                    }
                                    DemoUtility.SaveBin(outputDirStr.toString() + "_left.raw", mMonoBestImage.get(1).getImageData());
                                }
                                else if (iRet.getValue() == IddkResult.IDDK_SE_LEFT_FRAME_UNQUALIFIED)
                                {
                                    outRight = new FileOutputStream(outputDirStr.toString() + "_right.jpg");
                                    if (outRight != null)
                                    {
                                        bestImageRight.compress(Bitmap.CompressFormat.JPEG, 100, outRight);
                                    }
                                    DemoUtility.SaveBin(outputDirStr.toString() + "_right.raw", mMonoBestImage.get(0).getImageData());
                                }
                                else if (iRet.getValue() == IddkResult.IDDK_OK)
                                {
                                    outLeft = new FileOutputStream(outputDirStr.toString() + "_left.jpg");
                                    if (outLeft != null)
                                    {
                                        bestImageLeft.compress(Bitmap.CompressFormat.JPEG, 100, outLeft);
                                    }
                                    DemoUtility.SaveBin(outputDirStr.toString() + "_left.raw", mMonoBestImage.get(1).getImageData());

                                    outRight = new FileOutputStream(outputDirStr.toString() + "_right.jpg");
                                    if (outRight != null)
                                    {
                                        bestImageRight.compress(Bitmap.CompressFormat.JPEG, 100, outRight);
                                    }
                                    DemoUtility.SaveBin(outputDirStr.toString() + "_right.raw", mMonoBestImage.get(0).getImageData());
                                }
                            }
                            else
                            {
                                out = new FileOutputStream(outputDirStr.toString() + "_unknown.jpg");
                                if (out != null)
                                {
                                    bestImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                }
                                DemoUtility.SaveBin(outputDirStr.toString() + "_unknown.raw", mMonoBestImage.get(0).getImageData());
                            }
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                            iRet.setValue(IddkResult.IDDK_UNEXPECTED_ERROR);
                        }
                    }
                }
                if (iRet.intValue() == IddkResult.IDDK_SE_NO_QUALIFIED_FRAME)
                {
                    // No qualified images
                    iRet.setValue(IddkResult.IDDK_SE_NO_QUALIFIED_FRAME);
                    updateCurrentStatus(getString(R.string.capture_noqualified));
                    mMediaData.noEyeQualifiedPlayer.start();
                }
            }

            return 0;
        }

        private String checkIrisQuality(IddkIrisQuality irisQuality, IddkCaptureInfo settings) {
            String passedFormat = " ==> <font color='green'>%s</font>";
            String failedFormat = " ==> <font color='red'>%s</font>";
            if (irisQuality.getTotalScore() >= settings.getTotalScore()
                    && irisQuality.getUsableArea() >= settings.getUsableArea()) {
                return String.format(passedFormat, "PASSED");
            }
            return String.format(failedFormat, "FAILED");
        }

        /*****************************************************************************
         * Convert the Grayscale image from the camera to bitmap format that can be used to show to the users.
         *****************************************************************************/
        private Bitmap convertBitmap(byte[] rawImage, int imageWidth, int imageHeight)
        {
            byte[] Bits = new byte[rawImage.length * 4]; // That's where the RGBA array goes.

            int j;
            for (j = 0; j < rawImage.length; j++)
            {
                Bits[j * 4] = (byte) (rawImage[j]);
                Bits[j * 4 + 1] = (byte) (rawImage[j]);
                Bits[j * 4 + 2] = (byte) (rawImage[j]);
                Bits[j * 4 + 3] = -1; // That's the alpha
            }

            // Now put these nice RGBA pixels into a Bitmap object
            mCurrentBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
            mCurrentBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));

            return mCurrentBitmap;
        }

        /*****************************************************************************
         * Update the current streaming image to the captureView
         *****************************************************************************/
        protected void onProgressUpdate(Bitmap... bm)
        {
            if (isBinocularDevice)
            {
                captureView.setImageBitmap(bm[0]);
                captureViewLeft.setImageBitmap(bm[1]);
            }
            else
            {
                captureView.setImageBitmap(bm[0]);
            }
        }

        /*****************************************************************************
         * Post processing after the capturing process ends
         *****************************************************************************/
        protected void onPostExecute(Integer result)
        {
            countDownTimer.cancel();

            IddkResult stopResult = stopCamera(false);
            if (iRet.getValue() != IddkResult.IDDK_OK && stopResult.getValue() != iRet.getValue())
            {
                handleError(iRet);
            }
        }
    }


}